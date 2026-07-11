package com.example.studygether.Repository


import com.example.studygether.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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

class UserRepositoryImpl(
    private val db: FirebaseDatabase,
    private val auth: FirebaseAuth
) : UserRepository {

    private val usersRef = db.getReference("users")

    override suspend fun registerUser(
        email: String,
        username: String,
        password: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val newId = authResult.user?.uid
                ?: throw IllegalStateException("Failed to create auth account")

            val newUser = User(
                id = newId,
                email = email,
                username = username,
                createdAt = System.currentTimeMillis()
            )
            usersRef.child(newId).setValue(newUser).await()
            Result.success(newId)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("An account with this email already exists. Please log in instead."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(id: String): Result<User?> = withContext(Dispatchers.IO) {
        try {
            val snapshot = usersRef.child(id).get().await()
            Result.success(snapshot.getValue(User::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByEmail(email: String): Result<User?> = withContext(Dispatchers.IO) {
        try {
            val snapshot = usersRef.orderByChild("email").equalTo(email).get().await()
            Result.success(snapshot.children.firstOrNull()?.getValue(User::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfileImage(
        id: String,
        url: String,
        publicId: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            usersRef.child(id).updateChildren(
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

    override suspend fun updateUsername(id: String, username: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                usersRef.child(id).child("username").setValue(username).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override fun observeUser(id: String): Flow<User?> = callbackFlow {
        val ref = usersRef.child(id)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(User::class.java))
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }
}