package com.example.studygether.Navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.studygether.View.BottomBar
import com.example.studygether.View.ChannelScreen
import com.example.studygether.View.ConvoListScreen
import com.example.studygether.View.ConvoScreen
import com.example.studygether.View.TopBar
import com.example.studygether.ViewModels.MainActivityViewModel

@Composable
fun AuthGraph()
{


}

@Composable
fun MainGraph(viewModel: MainActivityViewModel)
{
    val navController= rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomNav = navBackStackEntry?.destination?.let { dest ->
        bottomNavItems.any { dest.hasRoute(it.route::class) }
    }?:false

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {TopBar(navController)},
        bottomBar = {if (showBottomNav)
            {
                BottomBar(navController)

            }
        }
    )
    {
        innerPadding->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = MainGraph
        )
        {
            navigation<MainGraph>(startDestination = Channels){
                composable<Channels>
                {
                    ChannelScreen(viewModel)
                }

                composable<ConvoList>
                {
                    ConvoListScreen(viewModel,
                        {name, image ->
                            navController.navigate(
                                Convo(
                                    name,image) )})
                }
                composable<Convo>
                {
                        backStackEntry ->
                    val route: Convo = backStackEntry.toRoute()
                    ConvoScreen(viewModel,
                        name = route.name,
                        image = route.image,
                        onGoBack ={navController.navigate(ConvoList)})
                }

            }
        }

    }

}