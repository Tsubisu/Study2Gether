package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.App.SessionState
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.View.AppBars.ChannelTitleCard
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.BottomBarState
import kotlinx.coroutines.launch

data class ChannelMessage(
    val senderName: String,
    val text: String,
    val isCurrentUser: Boolean
)

@Composable
fun ChannelScreen(
    modifier: Modifier,
    channelName: String,
    channelLogo: Int,
    channelMemberCount: Int,
) {
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    
    LaunchedEffect(channelName, channelLogo, channelMemberCount) {
        appBarsViewModel.setTitleBar(
            title = { ChannelTitleCard(channelName, channelLogo, channelMemberCount) },
            showBackButton = true,
            actions = {
                Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Add, contentDescription = null)
                    }

                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Settings, contentDescription = null)
                    }
                }
            },
            barColor = Color.Unspecified
        )
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
    }

    val currentUser = SessionState.currentUser.collectAsState().value
    val currentUserName = currentUser?.username ?: "You"

    val initialMessages = remember(channelName) {
        when (channelName) {
            "Study Group: Calculus" -> listOf(
                ChannelMessage("Alice", "Welcome to the Calculus study channel! 📐", false),
                ChannelMessage("Bob", "Hey guys, has anyone solved the limits problem on page 42?", false),
                ChannelMessage("Charlie", "Yeah, you need to use L'Hopital's rule there first.", false),
                ChannelMessage("Alice", "Thanks Charlie! That worked perfectly.", false)
            )
            "Tech Support" -> listOf(
                ChannelMessage("Support Bot", "Welcome to Tech Support! Let us know if you experience any bugs or issues.", false),
                ChannelMessage("Alice", "I'm having trouble getting the Zego video call to connect.", false),
                ChannelMessage("Support Bot", "Please verify your APP_ID and APP_SIGN settings in ZegoConfig.", false)
            )
            "Kotlin & Android" -> listOf(
                ChannelMessage("Alice", "Jetpack Compose is awesome! 🚀", false),
                ChannelMessage("Bob", "Totally, much better than traditional XML layouts.", false)
            )
            "General Chat" -> listOf(
                ChannelMessage("Alice", "Hey StudyGether team! How is it going?", false),
                ChannelMessage("Bob", "All good here, working on our new updates.", false)
            )
            else -> listOf(
                ChannelMessage("System", "Welcome to the $channelName channel!", false)
            )
        }
    }

    val messages = remember { mutableStateListOf<ChannelMessage>().apply { addAll(initialMessages) } }
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Messages List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(messages) { msg ->
                        GroupChatBubble(
                            text = msg.text,
                            senderName = msg.senderName,
                            isCurrentUser = msg.isCurrentUser
                        )
                    }
                }

                // Input field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
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
                                messages.add(ChannelMessage(currentUserName, messageText, true))
                                messageText = ""
                                scope.launch {
                                    if (messages.isNotEmpty()) {
                                        listState.animateScrollToItem(messages.size - 1)
                                    }
                                }
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
}

@Composable
fun GroupChatBubble(
    text: String,
    senderName: String,
    isCurrentUser: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isCurrentUser) {
            com.example.studygether.View.Components.AvatarImage(
                imageUrl = null,
                name = senderName,
                size = 32.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
        ) {
            if (!isCurrentUser) {
                Text(
                    text = senderName,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
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
}
