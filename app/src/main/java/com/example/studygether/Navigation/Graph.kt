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
import com.example.studygether.View.Screens.CommunityCreationScreen
import com.example.studygether.View.Screens.ConvoListScreen
import com.example.studygether.View.Screens.ConvoScreen
import com.example.studygether.View.AppBars.TopBar
import com.example.studygether.View.Screens.ProfileScreen
import com.example.studygether.View.Screens.ThemeSelectionScreen
import com.example.studygether.View.Screens.SecurityBody
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studygether.App.SessionState
import com.example.studygether.View.Screens.HomeScreen
import com.example.studygether.View.Screens.SignInScreen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar(navController) }
    )

    { innerPadding ->
        NavHost(
            // modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = startDestination,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }

        )
        {
            navigation<MainGraphRoute>(startDestination = ChannelList) {

                composable<Home> {
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        onNavigateToProfile = { navController.navigate(Profile) }
                    )
                }
                composable<ChannelList>
                {
                    ChannelListScreen(
                        modifier = Modifier.padding(innerPadding),
                        { name, image, memberCount ->
                            navController.navigate(
                                Channel(name, image, memberCount)
                            )
                        })
                }

                composable<ConvoList>
                {
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
                composable<Convo>
                { backStackEntry ->
                    val route: Convo = backStackEntry.toRoute()
                    ConvoScreen(
                        modifier = Modifier.padding(innerPadding),
                        name = route.name,
                        image = route.image,
                        targetUserId = route.targetUserId,
                        onGoBack = { navController.navigateUp() })
                }

                composable<VideoCall>
                { backStackEntry ->
                    val route: VideoCall = backStackEntry.toRoute()
                    com.example.studygether.View.Screens.VideoCallScreen(
                        roomId = route.roomId,
                        targetUserId = route.targetUserId,
                        onCallEnded = {
                            navController.navigateUp()
                        }
                    )
                }

                composable<Channel>
                {

                        backStackEntry ->
                    val route: Channel = backStackEntry.toRoute()
                    ChannelScreen(
                        modifier = Modifier.padding(innerPadding),
                        channelName = route.channelName,
                        channelLogo = route.channelLogo,
                        channelMemberCount = route.channelMemberCount
                    )
                }

                composable<Profile> {
                    ProfileScreen(
                        onNavigateBack = { navController.navigateUp() },
                        onNavigateToTheme = { navController.navigate(ThemeSelection) },
                        onNavigateToSecurity = { navController.navigate(Security) }
                    )
                }

                composable<ThemeSelection> {
                    ThemeSelectionScreen(
                        onNavigateBack = { navController.navigateUp() }
                    )
                }

                composable<Security> {
                    SecurityBody(
                        onNavigateBack = { navController.navigateUp() }
                    )
                }
            }
            navigation<AuthGraph>(startDestination = Login)
            {
                composable<Login>
                {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate(MainGraphRoute)
                            {
                                popUpTo(Login) { inclusive = true }
                            }
                        },
                        onSignIn = { navController.navigate(SignIn) },
                        onCreateCommunity = { navController.navigate(CommunityCreation) },
                        onForgetPassword = { navController.navigate(ForgetPassword) },
                    )

                }

                composable<SignIn>
                {
                    SignInScreen(onBackToLogin = { navController.navigateUp() })
                }

                composable<ForgetPassword>
                {
                    ForgetPasswordScreen(
                        onBackToLogin = {
                            navController.navigate(Login)
                            {
                                popUpTo(Login) { inclusive = false }
                            }
                        },
                    )
                }

                composable<CommunityCreation>
                {
                    CommunityCreationScreen(onBackToLogin = { navController.navigateUp() })
                }
            }
        }
    }
}
