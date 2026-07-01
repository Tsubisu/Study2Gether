package com.example.studygether.Repository

import com.example.studygether.Model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface UserRepository {
    suspend fun registerUser(email: String, username: String, password: String): Result<String>
    suspend fun getUser(id: String): Result<User?>
    suspend fun getUserByEmail(email: String): Result<User?>
    suspend fun updateProfileImage(id: String, url: String, publicId: String): Result<Unit>
    suspend fun updateUsername(id: String, username: String): Result<Unit>
    fun observeUser(id: String): Flow<User?>
}