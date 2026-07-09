package com.example.studygether.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygether.App.SessionState
import com.example.studygether.Model.Comment
import com.example.studygether.Model.Post
import com.example.studygether.Repository.AppRepositories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PostDetailViewModel : ViewModel() {

    private val channelRepository = AppRepositories.channelRepository
    private val communityRepository = AppRepositories.communityRepository

    private val _postId = MutableStateFlow<String?>(null)
    private val _channelId = MutableStateFlow<String?>(null)
    private val _communityId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val post: StateFlow<Post?> = _postId
        .flatMapLatest { id ->
            if (id != null) channelRepository.observePost(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val comments: StateFlow<List<Comment>> = _postId
        .flatMapLatest { id ->
            if (id != null) channelRepository.observeComments(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val userReaction: StateFlow<String?> = combine(_postId, SessionState.currentUser) { pId, user ->
        if (pId != null && user != null) {
            channelRepository.observeUserPostReaction(pId, user.id)
        } else {
            flowOf(null)
        }
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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

    fun initPost(postId: String, channelId: String, communityId: String) {
        _postId.value = postId
        _channelId.value = channelId
        _communityId.value = communityId
    }

    fun upvotePost() {
        val pId = _postId.value ?: return
        val user = SessionState.currentUser.value ?: return
        viewModelScope.launch {
            channelRepository.toggleLikePost(pId, user.id)
        }
    }

    fun downvotePost() {
        val pId = _postId.value ?: return
        val user = SessionState.currentUser.value ?: return
        viewModelScope.launch {
            channelRepository.toggleDislikePost(pId, user.id)
        }
    }

    fun addComment(content: String, onResult: (Result<String>) -> Unit) {
        val pId = _postId.value ?: return
        val user = SessionState.currentUser.value ?: return
        if (content.isBlank()) return

        viewModelScope.launch {
            val res = channelRepository.createComment(pId, user.id, user.username, content.trim())
            onResult(res)
        }
    }

    fun deleteComment(commentId: String, onResult: (Result<Unit>) -> Unit) {
        val pId = _postId.value ?: return
        viewModelScope.launch {
            val res = channelRepository.deleteComment(pId, commentId)
            onResult(res)
        }
    }
}
