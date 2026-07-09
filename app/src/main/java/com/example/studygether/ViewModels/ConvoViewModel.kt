package com.example.studygether.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygether.App.SessionState
import com.example.studygether.App.UserCommunity
import com.example.studygether.Repository.AppRepositories
import com.example.studygether.Utility.ZegoService
import im.zego.zim.entity.ZIMMessage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ConvoViewModel : ViewModel() {

    private val communityRepository = AppRepositories.communityRepository

    private val _messages = MutableStateFlow<List<ZIMMessage>>(emptyList())
    val messages: StateFlow<List<ZIMMessage>> = _messages.asStateFlow()

    private val _isTargetMemberOfSameCommunity = MutableStateFlow(false)
    val isTargetMemberOfSameCommunity: StateFlow<Boolean> = _isTargetMemberOfSameCommunity.asStateFlow()

    private val _targetUser = MutableStateFlow<com.example.studygether.Model.User?>(null)
    val targetUser: StateFlow<com.example.studygether.Model.User?> = _targetUser.asStateFlow()

    private val _targetUserId = MutableStateFlow<String?>(null)
    val targetUserId: StateFlow<String?> = _targetUserId.asStateFlow()

    val blockedUserIds: StateFlow<Set<String>> = ZegoService.blockedUserIds

    private val onNewMessage = { fromUserId: String, message: ZIMMessage ->
        if (fromUserId == _targetUserId.value) {
            val alreadyExists = _messages.value.any { it.messageID == message.messageID }
            if (!alreadyExists) {
                _messages.value = _messages.value + message
            }
        }
    }

    init {
        ZegoService.setOnNewMessageListener(onNewMessage)
    }

    fun initChat(targetId: String) {
        _targetUserId.value = targetId
        viewModelScope.launch {
            AppRepositories.userRepository.getUser(targetId).onSuccess { user ->
                _targetUser.value = user
            }
        }
        loadHistory()
        checkCommunityConstraint(targetId)
    }

    private fun checkCommunityConstraint(targetId: String) {
        viewModelScope.launch {
            val community = UserCommunity.currentUserSelectedCommunity.value
            if (community != null) {
                communityRepository.isMember(community.id, targetId).fold(
                    onSuccess = { isMember ->
                        _isTargetMemberOfSameCommunity.value = isMember
                    },
                    onFailure = {
                        _isTargetMemberOfSameCommunity.value = false
                    }
                )
            } else {
                _isTargetMemberOfSameCommunity.value = false
            }
        }
    }

    fun loadHistory() {
        val targetId = _targetUserId.value ?: return
        ZegoService.queryHistory(targetId, null, 50) { list, _ ->
            if (list != null) {
                _messages.value = list
            }
        }
    }

    fun sendMessage(text: String) {
        val targetId = _targetUserId.value ?: return
        if (text.isBlank()) return
        
        if (!_isTargetMemberOfSameCommunity.value) {
            android.util.Log.e("ConvoViewModel", "Cannot send message: User is not in the same community.")
            return
        }

        if (blockedUserIds.value.contains(targetId)) {
            android.util.Log.e("ConvoViewModel", "Cannot send message: User is blocked.")
            return
        }

        ZegoService.sendMessage(targetId, text) { success, sentMsg ->
            if (success && sentMsg != null) {
                _messages.value = _messages.value + sentMsg
            }
        }
    }

    fun blockUser() {
        val targetId = _targetUserId.value ?: return
        ZegoService.blockUser(targetId) {
            // State automatically propagates
        }
    }

    fun unblockUser() {
        val targetId = _targetUserId.value ?: return
        ZegoService.unblockUser(targetId) {
            // State automatically propagates
        }
    }

    fun startCall(callback: (String?) -> Unit) {
        val targetId = _targetUserId.value ?: return callback(null)
        ZegoService.startCall(targetId, callback)
    }

    override fun onCleared() {
        super.onCleared()
        ZegoService.setOnNewMessageListener(null)
    }
}