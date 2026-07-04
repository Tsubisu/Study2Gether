package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.App.SessionState
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.View.AppBars.ChatUserLabelCard
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.ConvoViewModel
import com.example.studygether.ui.theme.Typography
import androidx.compose.runtime.collectAsState

@Composable
fun ConvoScreen(
    modifier: Modifier,
    name: String,
    image: Int,
    targetUserId: String,
    onGoBack: () -> Unit
) {
    val activity = LocalActivity.current as ComponentActivity
    val mainViewModel: AppBarsViewModel = viewModel(activity)
    val barColor: Color = MaterialTheme.colorScheme.background

    val chatViewModel: ConvoViewModel = viewModel()
    val messages by chatViewModel.messages.collectAsStateWithLifecycle()
    val targetUser by chatViewModel.targetUser.collectAsStateWithLifecycle()
    val isTargetMemberOfSameCommunity by chatViewModel.isTargetMemberOfSameCommunity.collectAsStateWithLifecycle()
    val blockedUserIds by chatViewModel.blockedUserIds.collectAsStateWithLifecycle()

    val isBlocked = blockedUserIds.contains(targetUserId)
    var messageText by remember { mutableStateOf("") }

    LaunchedEffect(targetUserId) {
        chatViewModel.initChat(targetUserId)
    }

    LaunchedEffect(targetUser, name, targetUserId, isBlocked) {
        val resolvedName = when {
            targetUser != null -> targetUser!!.username
            name.isNotBlank() && name != targetUserId && name.length < 24 -> name
            else -> "Loading chat..."
        }
        mainViewModel.setTitleBar(
            title = { ChatUserLabelCard(name = resolvedName, profileImageUrl = targetUser?.profileImageUrl, onClick = {}) },
            showBackButton = true,
            actions = {
                // Call trigger button
                IconButton(onClick = {
                    chatViewModel.startCall { callId ->
                        // Automatically navigated via AppGraph callState flow
                    }
                }) {
                    Icon(Icons.Default.Videocam, contentDescription = "Video Call")
                }

                // Block/Unblock toggle button
                IconButton(onClick = {
                    if (isBlocked) {
                        chatViewModel.unblockUser()
                    } else {
                        chatViewModel.blockUser()
                    }
                }) {
                    Icon(
                        imageVector = if (isBlocked) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = if (isBlocked) "Unblock" else "Block",
                        tint = if (isBlocked) Color.Red else MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            barColor = barColor
        )

        mainViewModel.setBottomBarType(BottomBarState(BottomBars.None))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Message list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            itemsIndexed(messages) { index, msg ->
                val isCurrentUser = msg.senderUserID == SessionState.currentUser.collectAsState().value?.id
                val text = when (msg) {
                    is im.zego.zim.entity.ZIMTextMessage -> msg.message
                    else -> "[Message]"
                }

                // Show avatar only if it is the last message in a consecutive block of the other user's messages
                val isLastInBlock = !isCurrentUser && (
                        index == messages.size - 1 ||
                                messages[index + 1].senderUserID != msg.senderUserID
                        )

                ChatBubble(
                    text = text,
                    isCurrentUser = isCurrentUser,
                    senderName = targetUser?.username ?: name,
                    senderImageUrl = targetUser?.profileImageUrl,
                    showAvatar = isLastInBlock
                )
            }
        }

        // Constraints and message input
        if (!isTargetMemberOfSameCommunity) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
            ) {
                Text(
                    text = "You can only chat with users in the same community.",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    style = Typography.bodyMedium
                )
            }
        } else if (isBlocked) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
            ) {
                Text(
                    text = "This user is blocked. Unblock them to send messages.",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    style = Typography.bodyMedium
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Type message...") },
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
                        if (messageText.isNotBlank()) {
                            chatViewModel.sendMessage(messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
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

@Composable
fun ChatBubble(
    text: String,
    isCurrentUser: Boolean,
    senderName: String = "",
    senderImageUrl: String? = null,
    showAvatar: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isCurrentUser) {
            if (showAvatar) {
                com.example.studygether.View.Components.AvatarImage(
                    imageUrl = senderImageUrl,
                    name = senderName,
                    size = 28.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                Spacer(modifier = Modifier.width(36.dp)) // aligns bubbles where avatar is absent
            }
        }

        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isCurrentUser) 16.dp else 0.dp,
                bottomEnd = if (isCurrentUser) 0.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(12.dp),
                color = if (isCurrentUser) Color.White else MaterialTheme.colorScheme.onSecondary,
                fontSize = 14.sp
            )
        }
    }
}