package com.example.studygether.Model

data class ChannelModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val memberId: List<String> = emptyList(),
    val moderators: List<String> = emptyList()
)