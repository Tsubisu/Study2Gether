package com.example.studygether.data.repository

import com.example.studygether.data.model.User
import com.google.firebase.auth.FirebaseAuth

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    override fun loginWithEmail(
        email: String,
        password: String,
        onResult: (Result<User>) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val user = User(
                            uid = firebaseUser.uid,
                            email = firebaseUser.email ?: "",
                            displayName = firebaseUser.displayName ?: ""
                        )
                        onResult(Result.success(user))
                    } else {
                        onResult(Result.failure(Exception("User data is empty.")))
                    }
                } else {
                    onResult(Result.failure(task.exception ?: Exception("Unknown authentication error.")))
                }
            }
    }

    override fun resetPassword(
        email: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(Result.success(Unit))
                } else {
                    onResult(Result.failure(task.exception ?: Exception("Failed to send reset email.")))
                }
            }
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: ""
        )
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
