package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.Model.User
import com.example.studygether.R
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.ConvoListViewModel
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.tokens.AppSpacing
import im.zego.zim.entity.ZIMConversation
import im.zego.zim.entity.ZIMTextMessage

@Composable
fun ConvoListScreen(
    modifier: Modifier,
    onNavigateToChat: (name: String, image: Int, targetUserId: String) -> Unit
) {
    val activity = LocalActivity.current as ComponentActivity
    val mainViewModel: AppBarsViewModel = viewModel(activity)
    
    val listViewModel: ConvoListViewModel = viewModel()
    val activeConversations by listViewModel.activeConversations.collectAsStateWithLifecycle()
    val communityMembers by listViewModel.communityMembers.collectAsStateWithLifecycle()
    val blockedUserIds by listViewModel.blockedUserIds.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        mainViewModel.setTitleBar(
            title = { Text("Direct Messages", style = Typography.headlineMedium) },
            actions = {}
        )
        mainViewModel.setBottomBarType(BottomBarState(BottomBars.NavBar))
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty()) {
            listViewModel.refreshActiveChats()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(all = AppSpacing.small)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Messenger Style Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    placeholder = { Text("Search members to chat...", style = Typography.bodyMedium) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (searchQuery.isEmpty()) {
                    // Active Conversations
                    if (activeConversations.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No active conversations yet.\nType a member's name in the search bar above to start a chat!",
                                textAlign = TextAlign.Center,
                                style = Typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(AppSpacing.medium)
                        ) {
                            items(activeConversations) { convo ->
                                val lastMsgText = when (val last = convo.lastMessage) {
                                    is ZIMTextMessage -> last.message
                                    else -> "No messages yet"
                                }
                                val matchingMember = communityMembers.firstOrNull { it.id == convo.conversationID }
                                val resolvedName = matchingMember?.username ?: convo.conversationName.ifEmpty { convo.conversationID }
                                val resolvedImageUrl = matchingMember?.profileImageUrl
                                ActiveChatCard(
                                    conversation = convo,
                                    resolvedName = resolvedName,
                                    resolvedImageUrl = resolvedImageUrl,
                                    lastMessageText = lastMsgText,
                                    onClick = { onNavigateToChat(resolvedName, R.drawable.avatar, convo.conversationID) }
                                )
                            }
                        }
                    }
                } else {
                    // Filtered Community Members
                    val filteredMembers = communityMembers.filter {
                        it.username.contains(searchQuery, ignoreCase = true) ||
                                it.email.contains(searchQuery, ignoreCase = true)
                    }

                    if (filteredMembers.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No members found matching \"$searchQuery\"",
                                textAlign = TextAlign.Center,
                                style = Typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(AppSpacing.medium)
                        ) {
                            items(filteredMembers) { member ->
                                val isBlocked = blockedUserIds.contains(member.id)
                                MemberCard(
                                    member = member,
                                    isBlocked = isBlocked,
                                    onChatClick = { onNavigateToChat(member.username, R.drawable.avatar, member.id) },
                                    onBlockToggle = {
                                        if (isBlocked) {
                                            listViewModel.unblockUser(member.id)
                                        } else {
                                            listViewModel.blockUser(member.id)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveChatCard(
    conversation: ZIMConversation,
    resolvedName: String,
    resolvedImageUrl: String?,
    lastMessageText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            ) {
                com.example.studygether.View.Components.AvatarImage(
                    imageUrl = resolvedImageUrl,
                    name = resolvedName,
                    size = 48.dp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = resolvedName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lastMessageText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                )
            }
            if (conversation.unreadMessageCount > 0) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.Red, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversation.unreadMessageCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MemberCard(
    member: User,
    isBlocked: Boolean,
    onChatClick: () -> Unit,
    onBlockToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
            ) {
                com.example.studygether.View.Components.AvatarImage(
                    imageUrl = member.profileImageUrl,
                    name = member.username,
                    size = 44.dp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = member.username,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = member.email,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f)
                )
            }

            // Chat Action button
            IconButton(
                onClick = onChatClick,
                enabled = !isBlocked,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Chat",
                    tint = if (isBlocked) Color.Gray else MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Block Action button
            Button(
                onClick = onBlockToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isBlocked) Color.Red else Color.LightGray,
                    contentColor = if (isBlocked) Color.White else Color.Black
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = if (isBlocked) Icons.Default.Lock else Icons.Default.LockOpen,
                    contentDescription = if (isBlocked) "Unblock" else "Block",
                    modifier = Modifier.size(14.dp),
                    tint = if (isBlocked) Color.White else Color.Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isBlocked) "Blocked" else "Block",
                    fontSize = 11.sp
                )
            }
        }
    }
}
