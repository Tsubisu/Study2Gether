package com.example.studygether.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studygether.Model.ProfileModel
import com.example.studygether.Repository.ProfileRepo
import androidx.lifecycle.ViewModel

class ProfileViewModel(val repo: ProfileRepo): ViewModel(){
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _userProfile = MutableLiveData<ProfileModel?>()
    val userProfile: LiveData<ProfileModel?> get() = _userProfile
    val currentUserId: String? get() = repo.getCurrentId()

    fun getUserProfile(id: String) {
        _loading.value = true
        repo.getUserProfile(id) { success, _, profile ->
            _loading.value = false
            if (success) {
                _userProfile.value = profile
            }
        }
    }

    fun updateUsername(id: String, newUsername: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repo.updateUsername(id, newUsername) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }
}