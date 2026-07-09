package com.example.studygether.data.repository

import com.example.studygether.data.model.User

interface AuthRepository {
    fun loginWithEmail(
        email: String,
        password: String,
        onResult: (Result<User>) -> Unit
    )

    fun resetPassword(
        email: String,
        onResult: (Result<Unit>) -> Unit
    )

    fun getCurrentUser(): User?
    fun logout()
}
