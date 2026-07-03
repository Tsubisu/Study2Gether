package com.example.studygether.Repository

import com.example.studygether.Model.Community
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface CommunityRepository{
    suspend fun registerAndCreateCommunity(
        community: Community,
        email: String,
        username: String,
        password: String
    ): Result<String>

    suspend fun createCommunity(community: Community, ownerId: String): Result<String>

    suspend fun getCommunity(id: String): Result<Community?>
    suspend fun updateCommunityImage(id: String, url: String, publicId: String): Result<Unit>
    suspend fun addMemberByEmail(
        id: String,
        email: String,
        defaultAnonymous: Boolean = false
    ): Result<String>

    suspend fun addMember(id: String, userId: String, defaultAnonymous: Boolean = false): Result<Unit>
    suspend fun removeMember(id: String, userId: String): Result<Unit>
    suspend fun isMember(id: String, userId: String): Result<Boolean>
    suspend fun getUserCommunities(userId: String): Result<List<Community>>
    fun observeCommunity(id: String): Flow<Community?>
    fun observeUserCommunityIds(uid: String): Flow<List<String>>
    fun startObservingUserCommunities(uid: String)
    fun stopObservingUserCommunities(uid: String)
    fun selectCommunity(uid: String, id: String?)
}