package com.example.studygether.Repository

import android.content.Context
import com.example.studygether.App.LastSelectedCommunityStore
import com.example.studygether.App.SessionState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object AppRepositories {

    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // owned here, lives for the app process, passed into communityRepository below
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val imageRepository: ImageRepository = CloudinaryImageRepositoryImpl()
    val userRepository: UserRepository = UserRepositoryImpl(db, auth)
    val authenticationRepository: AuthenticationRepository = AuthRepoImpl(auth, db)

    lateinit var communityRepository: CommunityRepository
        private set

    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return

        communityRepository = CommunityRepositoryImpl(
            db = db,
            auth = auth,
            userRepository = userRepository,
            lastSelectedStore = LastSelectedCommunityStore(context.applicationContext),
            repositoryScope = repositoryScope
        )

        SessionState.init(communityRepository,userRepository)

        isInitialized = true
    }
}