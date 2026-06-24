package com.example.studygether.repository

import com.example.studygether.model.ProfileModel

interface ProfileRepo {
    fun getUserProfile(id: String, callback: (Boolean, String, ProfileModel?) -> Unit)

    fun updateUsername(id: String, newUsername: String, callback: (Boolean, String) -> Unit)

    fun getCurrentId(): String?
}