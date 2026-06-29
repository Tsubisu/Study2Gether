package com.example.studygether.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.PostAdd
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
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ViewModels.MainActivityViewModel
import com.example.studygether.ViewModels.TopBarState
import com.example.studygether.ui.theme.tokens.AppSpacing

@Composable
fun Channel(mainViewModel: MainActivityViewModel= viewModel(),
            modifier: Modifier,
            channelName:String,
            channelLogo:Int,
            channelMemberCount:Int,
            )
{
    val barColor: Color= MaterialTheme.colorScheme.background
    LaunchedEffect(channelName, channelLogo,channelMemberCount)
    {
        mainViewModel.setTopBar(TopBarState(
            title = {ChannelTitleCard(channelName,channelLogo,channelMemberCount)},
            showBackButton = true,
            actions = {
                Row(horizontalArrangement = Arrangement.spacedBy(0.dp))
                    {
                        IconButton(onClick = {})
                            {
                                Icon(Icons.Outlined.Add,contentDescription = null)
                            }
//                        IconButton(onClick = {})
//                            {
//                                Icon(Icons.Outlined.Group,contentDescription = null)
//                            }

                        IconButton(onClick = {})
                            {
                            Icon(Icons.Outlined.Settings,contentDescription = null)
                            }
                    }
            },
            barColor = barColor
        ))
    }
}