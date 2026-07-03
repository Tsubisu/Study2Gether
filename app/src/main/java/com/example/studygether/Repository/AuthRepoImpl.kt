package com.example.studygether.Repository

import android.util.Log
import com.example.studygether.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseDatabase
    ): AuthenticationRepository
{
    override suspend fun loginAndFetchUser(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            Log.d("LoginPressed from Repo","LoginPressed from Repo")
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user ?: return@withContext Result.failure(
                    IllegalStateException("Login succeeded but user is null")
                )

                val snapshot = db.getReference("users")
                    .child(firebaseUser.uid)
                    .get()
                    .await()

                val user = snapshot.getValue(User::class.java) ?: return@withContext Result.failure(
                    IllegalStateException("User profile not found")
                )

                Result.success(user)
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                Result.failure(Exception("Incorrect Email or Password"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                Result.success(Unit)
            } catch (e: FirebaseAuthInvalidUserException) {
                Result.failure(Exception("No account found with this email"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

}