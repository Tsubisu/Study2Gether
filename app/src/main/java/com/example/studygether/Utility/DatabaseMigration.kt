package com.example.studygether.Utility

import android.util.Log
import com.example.studygether.Model.Community
import com.google.firebase.database.FirebaseDatabase

object DatabaseMigration {
    /**
     * Call this method once (e.g. inside MainActivity onCreate) to migrate existing community
     * members in Firebase to include their correct authority roles.
     * It compares user IDs to the community creatorId to assign "OWNER" or "MEMBER".
     */
    fun migrateCommunityMembersRole() {
        val db = FirebaseDatabase.getInstance()
        val communitiesRef = db.getReference("communities")
        val membersRef = db.getReference("communityMembers")

        communitiesRef.get().addOnSuccessListener { communitiesSnapshot ->
            if (!communitiesSnapshot.exists()) {
                Log.d("DatabaseMigration", "No communities found to migrate.")
                return@addOnSuccessListener
            }

            for (communitySnap in communitiesSnapshot.children) {
                val community = communitySnap.getValue(Community::class.java)
                if (community == null) continue

                val communityId = community.id
                val creatorId = community.creatorId

                if (communityId.isEmpty()) continue

                // Fetch members of this community
                membersRef.child(communityId).get().addOnSuccessListener { membersSnapshot ->
                    if (!membersSnapshot.exists()) return@addOnSuccessListener

                    val updates = mutableMapOf<String, Any>()
                    for (memberSnap in membersSnapshot.children) {
                        val userId = memberSnap.key ?: continue
                        val memberMap = memberSnap.value as? Map<*, *> ?: continue
                        
                        // Skip if role already exists
                        if (memberMap.containsKey("role")) continue

                        val role = if (userId == creatorId) "OWNER" else "MEMBER"
                        updates["/communityMembers/$communityId/$userId/role"] = role
                    }

                    if (updates.isNotEmpty()) {
                        db.reference.updateChildren(updates)
                            .addOnSuccessListener {
                                Log.d("DatabaseMigration", "Successfully updated roles for community: $communityId")
                            }
                            .addOnFailureListener { e ->
                                Log.e("DatabaseMigration", "Failed to update roles for community: $communityId", e)
                            }
                    }
                }
            }
        }.addOnFailureListener { e ->
            Log.e("DatabaseMigration", "Failed to fetch communities for migration", e)
        }
    }
}
