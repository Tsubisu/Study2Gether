package com.example.studygether.Repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
class ChannelRepositoryImpl
    (private val database: FirebaseDatabase): ChannelRepository{

    private val ref = database.getReference("channels")

    override fun joinChannel(
        channelId: String,
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(channelId).child("members").child(userId).setValue(true)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Successfully joined the channel")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }
    }
        override fun leaveChannel(
        channelId: String,
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(channelId).child("members").child(userId).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Successfully left the channel")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }
        }

    override fun assignModerator(
        channelId: String,
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(channelId).child("moderators").child(userId).setValue(true)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Moderator assigned successfully")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }

    }
    override fun removeModerator(
        channelId: String,
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(channelId).child("moderators").child(userId).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Moderator removed successfully")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }    }

}