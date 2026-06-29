package com.example.studygether.Repository

interface ChannelRepo {
     fun joinChannel(channelId: String, userId: String,callback: (Boolean, String) -> Unit)
     fun leaveChannel(channelId: String, userId:String, callback: (Boolean, String) -> Unit)

    fun assignModerator(channelId: String, userId: String, callback: (Boolean, String) -> Unit)

    fun removeModerator(channelId: String, userId: String, callback: (Boolean, String) -> Unit)
}
