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
        // lastSelectedStore intentionally NOT cleared — needed for next login's auto-select
    }
}