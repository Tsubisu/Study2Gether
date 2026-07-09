package com.example.studygether.Repository

import com.example.studygether.Model.ChannelModel
import com.example.studygether.Model.Post
import com.example.studygether.Model.Comment
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    suspend fun createChannel(communityId: String, name: String, description: String, creatorId: String): Result<String>
    fun observeChannelsForCommunity(communityId: String): Flow<List<ChannelModel>>
    
    fun observeChannelMembers(channelId: String): Flow<List<String>>
    fun observeChannelModerators(channelId: String): Flow<List<String>>
    
    suspend fun joinChannel(channelId: String, userId: String): Result<Unit>
    suspend fun leaveChannel(channelId: String, userId: String): Result<Unit>
    
    suspend fun assignModerator(channelId: String, userId: String): Result<Unit>
    suspend fun removeModerator(channelId: String, userId: String): Result<Unit>
    
    suspend fun isMember(channelId: String, userId: String): Boolean
    suspend fun isModerator(channelId: String, userId: String): Boolean

    // Reddit-style post features
    suspend fun createPost(channelId: String, communityId: String, authorId: String, authorName: String, title: String, content: String): Result<String>
    fun observePosts(channelId: String): Flow<List<Post>>
    fun observePost(postId: String): Flow<Post?>
    suspend fun deletePost(channelId: String, postId: String): Result<Unit>
    
    // Likes / Dislikes
    suspend fun toggleLikePost(postId: String, userId: String): Result<Unit>
    suspend fun toggleDislikePost(postId: String, userId: String): Result<Unit>
    fun observeUserPostReaction(postId: String, userId: String): Flow<String?>

    // Commenting features
    suspend fun createComment(postId: String, authorId: String, authorName: String, content: String): Result<String>
    fun observeComments(postId: String): Flow<List<Comment>>
    suspend fun deleteComment(postId: String, commentId: String): Result<Unit>
}
