package com.example.studygether.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygether.App.SessionState
import com.example.studygether.Model.User
import com.example.studygether.Model.Community
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    val currentUser: StateFlow<User?> = SessionState.currentUser

    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()

    private val db = FirebaseDatabase.getInstance()
    private val usersRef = db.getReference("users")

    fun uploadProfileImage(context: Context, uri: Uri) {
        val uid = currentUser.value?.id ?: return
        _isUploading.value = true
        com.example.studygether.Repository.AppRepositories.imageRepository.uploadImage(context, uri) { result ->
            if (result != null) {
                viewModelScope.launch {
                    com.example.studygether.Repository.AppRepositories.userRepository.updateProfileImage(uid, result.url, result.publicId)
                    _isUploading.value = false
                }
            } else {
                _isUploading.value = false
            }
        }
    }

    fun updateStatus(status: String) {
        val uid = currentUser.value?.id ?: return
        viewModelScope.launch {
            usersRef.child(uid).child("status").setValue(status)
                .addOnSuccessListener {
                    // Locally sync SessionState if needed
                    SessionState.currentUser.value?.let { user ->
                        SessionState.setUser(user.copy(status = status))
                    }
                }
        }
    }

    fun updateCustomStatus(customStatus: String) {
        val uid = currentUser.value?.id ?: return
        viewModelScope.launch {
            usersRef.child(uid).child("customStatus").setValue(customStatus)
                .addOnSuccessListener {
                    SessionState.currentUser.value?.let { user ->
                        SessionState.setUser(user.copy(customStatus = customStatus))
                    }
                }
        }
    }

    fun createCommunity(
        name: String,
        isPublic: Boolean,
        logoUrl: String,
        logoPublicId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val uid = currentUser.value?.id ?: return
        viewModelScope.launch {
            val community = Community(
                name = name,
                isPublic = isPublic,
                profileImageUrl = logoUrl,
                profileImagePublicId = logoPublicId,
                createdAt = System.currentTimeMillis()
            )
            val result = com.example.studygether.Repository.AppRepositories.communityRepository.createCommunity(community, uid)
            result.fold(
                onSuccess = { communityId ->
                    com.example.studygether.Repository.AppRepositories.communityRepository.selectCommunity(uid, communityId)
                    onSuccess()
                },
                onFailure = { error ->
                    onFailure(error.message ?: "Failed to create community")
                }
            )
        }
    }

    fun logout() {
        SessionState.clear()
    }
}
