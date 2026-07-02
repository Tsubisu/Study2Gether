package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.View.AppBars.ChannelTitleCard
import com.example.studygether.ViewModels.AppBarsViewModel


@Composable
fun ChannelScreen(
    modifier: Modifier,
    channelName: String,
    channelLogo: Int,
    channelMemberCount: Int,
) {
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    val barColor: Color = MaterialTheme.colorScheme.background
    LaunchedEffect(channelName, channelLogo, channelMemberCount) {
        appBarsViewModel.setTitleBar(
            title = { ChannelTitleCard(channelName, channelLogo, channelMemberCount) },
            showBackButton = true,
            actions = {
                Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Add, contentDescription = null)
                    }

                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Settings, contentDescription = null)
                    }
                }
            },
            barColor = barColor
        )
    }
}
