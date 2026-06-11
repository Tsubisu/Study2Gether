package com.example.studygether.Model

data class CurrentUser(val id :Int)

data class Users(
    val id:Int,
    val profilePicture:Int,
    val name:String,
    val isBlocked:Boolean,
    )
