package com.example.studygether.Navigation
import com.example.studygether.Model.Users
import kotlinx.serialization.Serializable


//Graph for authentication related screens
@Serializable object AuthGraph

//Graph for Main Application
@Serializable object MainGraph

//App Screens
@Serializable object HomePage
@Serializable object ConvoList
@Serializable data class Convo(val name:String, val image:Int)
@Serializable object Channels
@Serializable object Setting

@Serializable object Profile

//Auth Screens
@Serializable object Login
@Serializable object CommCreation
@Serializable object ForgotPassword
@Serializable object Otp
@Serializable object PasswordChange