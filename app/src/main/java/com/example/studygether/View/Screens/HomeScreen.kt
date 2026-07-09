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
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.studygether.Repository.AppRepositories
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch
import com.example.studygether.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    onNavigateToProfile: () -> Unit,
    onNavigateToChannel: (channelId: String, channelName: String, communityId: String) -> Unit
) {
    val selectedCommunity = UserCommunity.currentUserSelectedCommunity.collectAsStateWithLifecycle()
    val screenState = rememberCommunityScreenState()
    val currentUser = SessionState.currentUser.collectAsStateWithLifecycle()
    val communities = UserCommunity.userCommunityList.collectAsStateWithLifecycle()

    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel = viewModel<AppBarsViewModel>(activity)

    val scope = rememberCoroutineScope()
    var showInviteDialog by remember { mutableStateOf(false) }
    var inviteEmail by remember { mutableStateOf("") }
    var isInviting by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.NavBar))
    }

    LaunchedEffect(screenState) {
        val title = when (screenState) {
            is CommunityScreenState.Loaded -> screenState.community.name
            else -> "HomeScreen"
        }
        appBarsViewModel.setTitleBar(
            title = { Text(title, style = Typography.headlineMedium) },
            actions = { IconButton(onClick = onNavigateToProfile) { Icon(Icons.Default.Face, contentDescription = null) } }
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
                                text = "Welcome to ${screenState.community.name}, ${currentUser.value?.username ?: ""}",
                                style = Typography.titleLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            Button(
                                onClick = {
                                    inviteEmail = ""
                                    showInviteDialog = true
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PersonAdd, contentDescription = "Add Member")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Add Member by Email")
                            }
                            
                            val channelsFlow = remember(screenState.community.id) {
                                AppRepositories.channelRepository.observeChannelsForCommunity(screenState.community.id)
                            }.collectAsState(initial = emptyList())
                            val channels = channelsFlow.value

                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Channels",
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
                                                Icon(
                                                    painter = painterResource(id = R.drawable.baseline_people_24),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(24.dp)
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

    if (showInviteDialog) {
        val community = (screenState as? CommunityScreenState.Loaded)?.community
        AlertDialog(
            onDismissRequest = { if (!isInviting) showInviteDialog = false },
            title = { Text("Add Member to Community") },
            text = {
                Column {
                    Text("Enter the email address of the user to invite them to this community.", modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = inviteEmail,
                        onValueChange = { inviteEmail = it },
                        label = { Text("User Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isInviting
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (community == null) return@Button
                        if (inviteEmail.isBlank()) {
                            android.widget.Toast.makeText(context, "Email cannot be empty", android.widget.Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        isInviting = true
                        scope.launch {
                            com.example.studygether.Repository.AppRepositories.communityRepository
                                .addMemberByEmail(community.id, inviteEmail.trim(), false)
                                .fold(
                                    onSuccess = {
                                        android.widget.Toast.makeText(context, "User added successfully!", android.widget.Toast.LENGTH_SHORT).show()
                                        showInviteDialog = false
                                    },
                                    onFailure = { error ->
                                        android.widget.Toast.makeText(context, error.message ?: "Failed to add member", android.widget.Toast.LENGTH_LONG).show()
                                    }
                                )
                            isInviting = false
                        }
                    },
                    enabled = !isInviting
                ) {
                    if (isInviting) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Add")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showInviteDialog = false },
                    enabled = !isInviting
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}