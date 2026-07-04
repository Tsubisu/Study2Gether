package com.example.studygether.Navigation
import com.example.studygether.Model.User
import kotlinx.serialization.Serializable

//Graph for Main Application
@Serializable object MainGraphRoute

@Serializable
object Home

//App Screens
@Serializable object HomePage
@Serializable object ConvoList
@Serializable data class Convo(val name:String, val image:Int, val targetUserId:String)
@Serializable data class VideoCall(val roomId: String, val targetUserId: String)
@Serializable object ChannelList

@Serializable data class Channel(val channelName:String, val channelLogo:Int, val channelMemberCount:Int)
@Serializable object Setting

@Serializable object Profile
@Serializable object ThemeSelection
@Serializable object Security



//Graph for authentication related screens
@Serializable object AuthGraph
//Auth Screens
@Serializable object Login
@Serializable object CommunityCreation
@Serializable object ForgetPassword
@Serializable object Otp
@Serializable object PasswordChange
 @Serializable object SignIn