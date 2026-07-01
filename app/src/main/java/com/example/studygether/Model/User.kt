package com.example.studygether.Model


data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val profileImagePublicId: String = "",
    val createdAt: Long = 0L
)
