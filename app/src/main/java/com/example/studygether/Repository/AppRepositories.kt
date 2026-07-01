package com.example.studygether.Repository


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object AppRepositories {
    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val imageRepository: ImageRepository = CloudinaryImageRepositoryImpl()
    val userRepository: UserRepository = UserRepositoryImpl(db, auth)
    val communityRepository: CommunityRepository = CommunityRepositoryImpl(
        db = db,
        auth = auth,
        userRepository = userRepository
    )
}