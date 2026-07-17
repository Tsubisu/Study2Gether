package com.example.studygether.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygether.App.SessionState
import com.example.studygether.App.UserCommunity
import com.example.studygether.Model.User
import com.example.studygether.Repository.AppRepositories
import com.example.studygether.Utility.ZegoService
import im.zego.zim.entity.ZIMConversation
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ConvoListViewModel : ViewModel() {

    private val communityRepository = AppRepositories.communityRepository
    private val userRepository = AppRepositories.userRepository

    private val _communityMembers = MutableStateFlow<List<User>>(emptyList())
    val communityMembers: StateFlow<List<User>> = _communityMembers.asStateFlow()

    private val _activeConversations = MutableStateFlow<List<ZIMConversation>>(emptyList())
    val activeConversations: StateFlow<List<ZIMConversation>> = _activeConversations.asStateFlow()

    val blockedUserIds: StateFlow<Set<String>> = ZegoService.blockedUserIds

    init {
        
        viewModelScope.launch {
            combine(
                UserCommunity.currentUserSelectedCommunity,
                SessionState.currentUser
            ) { community, currentUser ->
                Pair(community, currentUser)
            }.collectLatest { (community, currentUser) ->
                if (community != null && currentUser != null) {
                    communityRepository.observeCommunityMembers(community.id).collectLatest { memberIds ->
                        val currentUid = currentUser.id
                        val filteredIds = memberIds.filter { it != currentUid }
                        
                        
                        val membersList = mutableListOf<User>()
                        filteredIds.forEach { id ->
                            val userResult = userRepository.getUser(id)
                            userResult.onSuccess { user ->
                                if (user != null) {
                                    membersList.add(user)
                                    _communityMembers.value = membersList.toList()
                                }
                            }
                        }
                        _communityMembers.value = membersList.toList()
                        refreshActiveChats()
                    }
                } else {
                    _communityMembers.value = emptyList()
                    _activeConversations.value = emptyList()
                }
            }
        }
    }

    fun refreshActiveChats() {
        ZegoService.queryConversations { list, _ ->
            if (list != null) {
                
                val memberIds = _communityMembers.value.map { it.id }.toSet()
                val filteredList = list.filter { it.conversationID in memberIds }
                _activeConversations.value = filteredList
            }
        }
    }

    fun blockUser(userId: String) {
        ZegoService.blockUser(userId) { success ->
            if (success) {
                refreshActiveChats()
            }
        }
    }

    fun unblockUser(userId: String) {
        ZegoService.unblockUser(userId) { success ->
            if (success) {
                refreshActiveChats()
            }
        }
    }
}
