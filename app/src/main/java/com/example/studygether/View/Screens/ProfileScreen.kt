package com.example.studygether.View.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.ViewModels.ProfileViewModel
import com.example.studygether.ui.theme.Typography
import coil3.compose.AsyncImage
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ViewModels.BottomBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTheme: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    LaunchedEffect(Unit) {
        appBarsViewModel.hideTopBar()
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
    }

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    var showCustomStatusDialog by remember { mutableStateOf(false) }
    var tempStatusText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("You", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // User Info Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar representation
                Box {
                    com.example.studygether.View.Components.AvatarImage(
                        imageUrl = currentUser?.profileImageUrl,
                        name = currentUser?.username ?: "Anonymous User",
                        size = 68.dp,
                        textStyle = Typography.headlineMedium
                    )

                    // Active dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(2.dp)
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentUser?.status == "Active") Color.Green else Color.Gray
                            )
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = currentUser?.username ?: "Anonymous User",
                        style = Typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Text(
                        text = currentUser?.status ?: "Active",
                        style = Typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Update status trigger
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        tempStatusText = currentUser?.customStatus ?: ""
                        showCustomStatusDialog = true
                    },
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SentimentSatisfied,
                        contentDescription = "Status",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (currentUser?.customStatus.isNullOrEmpty()) {
                            "Update your status"
                        } else {
                            currentUser?.customStatus.orEmpty()
                        },
                        style = Typography.bodyMedium.copy(
                            color = if (currentUser?.customStatus.isNullOrEmpty()) {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Options List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val isAway = currentUser?.status == "Away"
                ProfileOptionItem(
                    icon = Icons.Default.AccountCircle,
                    title = if (isAway) "Set yourself as active" else "Set yourself as away",
                    onClick = {
                        viewModel.updateStatus(if (isAway) "Active" else "Away")
                    }
                )


                ProfileOptionItem(
                    icon = Icons.Default.Tune,
                    title = "Preferences (Theme Chooser)",
                    onClick = onNavigateToTheme
                )

                ProfileOptionItem(
                    icon = Icons.Default.Security,
                    title = "Security",
                    onClick = onNavigateToSecurity
                )
            }

            // Logout Action
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Log Out")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }

    if (showCustomStatusDialog) {
        AlertDialog(
            onDismissRequest = { showCustomStatusDialog = false },
            title = { Text("Update status message") },
            text = {
                OutlinedTextField(
                    value = tempStatusText,
                    onValueChange = { tempStatusText = it },
                    placeholder = { Text("What's on your mind?") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateCustomStatus(tempStatusText)
                        showCustomStatusDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomStatusDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = Typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}
