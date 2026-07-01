package com.example.studygether.Model

data class Community(
    val id: String = "",
    val name: String = "",
    val profileImageUrl: String = "",
    val profileImagePublicId: String = "",
    val creatorId: String = "",
    val createdAt: Long = 0L,
    val isPublic: Boolean = false,
)


data class CommunityMember(
    val joinedAt: Long = 0L,
    val defaultAnonymous: Boolean = false
)
