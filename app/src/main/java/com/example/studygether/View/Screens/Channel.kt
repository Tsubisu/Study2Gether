@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.studygether.View.Screens

import android.widget.Toast
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.Model.Post
import com.example.studygether.R
import com.example.studygether.Repository.AppRepositories
import com.example.studygether.App.SessionState
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.View.AppBars.ChannelTitleCard
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ViewModels.ChannelDetailViewModel
import com.example.studygether.ViewModels.PostSortOrder
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.tokens.AppSpacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChannelScreen(
    modifier: Modifier,
    channelId: String,
    channelName: String,
    communityId: String,
    onNavigateToPostDetail: (postId: String, channelId: String, communityId: String) -> Unit
) {
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    val viewModel: ChannelDetailViewModel = viewModel()
    val context = LocalContext.current

    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val isMember by viewModel.isMember.collectAsStateWithLifecycle()
    val isModerator by viewModel.isModerator.collectAsStateWithLifecycle()
    val currentUserId by viewModel.currentUserId.collectAsStateWithLifecycle()

    // Observe channel details for description and metadata
    val channelsFlow = remember(channelId) {
        AppRepositories.channelRepository.observeChannelsForCommunity(communityId)
    }.collectAsState(initial = emptyList())
    val channelDetail = channelsFlow.value.firstOrNull { it.id == channelId }
    val channelDescription = channelDetail?.description ?: "No description provided."

    val membersFlow = remember(channelId) {
        AppRepositories.channelRepository.observeChannelMembers(channelId)
    }.collectAsState(initial = emptyList())
    val memberCount = membersFlow.value.size

    var showCreatePostDialog by remember { mutableStateOf(false) }
    var postTitle by remember { mutableStateOf("") }
    var postContent by remember { mutableStateOf("") }
    var isPosting by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val channelLogoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                AppRepositories.imageRepository.uploadImage(context, uri) { resultData ->
                    if (resultData != null) {
                        scope.launch {
                            AppRepositories.channelRepository.updateChannelImage(channelId, resultData.url)
                        }
                    }
                }
            }
        }
    }

    val barBgColor = MaterialTheme.colorScheme.background
    LaunchedEffect(channelId, channelName, memberCount, channelDetail?.imageUrl, isModerator) {
        viewModel.initChannel(channelId, communityId)
        appBarsViewModel.setTitleBar(
            title = {
                ChannelTitleCard(
                    channelName = channelName,
                    imageUrl = channelDetail?.imageUrl,
                    channelMemberCount = memberCount,
                    onLogoClick = if (isModerator) { {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        channelLogoLauncher.launch(intent)
                    } } else null
                )
            },
            showBackButton = true,
            actions = {
                Row {
                    // Quick stats or menu action if needed
                }
            },
            barColor = barBgColor
        )
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    // Header Item: Channel details & Join/Leave
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "About Channel",
                                    style = Typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = channelDescription,
                                    style = Typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Button(
                                    onClick = {
                                        if (isMember) {
                                            viewModel.leaveChannel { res ->
                                                res.onFailure {
                                                    Toast.makeText(context, it.message ?: "Failed to leave channel", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        } else {
                                            viewModel.joinChannel { res ->
                                                res.onFailure {
                                                    Toast.makeText(context, it.message ?: "Failed to join channel", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isMember) Color.LightGray else MaterialTheme.colorScheme.primary,
                                        contentColor = if (isMember) Color.Black else Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(if (isMember) "Leave Channel" else "Join Channel")
                                }
                            }
                        }
                    }

                    item {
                        val currentSortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sort by:",
                                style = Typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold
                            )
                            
                            FilterChip(
                                selected = currentSortOrder == PostSortOrder.NEWEST,
                                onClick = { viewModel.setSortOrder(PostSortOrder.NEWEST) },
                                label = { Text("Newest") }
                            )
                            
                            FilterChip(
                                selected = currentSortOrder == PostSortOrder.MOST_POPULAR,
                                onClick = { viewModel.setSortOrder(PostSortOrder.MOST_POPULAR) },
                                label = { Text("Most Popular") }
                            )
                        }
                    }

                    // Posts Timeline items
                    if (posts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No posts yet. Be the first to create one!",
                                    style = Typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                        }
                    } else {
                        items(posts) { post ->
                            PostCard(
                                post = post,
                                isModerator = isModerator || (currentUserId == post.authorId),
                                onVoteUp = { viewModel.upvotePost(post.id) },
                                onVoteDown = { viewModel.downvotePost(post.id) },
                                onDelete = {
                                    viewModel.deletePost(post.id) { res ->
                                        res.onFailure {
                                            Toast.makeText(context, it.message ?: "Failed to delete post", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onClick = { onNavigateToPostDetail(post.id, channelId, communityId) }
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button to create a post (visible if member)
        if (isMember) {
            FloatingActionButton(
                onClick = {
                    postTitle = ""
                    postContent = ""
                    showCreatePostDialog = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Post")
            }
        }

        if (showCreatePostDialog) {
            AlertDialog(
                onDismissRequest = { if (!isPosting) showCreatePostDialog = false },
                title = { Text("Create Post") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = postTitle,
                            onValueChange = { postTitle = it },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !isPosting
                        )
                        OutlinedTextField(
                            value = postContent,
                            onValueChange = { postContent = it },
                            label = { Text("Content") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            enabled = !isPosting
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (postTitle.isBlank() || postContent.isBlank()) return@Button
                            isPosting = true
                            viewModel.createPost(postTitle.trim(), postContent.trim()) { res ->
                                isPosting = false
                                showCreatePostDialog = false
                                res.onFailure {
                                    Toast.makeText(context, it.message ?: "Failed to post", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        enabled = !isPosting && postTitle.isNotBlank() && postContent.isNotBlank()
                    ) {
                        if (isPosting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Post")
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showCreatePostDialog = false },
                        enabled = !isPosting
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    isModerator: Boolean,
    onVoteUp: () -> Unit,
    onVoteDown: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val date = remember(post.createdAt) {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(post.createdAt))
    }

    // Observe comments count reactively
    val commentsFlow = remember(post.id) {
        AppRepositories.channelRepository.observeComments(post.id)
    }.collectAsState(initial = emptyList())
    val commentsCount = commentsFlow.value.size

    // Observe current reaction of the user
    val currentUserId = SessionState.currentUser.collectAsState().value?.id
    val reaction = if (currentUserId != null) {
        remember(post.id, currentUserId) {
            AppRepositories.channelRepository.observeUserPostReaction(post.id, currentUserId)
        }.collectAsState(initial = null).value
    } else null

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Post metadata (Author and Date)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Posted by u/${post.authorName} • $date",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f)
                )

                if (isModerator) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Post",
                            tint = Color.Red.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            // Post Title
            Text(
                text = post.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            
            // Post Content
            Text(
                text = post.content,
                fontSize = 13.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Interactive Footer (Voting and comments count)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Vote Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(onClick = onVoteUp, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Upvote",
                            tint = if (reaction == "LIKE") Color(0xFFFF5722) else MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = (post.likesCount - post.dislikesCount).toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (reaction) {
                            "LIKE" -> Color(0xFFFF5722)
                            "DISLIKE" -> Color(0xFF3F51B5)
                            else -> MaterialTheme.colorScheme.onSecondary
                        }
                    )
                    IconButton(onClick = onVoteDown, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "Downvote",
                            tint = if (reaction == "DISLIKE") Color(0xFF3F51B5) else MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Comments count
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "$commentsCount comments",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
