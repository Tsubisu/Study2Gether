package com.example.studygether.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygether.App.SessionState
import com.example.studygether.Model.Post
import com.example.studygether.Repository.AppRepositories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class PostSortOrder {
    NEWEST,
    MOST_POPULAR
}

class ChannelDetailViewModel : ViewModel() {

    private val channelRepository = AppRepositories.channelRepository
    private val communityRepository = AppRepositories.communityRepository

    private val _channelId = MutableStateFlow<String?>(null)
    private val _communityId = MutableStateFlow<String?>(null)

    private val _sortOrder = MutableStateFlow(PostSortOrder.NEWEST)
    val sortOrder: StateFlow<PostSortOrder> = _sortOrder.asStateFlow()

    fun setSortOrder(order: PostSortOrder) {
        _sortOrder.value = order
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: StateFlow<List<Post>> = combine(_channelId, _sortOrder) { id, sortOrder ->
        Pair(id, sortOrder)
    }.flatMapLatest { (id, sortOrder) ->
        if (id != null) {
            channelRepository.observePosts(id).map { list ->
                when (sortOrder) {
                    PostSortOrder.NEWEST -> list.sortedByDescending { it.createdAt }
                    PostSortOrder.MOST_POPULAR -> list.sortedByDescending { it.likesCount - it.dislikesCount }
                }
            }
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val isMember: StateFlow<Boolean> = combine(_channelId, SessionState.currentUser) { id, user ->
        if (id != null && user != null) {
            channelRepository.observeChannelMembers(id).map { it.contains(user.id) }
        } else {
            flowOf(false)
        }
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isModerator: StateFlow<Boolean> = combine(_channelId, _communityId, SessionState.currentUser) { chId, commId, user ->
        if (user != null) {
            val isModFlow = if (chId != null) {
                channelRepository.observeChannelModerators(chId).map { it.contains(user.id) }
            } else {
                flowOf(false)
            }
            val isOwnerFlow = if (commId != null) {
                flow {
                    emit(communityRepository.isCommunityOwner(commId, user.id).getOrDefault(false))
                }
            } else {
                flowOf(false)
            }
            combine(isModFlow, isOwnerFlow) { isMod, isOwner -> isMod || isOwner }
        } else {
            flowOf(false)
        }
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val currentUserId = SessionState.currentUser.map { it?.id }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun initChannel(channelId: String, communityId: String) {
        _channelId.value = channelId
        _communityId.value = communityId
    }

    fun joinChannel(onResult: (Result<Unit>) -> Unit) {
        val chId = _channelId.value ?: return
        val user = SessionState.currentUser.value ?: return
        viewModelScope.launch {
            val res = channelRepository.joinChannel(chId, user.id)
            onResult(res)
        }
    }

    fun leaveChannel(onResult: (Result<Unit>) -> Unit) {
        val chId = _channelId.value ?: return
        val user = SessionState.currentUser.value ?: return
        viewModelScope.launch {
            val res = channelRepository.leaveChannel(chId, user.id)
            onResult(res)
        }
    }

    fun createPost(title: String, content: String, onResult: (Result<String>) -> Unit) {
        val chId = _channelId.value ?: return
        val commId = _communityId.value ?: return
        val user = SessionState.currentUser.value ?: return
        
        viewModelScope.launch {
            if (!isMember.value) {
                onResult(Result.failure(Exception("You must join this channel to create a post.")))
                return@launch
            }
            val res = channelRepository.createPost(chId, commId, user.id, user.username, title, content)
            onResult(res)
        }
    }

    fun upvotePost(postId: String) {
        val user = SessionState.currentUser.value ?: return
        viewModelScope.launch {
            channelRepository.toggleLikePost(postId, user.id)
        }
    }

    fun downvotePost(postId: String) {
        val user = SessionState.currentUser.value ?: return
        viewModelScope.launch {
            channelRepository.toggleDislikePost(postId, user.id)
        }
    }

    fun deletePost(postId: String, onResult: (Result<Unit>) -> Unit) {
        val chId = _channelId.value ?: return
        viewModelScope.launch {
            val res = channelRepository.deletePost(chId, postId)
            onResult(res)
        }
    }
}
