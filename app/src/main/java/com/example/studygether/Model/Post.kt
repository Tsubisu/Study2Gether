package com.example.studygether.Model

data class Post(
    val id: String = "",
    val channelId: String = "",
    val communityId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: Long = 0L,
    val likesCount: Int = 0,
    val dislikesCount: Int = 0
)
