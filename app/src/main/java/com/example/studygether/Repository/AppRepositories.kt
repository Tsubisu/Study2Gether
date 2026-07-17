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

    private val db by lazy { FirebaseDatabase.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var _imageRepository: ImageRepository? = null
    var imageRepository: ImageRepository
        get() {
            if (_imageRepository == null) {
                _imageRepository = CloudinaryImageRepositoryImpl()
            }
            return _imageRepository!!
        }
        set(value) {
            _imageRepository = value
        }

    private var _userRepository: UserRepository? = null
    var userRepository: UserRepository
        get() {
            if (_userRepository == null) {
                _userRepository = UserRepositoryImpl(db, auth)
            }
            return _userRepository!!
        }
        set(value) {
            _userRepository = value
        }

    private var _authenticationRepository: AuthenticationRepository? = null
    var authenticationRepository: AuthenticationRepository
        get() {
            if (_authenticationRepository == null) {
                _authenticationRepository = AuthRepoImpl(auth, db)
            }
            return _authenticationRepository!!
        }
        set(value) {
            _authenticationRepository = value
        }

    private var _channelRepository: ChannelRepository? = null
    var channelRepository: ChannelRepository
        get() {
            if (_channelRepository == null) {
                _channelRepository = ChannelRepositoryImpl(db)
            }
            return _channelRepository!!
        }
        set(value) {
            _channelRepository = value
        }

    lateinit var communityRepository: CommunityRepository

    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return

        
        try {
            com.cloudinary.android.MediaManager.get()
        } catch (e: IllegalStateException) {
            val config = mapOf(
                "cloud_name" to com.example.studygether.BuildConfig.CLOUDINARY_CLOUD_NAME,
                "secure" to true
            )
            com.cloudinary.android.MediaManager.init(context.applicationContext, config)
        } catch (e: Exception) {
            android.util.Log.e("AppRepositories", "Failed to initialize Cloudinary MediaManager", e)
        }

        communityRepository = CommunityRepositoryImpl(
            db = db,
            auth = auth,
            userRepository = userRepository,
            lastSelectedStore = LastSelectedCommunityStore(context.applicationContext),
            repositoryScope = repositoryScope
        )

        SessionState.init(communityRepository,userRepository)
        com.example.studygether.Utility.ZegoService.init(context.applicationContext)

        isInitialized = true
    }
}