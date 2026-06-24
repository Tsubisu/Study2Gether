package com.example.studygether.repository

import com.example.studygether.model.ProfileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileImplementation: ProfileRepo {
   private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("users")

    override fun getUserProfile(
        id: String,
        callback: (Boolean, String, ProfileModel?) -> Unit
    ) {
        ref.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val profile = snapshot.getValue(ProfileModel::class.java)
                    if (profile != null) {
                        callback(true, "successfull", profile)
                    } else {
                        callback(false, "Account doesnot exist", null)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })    }

    override fun updateUsername(
        id: String,
        newUsername: String,
        callback: (Boolean, String) -> Unit
    ) {
        val updateMap = mapOf<String, Any>(
            "username" to newUsername
        )
        ref.child(id).updateChildren(updateMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Username updated ")
            } else {
                callback(false, task.exception?.message ?: "Failed")
            }
        }
    }

    override fun getCurrentId(): String? {
        return auth.currentUser?.uid
    }

}