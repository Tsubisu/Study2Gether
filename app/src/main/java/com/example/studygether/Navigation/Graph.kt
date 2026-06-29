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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.studygether.ForgetPasswordBody
import com.example.studygether.LoginScreen
import com.example.studygether.View.BottomBar
import com.example.studygether.View.Channel
import com.example.studygether.View.ChannelListScreen
import com.example.studygether.View.CommunityCreationPage
import com.example.studygether.View.ConvoListScreen
import com.example.studygether.View.ConvoScreen
import com.example.studygether.View.TopBar
import com.example.studygether.ViewModels.MainActivityViewModel


@Composable
fun AppGraph(viewModel: MainActivityViewModel= viewModel())
{
    val navController= rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isAuthScreen = navBackStackEntry?.destination?.let { dest ->
        dest.hasRoute(Login::class)
        dest.hasRoute(CommunityCreation::class)
        dest.hasRoute(ForgetPassword::class)
    } ?: true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {if(!isAuthScreen)TopBar(navController)},
        bottomBar = {BottomBar(viewModel.bottomBarState.bottomBar,navController)}
    )
    {
            innerPadding->
        NavHost(
           // modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AuthGraph,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }

        )
        {
            navigation<MainGraphRoute>(startDestination = ChannelList) {
                composable<ChannelList>
                {
                    ChannelListScreen(viewModel, modifier = Modifier.padding(innerPadding),{ name, image, memberCount ->
                        navController.navigate(
                            Channel(name, image, memberCount)
                        )
                    })
                }

                composable<ConvoList>
                {
                    ConvoListScreen(
                        viewModel,
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
                        viewModel,
                        modifier = Modifier.padding(innerPadding),
                        name = route.name,
                        image = route.image,
                        onGoBack = { navController.navigateUp() })
                }

                composable<Channel>
                {

                        backStackEntry ->
                    val route: Channel = backStackEntry.toRoute()
                    Channel(
                        viewModel,
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
                    LoginScreen(onLogin = {navController.navigate(MainGraphRoute)
                    {
                        popUpTo(Login){inclusive= true}
                    } },
                        onCreateCommunity = {navController.navigate(CommunityCreation)},
                        onForgetPassword = {navController.navigate(ForgetPassword)})

                }

                composable<ForgetPassword>
                {
                    ForgetPasswordBody()
                }

                composable<CommunityCreation>
                {
                    CommunityCreationPage()
                }
            }

        }


    }

}

