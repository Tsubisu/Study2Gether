package com.example.studygether.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygether.App.SessionState
import com.example.studygether.App.UserCommunity
import com.example.studygether.Model.ChannelModel
import com.example.studygether.Repository.AppRepositories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChannelsViewModel : ViewModel() {

    private val communityRepository = AppRepositories.communityRepository
    private val channelRepository = AppRepositories.channelRepository

    val selectedCommunity = UserCommunity.currentUserSelectedCommunity

    @OptIn(ExperimentalCoroutinesApi::class)
    val channels: StateFlow<List<ChannelModel>> = selectedCommunity
        .flatMapLatest { community ->
            if (community != null) {
                channelRepository.observeChannelsForCommunity(community.id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isOwner = MutableStateFlow(false)
    val isOwner: StateFlow<Boolean> = _isOwner.asStateFlow()

    init {
        viewModelScope.launch {
            combine(selectedCommunity, SessionState.currentUser) { community, user ->
                if (community != null && user != null) {
                    communityRepository.isCommunityOwner(community.id, user.id).getOrDefault(false)
                } else false
            }.collect { owner ->
                _isOwner.value = owner
            }
        }
    }

    fun createChannel(name: String, description: String, onResult: (Result<String>) -> Unit) {
        val community = selectedCommunity.value ?: return onResult(Result.failure(Exception("No community selected")))
        val user = SessionState.currentUser.value ?: return onResult(Result.failure(Exception("Not logged in")))

        viewModelScope.launch {
            val result = channelRepository.createChannel(community.id, name, description, user.id)
            onResult(result)
        }
    }
}
