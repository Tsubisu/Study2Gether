package com.example.studygether.Repository

import com.example.studygether.Model.User
import com.google.firebase.auth.FirebaseUser

interface AuthenticationRepository {
    suspend fun loginAndFetchUser(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>


    fun getCurrentUser(): FirebaseUser?
}