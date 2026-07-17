package com.example.studygether.Repository

import com.example.studygether.App.LastSelectedCommunityStore
import com.example.studygether.App.UserCommunity
import com.example.studygether.Model.Community
import com.example.studygether.Model.CommunityMember
import com.example.studygether.Repository.CommunityRepository
import com.example.studygether.Repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



class CommunityRepositoryImpl(
    private val db: FirebaseDatabase,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val repositoryScope: CoroutineScope,
    private val lastSelectedStore: LastSelectedCommunityStore
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

        addMember(id, ownerId, false, "OWNER")
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


    override suspend fun addMember(
        id: String,
        userId: String,
        defaultAnonymous: Boolean,
        role: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val member = CommunityMember(
                joinedAt = System.currentTimeMillis(),
                defaultAnonymous = defaultAnonymous,
                role = role
            )
            val updates = mapOf(
                "communityMembers/$id/$userId" to member,
                "userCommunities/$userId/$id" to true,
                "communities/$id/memberCount" to ServerValue.increment(1)
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
        val currentUid = auth.currentUser?.uid ?: return@withContext Result.failure(Exception("Not authenticated"))
        
        val isSelf = currentUid == userId
        val isOwner = isCommunityOwner(id, currentUid).getOrElse { false }
        
        if (!isOwner && !isSelf) {
            return@withContext Result.failure(Exception("Only the community owner can remove users from this community."))
        }

        try {
            val updates = mapOf(
                "communityMembers/$id/$userId" to null,
                "userCommunities/$userId/$id" to null,
                "communities/$id/memberCount" to ServerValue.increment(-1)
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

    override suspend fun getUserCommunities(userId: String): Result<List<Community>> =
        withContext(Dispatchers.IO) {
            try {
                val communityIdsSnapshot = userCommunitiesRef.child(userId).get().await()
                val communityIds = communityIdsSnapshot.children.mapNotNull { it.key }

                val communities = communityIds.mapNotNull { communityId ->
                    communitiesRef.child(communityId)
                        .get()
                        .await()
                        .getValue(Community::class.java)
                }

                Result.success(communities)
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

    override fun observeUserCommunityIds(uid: String): Flow<List<String>> = callbackFlow {
        val ref = userCommunitiesRef.child(uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.key })
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override fun observeCommunityMembers(communityId: String): Flow<List<String>> = callbackFlow {
        val ref = membersRef.child(communityId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.key })
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }


    private var userCommunitiesJob: Job? = null
    private var hasAutoSelected = false
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun startObservingUserCommunities(uid: String) {
        userCommunitiesJob?.cancel()
        hasAutoSelected = false

        userCommunitiesJob = observeUserCommunityIds(uid)
            .flatMapLatest { ids ->
                if (ids.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(ids.map { id -> observeCommunity(id) }) { array -> array.toList() }
                }
            }
            .onEach { communities ->
                UserCommunity.update(communities)
                maybeAutoSelect(uid, communities)
            }
            .launchIn(repositoryScope)
    }
    override fun selectCommunity(uid: String, id: String?) {
        UserCommunity.selectCommunity(id)
        lastSelectedStore.save(uid, id)
    }

    private fun maybeAutoSelect(uid: String, communities: List<Community?>) {
        if (hasAutoSelected) return
        if (communities.isEmpty()) return

        val savedId = lastSelectedStore.get(uid)
        val idsInList = communities.mapNotNull { it?.id }

        val idToSelect = if (savedId != null && idsInList.contains(savedId)) {
            savedId
        } else {
            idsInList.firstOrNull()
        }

        UserCommunity.selectCommunity(idToSelect)
        hasAutoSelected = true
    }



    override fun stopObservingUserCommunities(uid: String) {
        userCommunitiesJob?.cancel()
        userCommunitiesJob = null
        hasAutoSelected = false
        UserCommunity.reset()
        
    }

    override suspend fun getMemberRole(communityId: String, userId: String): Result<String?> =
        withContext(Dispatchers.IO) {
            try {
                val snapshot = membersRef.child(communityId).child(userId).get().await()
                val member = snapshot.getValue(CommunityMember::class.java)
                Result.success(member?.role)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun isCommunityOwner(
        communityId: String,
        userId: String
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val communitySnap = communitiesRef.child(communityId).get().await()
            val community = communitySnap.getValue(Community::class.java)
            if (community != null && community.creatorId == userId) {
                return@withContext Result.success(true)
            }
            val snapshot = membersRef.child(communityId).child(userId).get().await()
            val member = snapshot.getValue(CommunityMember::class.java)
            Result.success(member?.role == "OWNER")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun assignChannelManager(
        communityId: String,
        channelId: String,
        userId: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val currentUid = auth.currentUser?.uid ?: return@withContext Result.failure(Exception("Not authenticated"))
        val isOwner = isCommunityOwner(communityId, currentUid).getOrElse { false }
        if (!isOwner) {
            return@withContext Result.failure(Exception("Only the community owner can assign channel managers."))
        }
        try {
            db.getReference("communityChannelManagers").child(communityId).child(channelId).child(userId).setValue(true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeChannelManager(
        communityId: String,
        channelId: String,
        userId: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val currentUid = auth.currentUser?.uid ?: return@withContext Result.failure(Exception("Not authenticated"))
        val isOwner = isCommunityOwner(communityId, currentUid).getOrElse { false }
        if (!isOwner) {
            return@withContext Result.failure(Exception("Only the community owner can remove channel managers."))
        }
        try {
            db.getReference("communityChannelManagers").child(communityId).child(channelId).child(userId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isChannelManager(
        communityId: String,
        channelId: String,
        userId: String
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val snapshot = db.getReference("communityChannelManagers").child(communityId).child(channelId).child(userId).get().await()
            Result.success(snapshot.exists() && snapshot.value == true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}