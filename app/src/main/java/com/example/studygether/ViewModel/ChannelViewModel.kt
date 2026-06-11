package com.example.studygether.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studygether.Model.ChannelModel
import com.example.studygether.Repository.ChannelRepo

class ChannelViewModel(val repo: ChannelRepo) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _allChannels = MutableLiveData<List<ChannelModel?>?>()
    val allChannels: LiveData<List<ChannelModel?>?> get() = _allChannels

    private val _channel = MutableLiveData<ChannelModel?>()
    val channel: LiveData<ChannelModel?> get() = _channel

    fun joinChannel(channelId: String, userId: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repo.joinChannel(channelId, userId) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }

    fun leaveChannel(channelId: String, userId: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repo.leaveChannel(channelId, userId) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }

    fun assignModerator(channelId: String, userId: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repo.assignModerator(channelId, userId) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }

    fun removeModerator(channelId: String, userId: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repo.removeModerator(channelId, userId) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }
}
