package com.example.studygether.Repository

import com.example.studygether.Model.Community
import com.example.studygether.Model.CommunityMember
import com.example.studygether.Repository.CommunityRepository
import com.example.studygether.Repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



class CommunityRepositoryImpl(
    private val db: FirebaseDatabase,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : CommunityRepository {

    private val communitiesRef = db.getReference("communities")
    private val usersRef = db.getReference("users")
    private val membersRef = db.getReference("communityMembers")
    private val userCommunitiesRef = db.getReference("userCommunities")

    override suspend fun registerAndCreateCommunity(
        community: Community,
        email: String,
        username: String,
        password: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val registerResult = userRepository.registerUser(email, username, password)
        val ownerId = registerResult.getOrElse { return@withContext Result.failure(it) }

        try {
            createCommunityInternal(community, ownerId)
        } catch (e: Exception) {
            try {
                auth.currentUser?.delete()?.await()
            } catch (deleteError: Exception) {
            }
            Result.failure(Exception("Failed to create community. Please try again. (${e.message})"))
        }
    }

    override suspend fun createCommunity(
        community: Community,
        ownerId: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            createCommunityInternal(community, ownerId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun createCommunityInternal(
        community: Community,
        ownerId: String
    ): Result<String> {
        val newRef = communitiesRef.push()
        val id = newRef.key ?: throw IllegalStateException("Failed to generate community id")
        val withId = community.copy(id = id, creatorId = ownerId)
        newRef.setValue(withId).await()

        addMember(id, ownerId)
        return Result.success(id)
    }

    override suspend fun getCommunity(id: String): Result<Community?> =
        withContext(Dispatchers.IO) {
            try {
                val snapshot = communitiesRef.child(id).get().await()
                Result.success(snapshot.getValue(Community::class.java))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun updateCommunityImage(
        id: String,
        url: String,
        publicId: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            communitiesRef.child(id).updateChildren(
                mapOf(
                    "profileImageUrl" to url,
                    "profileImagePublicId" to publicId
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addMemberByEmail(
        id: String,
        email: String,
        defaultAnonymous: Boolean
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val userQuery = usersRef.orderByChild("email").equalTo(email).get().await()
            val existingUser = userQuery.children.firstOrNull()
                ?.getValue(com.example.studygether.Model.User::class.java)
                ?: return@withContext Result.failure(
                    Exception("No registered user found with this email. Ask them to sign up first.")
                )

            addMember(id, existingUser.id, defaultAnonymous)
            Result.success(existingUser.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addMember(
        id: String,
        userId: String,
        defaultAnonymous: Boolean
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val member = CommunityMember(
                joinedAt = System.currentTimeMillis(),
                defaultAnonymous = defaultAnonymous
            )
            val updates = mapOf(
                "communityMembers/$id/$userId" to member,
                "userCommunities/$userId/$id" to true
            )
            db.reference.updateChildren(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeMember(
        id: String,
        userId: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val updates = mapOf(
                "communityMembers/$id/$userId" to null,
                "userCommunities/$userId/$id" to null
            )
            db.reference.updateChildren(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isMember(
        id: String,
        userId: String
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val snapshot = membersRef.child(id).child(userId).get().await()
            Result.success(snapshot.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserCommunities(userId: String): Result<List<String>> =
        withContext(Dispatchers.IO) {
            try {
                val snapshot = userCommunitiesRef.child(userId).get().await()
                Result.success(snapshot.children.mapNotNull { it.key })
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override fun observeCommunity(id: String): Flow<Community?> = callbackFlow {
        val ref = communitiesRef.child(id)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(Community::class.java))
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }
}