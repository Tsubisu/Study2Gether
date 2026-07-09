package com.example.studygether.Repository

import com.example.studygether.Model.ChannelModel
import com.example.studygether.Model.Post
import com.example.studygether.Model.Comment
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

class ChannelRepositoryImpl(
    private val db: FirebaseDatabase
) : ChannelRepository {

    private val channelsRef = db.getReference("channels")
    private val postsRef = db.getReference("posts")
    private val commentsRef = db.getReference("comments")
    private val reactionsRef = db.getReference("reactions")
    private val communityChannelsRef = db.getReference("communityChannels")

    override suspend fun createChannel(
        communityId: String,
        name: String,
        description: String,
        creatorId: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val newRef = channelsRef.push()
            val channelId = newRef.key ?: throw IllegalStateException("Failed to generate channel ID")
            
            val channel = mapOf(
                "id" to channelId,
                "name" to name,
                "description" to description,
                "communityId" to communityId,
                "creatorId" to creatorId,
                "createdAt" to System.currentTimeMillis()
            )
            
            val updates = mapOf(
                "channels/$channelId" to channel,
                "communityChannels/$communityId/$channelId" to true
            )
            
            db.reference.updateChildren(updates).await()
            Result.success(channelId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeChannelsForCommunity(communityId: String): Flow<List<ChannelModel>> = callbackFlow {
        val query = communityChannelsRef.child(communityId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val channelIds = snapshot.children.mapNotNull { it.key }
                if (channelIds.isEmpty()) {
                    trySend(emptyList())
                    return
                }
                
                channelsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(detailsSnapshot: DataSnapshot) {
                        val list = channelIds.mapNotNull { id ->
                            val snap = detailsSnapshot.child(id)
                            if (snap.exists()) {
                                val name = snap.child("name").getValue(String::class.java) ?: ""
                                val description = snap.child("description").getValue(String::class.java) ?: ""
                                val idVal = snap.child("id").getValue(String::class.java) ?: id
                                
                                val memberIds = snap.child("members").children.mapNotNull { it.key }
                                val modIds = snap.child("moderators").children.mapNotNull { it.key }
                                
                                ChannelModel(
                                    id = idVal,
                                    name = name,
                                    description = description,
                                    memberId = memberIds,
                                    moderators = modIds
                                )
                            } else null
                        }
                        trySend(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        close(error.toException())
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    override fun observeChannelMembers(channelId: String): Flow<List<String>> = callbackFlow {
        val ref = channelsRef.child(channelId).child("members")
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

    override fun observeChannelModerators(channelId: String): Flow<List<String>> = callbackFlow {
        val ref = channelsRef.child(channelId).child("moderators")
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

    override suspend fun joinChannel(channelId: String, userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            channelsRef.child(channelId).child("members").child(userId).setValue(true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun leaveChannel(channelId: String, userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            channelsRef.child(channelId).child("members").child(userId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun assignModerator(channelId: String, userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            channelsRef.child(channelId).child("moderators").child(userId).setValue(true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeModerator(channelId: String, userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            channelsRef.child(channelId).child("moderators").child(userId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isMember(channelId: String, userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val snapshot = channelsRef.child(channelId).child("members").child(userId).get().await()
            snapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isModerator(channelId: String, userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val snapshot = channelsRef.child(channelId).child("moderators").child(userId).get().await()
            snapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun createPost(
        channelId: String,
        communityId: String,
        authorId: String,
        authorName: String,
        title: String,
        content: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val newRef = postsRef.push()
            val postId = newRef.key ?: throw IllegalStateException("Failed to generate post ID")
            val post = Post(
                id = postId,
                channelId = channelId,
                communityId = communityId,
                authorId = authorId,
                authorName = authorName,
                title = title,
                content = content,
                createdAt = System.currentTimeMillis()
            )
            newRef.setValue(post).await()
            Result.success(postId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observePosts(channelId: String): Flow<List<Post>> = callbackFlow {
        val query = postsRef.orderByChild("channelId").equalTo(channelId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(Post::class.java) }
                    .sortedByDescending { it.createdAt }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    override fun observePost(postId: String): Flow<Post?> = callbackFlow {
        val ref = postsRef.child(postId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(Post::class.java))
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun deletePost(channelId: String, postId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            postsRef.child(postId).removeValue().await()
            commentsRef.child(postId).removeValue().await()
            reactionsRef.child(postId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleLikePost(postId: String, userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val reactionRef = reactionsRef.child(postId).child(userId)
            val current = reactionRef.get().await().getValue(String::class.java)
            val postRef = postsRef.child(postId)
            
            if (current == "LIKE") {
                reactionRef.removeValue().await()
                postRef.child("likesCount").runTransaction(DecrementTransactionHandler())
            } else if (current == "DISLIKE") {
                reactionRef.setValue("LIKE").await()
                postRef.child("likesCount").runTransaction(IncrementTransactionHandler())
                postRef.child("dislikesCount").runTransaction(DecrementTransactionHandler())
            } else {
                reactionRef.setValue("LIKE").await()
                postRef.child("likesCount").runTransaction(IncrementTransactionHandler())
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleDislikePost(postId: String, userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val reactionRef = reactionsRef.child(postId).child(userId)
            val current = reactionRef.get().await().getValue(String::class.java)
            val postRef = postsRef.child(postId)
            
            if (current == "DISLIKE") {
                reactionRef.removeValue().await()
                postRef.child("dislikesCount").runTransaction(DecrementTransactionHandler())
            } else if (current == "LIKE") {
                reactionRef.setValue("DISLIKE").await()
                postRef.child("dislikesCount").runTransaction(IncrementTransactionHandler())
                postRef.child("likesCount").runTransaction(DecrementTransactionHandler())
            } else {
                reactionRef.setValue("DISLIKE").await()
                postRef.child("dislikesCount").runTransaction(IncrementTransactionHandler())
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeUserPostReaction(postId: String, userId: String): Flow<String?> = callbackFlow {
        val ref = reactionsRef.child(postId).child(userId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(String::class.java))
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun createComment(postId: String, authorId: String, authorName: String, content: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val newRef = commentsRef.child(postId).push()
            val commentId = newRef.key ?: throw IllegalStateException("Failed to generate comment ID")
            val comment = Comment(
                id = commentId,
                postId = postId,
                authorId = authorId,
                authorName = authorName,
                content = content,
                createdAt = System.currentTimeMillis()
            )
            newRef.setValue(comment).await()
            Result.success(commentId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeComments(postId: String): Flow<List<Comment>> = callbackFlow {
        val ref = commentsRef.child(postId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(Comment::class.java) }
                    .sortedBy { it.createdAt }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun deleteComment(postId: String, commentId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            commentsRef.child(postId).child(commentId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private class IncrementTransactionHandler : com.google.firebase.database.Transaction.Handler {
        override fun doTransaction(currentData: com.google.firebase.database.MutableData): com.google.firebase.database.Transaction.Result {
            val current = currentData.getValue(Int::class.java) ?: 0
            currentData.value = current + 1
            return com.google.firebase.database.Transaction.success(currentData)
        }
        override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {}
    }

    private class DecrementTransactionHandler : com.google.firebase.database.Transaction.Handler {
        override fun doTransaction(currentData: com.google.firebase.database.MutableData): com.google.firebase.database.Transaction.Result {
            val current = currentData.getValue(Int::class.java) ?: 0
            currentData.value = maxOf(0, current - 1)
            return com.google.firebase.database.Transaction.success(currentData)
        }
        override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {}
    }
}