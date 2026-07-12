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
import android.widget.Toast
import androidx.compose.ui.layout.ContentScale

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.View.AppBars.BottomBars
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.provider.MediaStore
import androidx.compose.ui.platform.LocalContext
import com.example.studygether.ViewModels.BottomBarState

@Composable
fun ProfileScreen(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
    onNavigateToTheme: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    onNavigateToAboutUs: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    
    val barBgColor = MaterialTheme.colorScheme.background
    LaunchedEffect(Unit) {
        appBarsViewModel.setTitleBar(
            title = { Text("You", fontWeight = FontWeight.Bold) },
            showBackButton = true,
            actions = {},
            barColor = barBgColor
        )
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
    }

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isUploading by viewModel.isUploading.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showCustomStatusDialog by remember { mutableStateOf(false) }
    var tempStatusText by remember { mutableStateOf("") }

    var showCreateCommunityDialog by remember { mutableStateOf(false) }
    var communityName by remember { mutableStateOf("") }
    var isCommunityPublic by remember { mutableStateOf(true) }
    var selectedCommunityImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var isCreatingCommunity by remember { mutableStateOf(false) }
    var communityCreationError by remember { mutableStateOf("") }

    val communityImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                selectedCommunityImageUri = uri
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                viewModel.uploadProfileImage(context, uri)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
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
            Box(
                modifier = Modifier.clickable(enabled = !isUploading) {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    launcher.launch(intent)
                }
            ) {
                com.example.studygether.View.Components.AvatarImage(
                    imageUrl = currentUser?.profileImageUrl,
                    name = currentUser?.username ?: "Anonymous User",
                    size = 68.dp,
                    textStyle = Typography.headlineMedium
                )

                if (isUploading) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Edit Profile Picture",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Active dot
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .size(12.dp)
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
                    tint = MaterialTheme.colorScheme.primary
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

            ProfileOptionItem(
                icon = Icons.Default.Info,
                title = "About Us",
                onClick = onNavigateToAboutUs
            )

            ProfileOptionItem(
                icon = Icons.Default.GroupAdd,
                title = "Create Community",
                onClick = {
                    communityName = ""
                    isCommunityPublic = true
                    selectedCommunityImageUri = null
                    communityCreationError = ""
                    showCreateCommunityDialog = true
                }
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

    if (showCreateCommunityDialog) {
        AlertDialog(
            onDismissRequest = { if (!isCreatingCommunity) showCreateCommunityDialog = false },
            title = { Text("Create Community") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = communityName,
                        onValueChange = { communityName = it },
                        label = { Text("Community Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isCreatingCommunity
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Public Community")
                        Switch(
                            checked = isCommunityPublic,
                            onCheckedChange = { isCommunityPublic = it },
                            enabled = !isCreatingCommunity
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Community Logo", style = Typography.titleSmall)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable(enabled = !isCreatingCommunity) {
                                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                    communityImageLauncher.launch(intent)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedCommunityImageUri != null) {
                                AsyncImage(
                                    model = selectedCommunityImageUri,
                                    contentDescription = "Selected logo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Pick logo",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        TextButton(
                             onClick = {
                                 val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                 communityImageLauncher.launch(intent)
                             },
                            enabled = !isCreatingCommunity
                        ) {
                            Text("Choose Image")
                        }
                    }

                    if (communityCreationError.isNotEmpty()) {
                        Text(
                            text = communityCreationError,
                            color = MaterialTheme.colorScheme.error,
                            style = Typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (communityName.isBlank()) return@Button
                        isCreatingCommunity = true
                        communityCreationError = ""

                        if (selectedCommunityImageUri != null) {
                            com.example.studygether.Repository.AppRepositories.imageRepository.uploadImage(context, selectedCommunityImageUri!!) { result ->
                                if (result != null) {
                                    viewModel.createCommunity(
                                        name = communityName.trim(),
                                        isPublic = isCommunityPublic,
                                        logoUrl = result.url,
                                        logoPublicId = result.publicId,
                                        onSuccess = {
                                            isCreatingCommunity = false
                                            showCreateCommunityDialog = false
                                            Toast.makeText(context, "Community created successfully!", Toast.LENGTH_SHORT).show()
                                        },
                                        onFailure = { error ->
                                            communityCreationError = error
                                            isCreatingCommunity = false
                                        }
                                    )
                                } else {
                                    communityCreationError = "Failed to upload community logo."
                                    isCreatingCommunity = false
                                }
                            }
                        } else {
                            viewModel.createCommunity(
                                name = communityName.trim(),
                                isPublic = isCommunityPublic,
                                logoUrl = "",
                                logoPublicId = "",
                                onSuccess = {
                                    isCreatingCommunity = false
                                    showCreateCommunityDialog = false
                                    Toast.makeText(context, "Community created successfully!", Toast.LENGTH_SHORT).show()
                                },
                                onFailure = { error ->
                                    communityCreationError = error
                                    isCreatingCommunity = false
                                }
                            )
                        }
                    },
                    enabled = !isCreatingCommunity && communityName.isNotBlank()
                ) {
                    if (isCreatingCommunity) {
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
                    onClick = { showCreateCommunityDialog = false },
                    enabled = !isCreatingCommunity
                ) {
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
            //tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            tint = MaterialTheme.colorScheme.primary,
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
