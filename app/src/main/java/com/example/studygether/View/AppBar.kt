package com.example.studygether.View

import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.studygether.Navigation.bottomNavItems
import com.example.studygether.ViewModels.MainActivityViewModel
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.studygether.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController)
{
    val viewModel: MainActivityViewModel= viewModel()
    val dividerColor= MaterialTheme.colorScheme.outlineVariant
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navScreens = navBackStackEntry?.destination?.let { dest ->
        bottomNavItems.any { dest.hasRoute(it.route::class) }
    }?:false
    TopAppBar(
        title = viewModel.topBarState.title,
        colors= TopAppBarDefaults.topAppBarColors(
            containerColor =if(viewModel.topBarState.barColor==Color.Unspecified){
                MaterialTheme.colorScheme.primary}else{viewModel.topBarState.barColor},
            titleContentColor = Color.White
        ),
        navigationIcon = {
            if (viewModel.topBarState.showBackButton) {
                IconButton (onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }
        },
        actions = viewModel.topBarState.actions,
        modifier =  if (!navScreens)
                        {Modifier.drawBehind({drawLine(color = dividerColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2.dp.toPx())})
                        }
                    else
                    Modifier


    )
}
@Composable
fun BottomBar(navController: NavController)
{

    val dividerColor= MaterialTheme.colorScheme.outlineVariant
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    NavigationBar (
        modifier = Modifier.drawBehind({drawLine(color = dividerColor,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 1.dp.toPx())}),
         containerColor = MaterialTheme.colorScheme.background,
         contentColor = MaterialTheme.colorScheme.onPrimary,
        tonalElevation = 0.dp
    )
    {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = navBackStackEntry?.destination?.hasRoute(item.route::class)?:false,

                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(
                            navController.graph.findStartDestination().id
                        ) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label, style = Typography.labelMedium) }
            )
        }
    }
}