package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.App.CommunityScreenState
import com.example.studygether.App.SessionState
import com.example.studygether.App.UserCommunity
import com.example.studygether.App.rememberCommunityScreenState
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ui.theme.Typography
import com.example.studygether.App.LoadingIndicator
import com.example.studygether.App.NoCommunityJoinedContent
import kotlinx.coroutines.flow.flowOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import com.example.studygether.ViewModels.ConvoListViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.studygether.Repository.AppRepositories
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch
import com.example.studygether.R
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.provider.MediaStore
import com.example.studygether.View.Components.AvatarImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    onNavigateToProfile: () -> Unit,
    onNavigateToChannel: (channelId: String, channelName: String, communityId: String) -> Unit,
    onNavigateToChat: (name: String, image: Int, targetUserId: String) -> Unit
) {
    val selectedCommunity = UserCommunity.currentUserSelectedCommunity.collectAsStateWithLifecycle()
    val screenState = rememberCommunityScreenState()
    val currentUser = SessionState.currentUser.collectAsStateWithLifecycle()
    val communities = UserCommunity.userCommunityList.collectAsStateWithLifecycle()

    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel = viewModel<AppBarsViewModel>(activity)

    var showAddMemberDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val communityLogoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null && screenState is CommunityScreenState.Loaded) {
                AppRepositories.imageRepository.uploadImage(context, uri) { resultData ->
                    if (resultData != null) {
                        scope.launch {
                            AppRepositories.communityRepository.updateCommunityImage(
                                screenState.community.id,
                                resultData.url,
                                resultData.publicId
                            ).onSuccess {
                                Toast.makeText(context, "Community logo updated!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.NavBar))
    }

    LaunchedEffect(screenState, showAddMemberDialog, currentUser.value) {
        val title = when (screenState) {
            is CommunityScreenState.Loaded -> screenState.community.name
            else -> "HomeScreen"
        }
        val isOwner = screenState is CommunityScreenState.Loaded && currentUser.value?.id == screenState.community.creatorId

        appBarsViewModel.setTitleBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (screenState is CommunityScreenState.Loaded) {
                        val community = screenState.community
                        val logoModifier = if (isOwner) {
                             Modifier.clickable {
                                 val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                 communityLogoLauncher.launch(intent)
                             }
                        } else {
                            Modifier
                        }
                        AvatarImage(
                            imageUrl = community.profileImageUrl,
                            name = community.name,
                            size = 32.dp,
                            modifier = logoModifier
                        )
                    }
                    Text(title, style = Typography.headlineMedium)
                }
            },
            actions = {
                if (isOwner) {
                    IconButton(onClick = { showAddMemberDialog = true }) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Add Member")
                    }
                }
            }
        )
    }

    Box(modifier) {
        Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.primary)) {
            Column(
                modifier = Modifier.fillMaxSize().background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
            ) {
                when (screenState) {
                    is CommunityScreenState.Loading -> LoadingIndicator()
                    is CommunityScreenState.NoCommunity -> NoCommunityJoinedContent()
                    is CommunityScreenState.Loaded -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            
                            Text(
                                text = "Recent Chats",
                                style = Typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
                            )

                            val convoViewModel: ConvoListViewModel = viewModel()
                            val activeConversations by convoViewModel.activeConversations.collectAsStateWithLifecycle()
                            val communityMembers by convoViewModel.communityMembers.collectAsStateWithLifecycle()

                            if (activeConversations.isEmpty()) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = "No recent chats in this community.",
                                        style = Typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            } else {
                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(activeConversations) { convo ->
                                        val matchingMember = communityMembers.firstOrNull { it.id == convo.conversationID }
                                        val resolvedName = matchingMember?.username ?: convo.conversationName.ifEmpty { convo.conversationID }
                                        val resolvedImageUrl = matchingMember?.profileImageUrl

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .width(80.dp)
                                                .clickable {
                                                    onNavigateToChat(resolvedName, R.drawable.avatar, convo.conversationID)
                                                }
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(56.dp)
                                                    .clip(CircleShape)
                                            ) {
                                                com.example.studygether.View.Components.AvatarImage(
                                                    imageUrl = resolvedImageUrl,
                                                    name = resolvedName,
                                                    size = 56.dp
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = resolvedName,
                                                style = Typography.bodySmall.copy(
                                                    fontWeight = FontWeight.Medium,
                                                    color = MaterialTheme.colorScheme.onBackground
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                            
                            val channelsFlow = remember(screenState.community.id) {
                                AppRepositories.channelRepository.observeChannelsForCommunity(screenState.community.id)
                            }.collectAsState(initial = emptyList())
                            val channels = channelsFlow.value

                            Text(
                                text = "Recent Channels",
                                style = Typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
                            )

                            if (channels.isEmpty()) {
                                Box(
                                    modifier = Modifier.weight(1f).fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No channels created in this community yet.",
                                        style = Typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                    )
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier.weight(1f).fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(channels) { channel ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    onNavigateToChannel(channel.id, channel.name, screenState.community.id)
                                                },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                AvatarImage(
                                                    imageUrl = channel.imageUrl,
                                                    name = channel.name,
                                                    size = 36.dp
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = channel.name,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp,
                                                        color = MaterialTheme.colorScheme.onSecondary
                                                    )
                                                    Text(
                                                        text = channel.description.ifEmpty { "No description provided." },
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        fontSize = 11.sp,
                                                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
           }
        }
    }

    if (showAddMemberDialog && screenState is CommunityScreenState.Loaded) {
        val community = screenState.community
        var emailText by remember { mutableStateOf("") }
        var isAdding by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { if (!isAdding) showAddMemberDialog = false },
            title = { Text("Add Member to Community") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Enter the email address of the user you want to add to ${community.name}:", style = Typography.bodyMedium)
                    OutlinedTextField(
                        value = emailText,
                        onValueChange = { emailText = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isAdding
                    )
                    if (errorMessage.isNotEmpty()) {
                        Text(errorMessage, color = MaterialTheme.colorScheme.error, style = Typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (emailText.isBlank()) return@Button
                        isAdding = true
                        errorMessage = ""
                        scope.launch {
                            AppRepositories.userRepository.getUserByEmail(emailText.trim()).fold(
                                onSuccess = { user ->
                                    if (user == null) {
                                        errorMessage = "No user found with this email."
                                        isAdding = false
                                    } else {
                                        AppRepositories.communityRepository.isMember(community.id, user.id).fold(
                                            onSuccess = { alreadyMember ->
                                                if (alreadyMember) {
                                                    errorMessage = "User is already a member of this community."
                                                    isAdding = false
                                                } else {
                                                    AppRepositories.communityRepository.addMember(community.id, user.id).fold(
                                                        onSuccess = {
                                                            Toast.makeText(context, "Member added successfully!", Toast.LENGTH_SHORT).show()
                                                            isAdding = false
                                                            showAddMemberDialog = false
                                                        },
                                                        onFailure = { error ->
                                                            errorMessage = error.message ?: "Failed to add member."
                                                            isAdding = false
                                                        }
                                                    )
                                                }
                                            },
                                            onFailure = { error ->
                                                errorMessage = error.message ?: "Failed to verify membership status."
                                                isAdding = false
                                            }
                                        )
                                    }
                                },
                                onFailure = { error ->
                                    errorMessage = error.message ?: "Failed to look up user."
                                    isAdding = false
                                }
                            )
                        }
                    },
                    enabled = !isAdding && emailText.isNotBlank()
                ) {
                    if (isAdding) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Add")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddMemberDialog = false },
                    enabled = !isAdding
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}