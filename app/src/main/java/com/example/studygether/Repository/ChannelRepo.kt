package com.example.studygether.Repository

interface ChannelRepo {
    suspend fun joinChannel(channelId: String, userId: String): Boolean
    suspend fun leaveChannel(channelId: String, userId: String): Boolean
}
class ChannelRepositoryImpl : ChannelRepo {
    override suspend fun joinChannel(channelId: String, userId: String): Boolean {
        return try {
            true
        } catch (e: Exception) {
            false
        }
    }
    override suspend fun leaveChannel(channelId: String, userId: String): Boolean {
        return try {
            true
        } catch (e: Exception) {
            false
        }
    }
}