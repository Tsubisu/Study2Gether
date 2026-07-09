
@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.App.NoCommunityJoinedContent
import com.example.studygether.Model.ChannelModel
import com.example.studygether.R
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ViewModels.ChannelsViewModel
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.tokens.AppSpacing

@Composable
fun ChannelListScreen(
    modifier: Modifier,
    onNavigateToChannel: (channelId: String, channelName: String, communityId: String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    val channelsViewModel: ChannelsViewModel = viewModel()

    val selectedCommunity by channelsViewModel.selectedCommunity.collectAsStateWithLifecycle()
    val channels by channelsViewModel.channels.collectAsStateWithLifecycle()
    val isOwner by channelsViewModel.isOwner.collectAsStateWithLifecycle()

    var showCreateDialog by remember { mutableStateOf(false) }
    var channelNameText by remember { mutableStateOf("") }
    var channelDescText by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }

    LaunchedEffect(selectedCommunity) {
        val titleText = selectedCommunity?.name ?: "Channels"
        appBarsViewModel.setTitleBar(
            title = { Text(titleText, style = Typography.headlineMedium) },
            actions = {
                IconButton(onClick = onNavigateToProfile) {
                    Icon(Icons.Default.Face, contentDescription = null)
                }
            }
        )
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.NavBar))
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (selectedCommunity == null) {
            Box(
                modifier = Modifier
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
                ) {
                    NoCommunityJoinedContent()
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                ) {
                    if (channels.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No channels in this community yet.\n" +
                                        if (isOwner) "Click the '+' button below to create one!" else "Ask the community owner to create channels.",
                                textAlign = TextAlign.Center,
                                style = Typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(all = AppSpacing.small),
                            verticalArrangement = Arrangement.spacedBy(AppSpacing.medium)
                        ) {
                            items(channels) { ch ->
                                ChannelCard(
                                    channel = ch,
                                    communityId = selectedCommunity!!.id,
                                    onClick = onNavigateToChannel
                                )
                            }
                        }
                    }
                }
            }

            if (isOwner) {
                FloatingActionButton(
                    onClick = {
                        channelNameText = ""
                        channelDescText = ""
                        showCreateDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create Channel")
                }
            }
        }

        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { if (!isCreating) showCreateDialog = false },
                title = { Text("Create Channel") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = channelNameText,
                            onValueChange = { channelNameText = it },
                            label = { Text("Channel Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !isCreating
                        )
                        OutlinedTextField(
                            value = channelDescText,
                            onValueChange = { channelDescText = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isCreating
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (channelNameText.isBlank()) return@Button
                            isCreating = true
                            channelsViewModel.createChannel(
                                name = channelNameText.trim(),
                                description = channelDescText.trim()
                            ) { result ->
                                isCreating = false
                                showCreateDialog = false
                            }
                        },
                        enabled = !isCreating && channelNameText.isNotBlank()
                    ) {
                        if (isCreating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Create")
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showCreateDialog = false },
                        enabled = !isCreating
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ChannelCard(
    channel: ChannelModel,
    communityId: String,
    onClick: (channelId: String, channelName: String, communityId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(channel.id, channel.name, communityId) },
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
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = channel.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = channel.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = channel.description.ifEmpty { "No description provided." },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                )
            }
        }
    }
}
