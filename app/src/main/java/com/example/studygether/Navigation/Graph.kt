package com.example.studygether.Navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.studygether.View.Screens.LoginScreen
import com.example.studygether.View.Screens.ForgetPasswordScreen
import com.example.studygether.View.AppBars.BottomBar
import com.example.studygether.View.Screens.ChannelListScreen
import com.example.studygether.View.Screens.ChannelScreen
import com.example.studygether.View.Screens.PostDetailScreen
import com.example.studygether.View.Screens.CommunityCreationScreen
import com.example.studygether.View.Screens.ConvoListScreen
import com.example.studygether.View.Screens.ConvoScreen
import com.example.studygether.View.AppBars.TopBar
import com.example.studygether.View.Screens.ProfileScreen
import com.example.studygether.View.Screens.ThemeSelectionScreen
import com.example.studygether.View.Screens.SecurityBody
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studygether.App.SessionState
import com.example.studygether.View.Screens.HomeScreen
import com.example.studygether.View.Screens.SignInScreen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.background
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Apps
import androidx.compose.runtime.rememberCoroutineScope
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.Repository.AppRepositories


@Composable
fun AppGraph()
{
    val navController= rememberNavController()
    val isLoggedIn by SessionState.isLoggedIn.collectAsStateWithLifecycle()
    val startDestination = remember(isLoggedIn) { if (isLoggedIn) MainGraphRoute else AuthGraph }

    // Zego Call Invitation Signaling Overlay Handler
    val callState by com.example.studygether.Utility.ZegoService.callState.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current

    var callerUsername by remember { mutableStateOf<String?>(null) }

    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(drawerState) {
        appBarsViewModel.onMenuClick = {
            scope.launch {
                drawerState.open()
            }
        }
    }

    val currentUser by SessionState.currentUser.collectAsStateWithLifecycle()
    val communities by com.example.studygether.App.UserCommunity.userCommunityList.collectAsStateWithLifecycle()
    val selectedCommunity by com.example.studygether.App.UserCommunity.currentUserSelectedCommunity.collectAsStateWithLifecycle()

    LaunchedEffect(callState) {
        val incomingState = callState as? com.example.studygether.Utility.ZegoCallState.Incoming
        if (incomingState != null) {
            callerUsername = null
            AppRepositories.userRepository.getUser(incomingState.callerId).onSuccess { user ->
                callerUsername = user?.username
            }
        }
    }

    when (val state = callState) {
        is com.example.studygether.Utility.ZegoCallState.Incoming -> {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Incoming Video Call") },
                text = { Text("Call from: ${callerUsername ?: "Loading..."}") },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        onClick = {
                            com.example.studygether.Utility.ZegoService.acceptCall(state.callId, state.callerId, context)
                        }
                    ) {
                        Text("Accept", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        onClick = {
                            com.example.studygether.Utility.ZegoService.rejectCall(state.callId)
                        }
                    ) {
                        Text("Decline", color = Color.White)
                    }
                }
            )
        }
        is com.example.studygether.Utility.ZegoCallState.Active -> {
            LaunchedEffect(state.callId) {
                navController.navigate(VideoCall(state.roomId, state.targetUserId))
            }
        }
        else -> {}
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isLoggedIn,
        drawerContent = {
            if (isLoggedIn) {
                ModalDrawerSheet(
                    modifier = Modifier.width(300.dp),
                    drawerContainerColor = MaterialTheme.colorScheme.background,
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {
                   
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            com.example.studygether.View.Components.AvatarImage(
                                imageUrl = currentUser?.profileImageUrl,
                                name = currentUser?.username ?: "User",
                                size = 64.dp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = currentUser?.username ?: "Loading user...",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentUser?.email ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Navigation Items
                    Text(
                        text = "Navigation",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = backStackEntry?.destination

                    NavigationDrawerItem(
                        label = { Text("Home") },
                        selected = currentDestination?.route?.contains("Home") == true || currentDestination?.route?.contains("MainGraphRoute") == true,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Home) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Direct Messages") },
                        selected = currentDestination?.route?.contains("Convo") == true,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(ConvoList) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Textsms, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Channels") },
                        selected = currentDestination?.route?.contains("Channel") == true,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(ChannelList) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Groups, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Profile") },
                        selected = currentDestination?.route?.contains("Profile") == true,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Profile)
                        },
                        icon = { Icon(Icons.Default.Face, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Communities Section
                    Text(
                        text = "My Communities",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(communities) { community ->
                            if (community != null) {
                                val isSelectedComm = selectedCommunity?.id == community.id
                                NavigationDrawerItem(
                                    label = { Text(community.name) },
                                    selected = isSelectedComm,
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                        AppRepositories.communityRepository.selectCommunity(currentUser?.id ?: "", community.id)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Apps,
                                            contentDescription = null,
                                            tint = if (isSelectedComm) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                        )
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }
                        }
                    }

                    // Footer (Logout)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    NavigationDrawerItem(
                        label = { Text("Logout") },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                SessionState.clear()
                            }
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TopBar(navController) },
            bottomBar = { BottomBar(navController) }
        ) { innerPadding ->
            NavHost(
                // modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = startDestination,
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) },
                popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                popExitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                navigation<MainGraphRoute>(startDestination = ChannelList) {
                    composable<Home> {
                        HomeScreen(
                            modifier = Modifier.padding(innerPadding),
                            onNavigateToProfile = { navController.navigate(Profile) },
                            onNavigateToChannel = { channelId, channelName, communityId ->
                                navController.navigate(Channel(channelId, channelName, communityId))
                            },
                            onNavigateToChat = { name, image, targetUserId ->
                                navController.navigate(Convo(name, image, targetUserId))
                            }
                        )
                    }
                    composable<ChannelList> {
                        ChannelListScreen(
                            modifier = Modifier.padding(innerPadding),
                            onNavigateToChannel = { channelId, channelName, communityId ->
                                navController.navigate(Channel(channelId, channelName, communityId))
                            },
                            onNavigateToProfile = {
                                navController.navigate(Profile)
                            }
                        )
                    }

                    composable<ConvoList> {
                        ConvoListScreen(
                            modifier = Modifier.padding(innerPadding),
                            { name, image, targetUserId ->
                                navController.navigate(
                                    Convo(
                                        name, image, targetUserId
                                    )
                                )
                            })
                    }
                    composable<Convo> { backStackEntry ->
                        val route: Convo = backStackEntry.toRoute()
                        ConvoScreen(
                            modifier = Modifier.padding(innerPadding),
                            name = route.name,
                            image = route.image,
                            targetUserId = route.targetUserId,
                            onGoBack = { navController.navigateUp() })
                    }

                    composable<VideoCall> { backStackEntry ->
                        val route: VideoCall = backStackEntry.toRoute()
                        com.example.studygether.View.Screens.VideoCallScreen(
                            roomId = route.roomId,
                            targetUserId = route.targetUserId,
                            onCallEnded = {
                                navController.navigateUp()
                            }
                        )
                    }

                    composable<Channel> { backStackEntry ->
                        val route: Channel = backStackEntry.toRoute()
                        ChannelScreen(
                            modifier = Modifier.padding(innerPadding),
                            channelId = route.channelId,
                            channelName = route.channelName,
                            communityId = route.communityId,
                            onNavigateToPostDetail = { postId, channelId, communityId ->
                                navController.navigate(PostDetail(postId, channelId, communityId))
                            }
                        )
                    }

                    composable<PostDetail> { backStackEntry ->
                        val route: PostDetail = backStackEntry.toRoute()
                        PostDetailScreen(
                            modifier = Modifier.padding(innerPadding),
                            postId = route.postId,
                            channelId = route.channelId,
                            communityId = route.communityId,
                            onGoBack = {
                                navController.navigateUp()
                            }
                        )
                    }

                    composable<Profile> {
                        ProfileScreen(
                            modifier = Modifier.padding(innerPadding),
                            onNavigateBack = { navController.navigateUp() },
                            onNavigateToTheme = { navController.navigate(ThemeSelection) },
                            onNavigateToSecurity = { navController.navigate(Security) }
                        )
                    }

                    composable<ThemeSelection> {
                        ThemeSelectionScreen(
                            modifier = Modifier.padding(innerPadding),
                            onNavigateBack = { navController.navigateUp() }
                        )
                    }

                    composable<Security> {
                        SecurityBody(
                            modifier = Modifier.padding(innerPadding),
                            onNavigateBack = { navController.navigateUp() }
                        )
                    }
                }
                navigation<AuthGraph>(startDestination = Login) {
                    composable<Login> {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate(MainGraphRoute) {
                                    popUpTo(Login) { inclusive = true }
                                }
                            },
                            onSignIn = { navController.navigate(SignIn) },
                            onCreateCommunity = { navController.navigate(CommunityCreation) },
                            onForgetPassword = { navController.navigate(ForgetPassword) },
                        )
                    }

                    composable<SignIn> {
                        SignInScreen(onBackToLogin = { navController.navigateUp() })
                    }

                    composable<ForgetPassword> {
                        ForgetPasswordScreen(
                            onBackToLogin = {
                                navController.navigate(Login) {
                                    popUpTo(Login) { inclusive = false }
                                }
                            },
                        )
                    }

                    composable<CommunityCreation> {
                        CommunityCreationScreen(onBackToLogin = { navController.navigateUp() })
                    }
                }
            }
        }
    }
}
