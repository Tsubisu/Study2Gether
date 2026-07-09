@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.studygether.View.Screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.Model.Comment
import com.example.studygether.Model.Post
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ViewModels.PostDetailViewModel
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.tokens.AppSpacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PostDetailScreen(
    modifier: Modifier,
    postId: String,
    channelId: String,
    communityId: String,
    onGoBack: () -> Unit
) {
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    val viewModel: PostDetailViewModel = viewModel()
    val context = LocalContext.current

    val post by viewModel.post.collectAsStateWithLifecycle()
    val comments by viewModel.comments.collectAsStateWithLifecycle()
    val reaction by viewModel.userReaction.collectAsStateWithLifecycle()
    val isModerator by viewModel.isModerator.collectAsStateWithLifecycle()
    val currentUserId by viewModel.currentUserId.collectAsStateWithLifecycle()

    var commentText by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    val barBgColor = MaterialTheme.colorScheme.background
    LaunchedEffect(postId, channelId, communityId) {
        viewModel.initPost(postId, channelId, communityId)
        appBarsViewModel.setTitleBar(
            title = { Text("Post Thread", style = Typography.headlineMedium) },
            showBackButton = true,
            actions = {},
            barColor = barBgColor
        )
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    post?.let { p ->
                        item {
                            MainPostDetailCard(
                                post = p,
                                reaction = reaction,
                                onVoteUp = { viewModel.upvotePost() },
                                onVoteDown = { viewModel.downvotePost() }
                            )
                        }

                        item {
                            Text(
                                text = "Comments (${comments.size})",
                                style = Typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        if (comments.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No comments yet. Write one below!",
                                        style = Typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        } else {
                            items(comments) { comment ->
                                CommentItem(
                                    comment = comment,
                                    canDelete = isModerator || (currentUserId == comment.authorId),
                                    onDelete = {
                                        viewModel.deleteComment(comment.id) { res ->
                                            res.onFailure {
                                                Toast.makeText(context, it.message ?: "Failed to delete comment", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    } ?: item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                // Comment input box at bottom of container
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text("Add a comment...") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    IconButton(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                isSubmitting = true
                                viewModel.addComment(commentText.trim()) { res ->
                                    isSubmitting = false
                                    commentText = ""
                                    res.onFailure {
                                        Toast.makeText(context, it.message ?: "Failed to add comment", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        enabled = !isSubmitting && commentText.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainPostDetailCard(
    post: Post,
    reaction: String?,
    onVoteUp: () -> Unit,
    onVoteDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val date = remember(post.createdAt) {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(post.createdAt))
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Posted by u/${post.authorName} • $date",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = post.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))
            
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.9f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVoteUp, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Upvote",
                        tint = if (reaction == "LIKE") Color(0xFFFF5722) else MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = (post.likesCount - post.dislikesCount).toString(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (reaction) {
                        "LIKE" -> Color(0xFFFF5722)
                        "DISLIKE" -> Color(0xFF3F51B5)
                        else -> MaterialTheme.colorScheme.onSecondary
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                IconButton(onClick = onVoteDown, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Downvote",
                        tint = if (reaction == "DISLIKE") Color(0xFF3F51B5) else MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    canDelete: Boolean,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val date = remember(comment.createdAt) {
        val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        sdf.format(Date(comment.createdAt))
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "u/${comment.authorName} • $date",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                )

                if (canDelete) {
                    IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Comment",
                            tint = Color.Red.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.content,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.9f)
            )
        }
    }
}
