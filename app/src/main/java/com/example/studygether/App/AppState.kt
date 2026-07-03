package com.example.studygether.App

import com.example.studygether.Model.User
import com.example.studygether.Repository.CommunityRepository
import com.example.studygether.Repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

object SessionState {
    private val sessionScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()


    private val _isHydrating = MutableStateFlow(true)
    val isHydrating: StateFlow<Boolean> = _isHydrating.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = _currentUser
        .map { it != null }
        .stateIn(
            scope = sessionScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    private lateinit var communityRepository: CommunityRepository
    private lateinit var userRepository: UserRepository

    fun init(communityRepository: CommunityRepository, userRepository: UserRepository) {
        this.communityRepository = communityRepository
        this.userRepository = userRepository
        hydrateFromCachedSession()
    }

    private fun hydrateFromCachedSession() {
        val firebaseUser = Firebase.auth.currentUser
        if (firebaseUser == null) {
            _isHydrating.value = false
            return
        }
        sessionScope.launch {
            userRepository.getUser(firebaseUser.uid).fold(
                onSuccess = { user ->
                    if (user != null) {
                        setUser(user)
                    } else {
                        _currentUser.value = null
                    }
                },
                onFailure = {
                    Firebase.auth.signOut()
                    _currentUser.value = null

                }
            )
            _isHydrating.value = false

        }
    }

    fun setUser(user: User) {
        _currentUser.update { user }
        communityRepository.startObservingUserCommunities(user.id)
    }

    fun clear() {
        val uid = _currentUser.value?.id
        _currentUser.update { null }
        if (uid != null) {
            communityRepository.stopObservingUserCommunities(uid)
        }
        Firebase.auth.signOut()
    }
}