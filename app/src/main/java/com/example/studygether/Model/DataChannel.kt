package com.example.studygether.Model

data class DataChannel(
    val id: String,
    val name: String,
    val description: String,
    val memberId: List<String> = emptyList()
)