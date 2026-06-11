package com.example.studygether.Repository

interface ProfileRepo {
    fun getUserProfile(id: String, callback: (Boolean, String) -> Unit)

    fun updateUsername(id: String, newUsername: String, callback: (Boolean, String) -> Unit)

    fun getCurrentid(): String?
}