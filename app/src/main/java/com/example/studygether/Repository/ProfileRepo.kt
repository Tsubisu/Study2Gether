package com.example.studygether.Repository

import com.example.studygether.Model.ProfileModel

interface ProfileRepo {
    fun getUserProfile(id: String, callback: (Boolean, String, ProfileModel?) -> Unit)

    fun updateUsername(id: String, newUsername: String, callback: (Boolean, String) -> Unit)

    fun getCurrentId(): String?
}