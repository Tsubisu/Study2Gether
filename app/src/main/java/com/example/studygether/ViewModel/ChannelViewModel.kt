package com.example.studygether.ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygether.Model.DataChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.studygether.Repository.ChannelRepo

class ChannelViewModel(
    private val repository: ChannelRepo,
    private val currentUserId: String
) : ViewModel() {

    private val _channelState = MutableStateFlow< DataChannel?>(null)
    val channelState: StateFlow<DataChannel?> = _channelState.asStateFlow()
    fun loadChannel(channel: DataChannel) {
        _channelState.value = channel
    }

    fun Members() {
        val channel = _channelState.value ?: return
        val isMember = channel.memberId.contains(currentUserId)

        viewModelScope.launch {
            if (isMember) {
                val success = repository.leaveChannel(channel.id, currentUserId)
                if (success) {
                    _channelState.update { current ->
                        current?.copy(memberId = current.memberId - currentUserId)
                    }
                }
            } else {
                val success = repository.joinChannel(channel.id, currentUserId)
                if (success) {
                    _channelState.update { current ->
                        current?.copy(memberId = current.memberId + currentUserId)
                    }
                }
            }
        }
    }
}