package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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


@Composable
fun HomeScreen(modifier: Modifier) {
    val selectedCommunity = UserCommunity.currentUserSelectedCommunity.collectAsStateWithLifecycle()
    val screenState = rememberCommunityScreenState()
    val currentUser = SessionState.currentUser.collectAsStateWithLifecycle()
    val communities = UserCommunity.userCommunityList.collectAsStateWithLifecycle()

    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel = viewModel<AppBarsViewModel>(activity)

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
            actions = { IconButton(onClick = {}) { Icon(Icons.Default.Face, contentDescription = null) } }
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
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(currentUser.value?.username ?: "")
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(screenState.community.name)
                            }
                            // actual channel list content goes here
                        }
                    }
                }
           }
        }
    }
}

//@Composable
//fun HomeScreen(modifier: Modifier)
//{
//    val selectedCommunity= UserCommunity.currentUserSelectedCommunity.collectAsStateWithLifecycle()
//    val currentUser= SessionState.currentUser.collectAsStateWithLifecycle()
//
//    val activity = LocalActivity.current as ComponentActivity
//    val appBarsViewModel= viewModel<AppBarsViewModel>(activity)
//    LaunchedEffect(Unit)
//    {
//        appBarsViewModel.setTitleBar(
//            title ={Text("Channels",style= Typography.headlineMedium)},
//            actions = {IconButton(onClick={})
//            {
//                Icon(Icons.Default.Face,contentDescription = null)
//            }
//            }
//        )
//
//        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.NavBar))
//    }
//    Box(modifier)
//    {
//        Box(modifier = Modifier.fillMaxSize().background(color= MaterialTheme.colorScheme.primary)){
//
//
//            Column(modifier=Modifier.fillMaxSize())
//            {
//                Row(modifier= Modifier.fillMaxWidth())
//                {
//                    Text(currentUser.value!!.username)
//                }
//                Row(modifier= Modifier.fillMaxWidth())
//                {
//                    Text(selectedCommunity.value!!.name)
//                }
//            }
//        }
//
//    }
//}