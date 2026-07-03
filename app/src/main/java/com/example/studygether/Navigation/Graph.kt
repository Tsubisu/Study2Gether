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
import com.example.studygether.ViewModels.AppBarsViewModel


import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studygether.App.SessionState
import com.example.studygether.View.Screens.HomeScreen
import com.example.studygether.View.Screens.SignInScreen

@Composable
fun AppGraph()
{
    val navController= rememberNavController()
    val isLoggedIn by SessionState.isLoggedIn.collectAsStateWithLifecycle()
    val startDestination = remember(isLoggedIn) { if (isLoggedIn) MainGraphRoute else AuthGraph }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar(navController) }
    )

    {
            innerPadding->
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
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
                composable<ChannelList>
                {
                    ChannelListScreen(modifier = Modifier.padding(innerPadding),{ name, image, memberCount ->
                        navController.navigate(
                            Channel(name, image, memberCount)
                        )
                    })
                }

                composable<ConvoList>
                {
                    ConvoListScreen(
                        modifier = Modifier.padding(innerPadding),
                        { name, image ->
                            navController.navigate(
                                Convo(
                                    name, image
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
                        onGoBack = { navController.navigateUp() })
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

            }
            navigation<AuthGraph>(startDestination = Login )
            {
                composable<Login>
                {
                    LoginScreen(onLoginSuccess = {navController.navigate(MainGraphRoute)
                    {
                        popUpTo(Login){inclusive= true}
                    } },
                        onSignIn = {navController.navigate(SignIn)},
                        onCreateCommunity = {navController.navigate(CommunityCreation)},
                        onForgetPassword = {navController.navigate(ForgetPassword)},
                    )

                }

                composable<SignIn>
                {
                    SignInScreen(onBackToLogin = {navController.navigateUp()})
                }

                composable<ForgetPassword>
                {
                    ForgetPasswordScreen(
                        onBackToLogin = {navController.navigate(Login)
                        {
                            popUpTo(Login){inclusive= false}
                        } },
                    )
                }

                composable<CommunityCreation>
                {
                    CommunityCreationScreen(onBackToLogin = {navController.navigateUp()})
                }
            }

        }


    }

}

