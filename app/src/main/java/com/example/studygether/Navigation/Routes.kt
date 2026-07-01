package com.example.studygether.Navigation
import com.example.studygether.Model.User
import kotlinx.serialization.Serializable

//Graph for Main Application
@Serializable object MainGraphRoute

//App Screens
@Serializable object HomePage
@Serializable object ConvoList
@Serializable data class Convo(val name:String, val image:Int)
@Serializable object ChannelList

@Serializable data class Channel(val channelName:String, val channelLogo:Int, val channelMemberCount:Int)
@Serializable object Setting

@Serializable object Profile



//Graph for authentication related screens
@Serializable object AuthGraph
//Auth Screens
@Serializable object Login
@Serializable object CommunityCreation
@Serializable object ForgetPassword
@Serializable object Otp
@Serializable object PasswordChange
