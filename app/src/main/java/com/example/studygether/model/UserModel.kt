package com.example.studygether.model

data class UserModel(
    val id : String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
)
{
    fun toMap() : Map<String, Any?>{
        return mapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "password" to password,
        )
    }
}
