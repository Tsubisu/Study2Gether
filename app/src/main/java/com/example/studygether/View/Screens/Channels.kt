@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.studygether.View.Screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.Model.Channel
import com.example.studygether.R
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ui.theme.MainTheme
import com.example.studygether.ui.theme.TextColor
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.tokens.AppSpacing

@Composable
fun ChannelListScreen(modifier:Modifier,
                      onNavigateToChannel:(name:String, image:Int,memberCount:Int)->Unit) {
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    LaunchedEffect(Unit)
    {
        appBarsViewModel.setTitleBar(
            title ={Text("Channels",style= Typography.headlineMedium)},
            actions = {IconButton(onClick={})
            {
                Icon(Icons.Default.Face,contentDescription = null)
            }
            }
        )

        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.NavBar))
    }
    val channel by remember{mutableStateOf(Channel(
      "Tech Support",
      R.drawable.logo,
      265))}

        Box(modifier)
        {
            Box(modifier = Modifier.fillMaxSize().background(color= MaterialTheme.colorScheme.primary)){
                LazyColumn(modifier = Modifier.fillMaxSize().background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ).padding(all= AppSpacing.small),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.medium)
                    )
                {

                    items(4)
                    {
                        ChannelCard(channel,onNavigateToChannel)
                    }


                }
            }

    }


}


fun testChannelList(): List<Channel>
{
    val channelList= ArrayList<Channel>()
    return channelList
}

@Composable
fun ChannelCard(channel: Channel,onClick:(name:String , image:Int,memberCount:Int)-> Unit)
{

    Card(Modifier.padding(vertical = AppSpacing.medium, horizontal = AppSpacing.large)
            .clickable { onClick(channel.name,channel.resourceId,channel.members) },
        colors= CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        )
    {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically)
        {
            Column(modifier = Modifier.weight(2f).padding(start = AppSpacing.large), verticalArrangement = Arrangement.spacedBy(
                AppSpacing.small), horizontalAlignment = Alignment.Start){

                Spacer(Modifier.width(AppSpacing.large))
                Text(channel.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Text("Members:${channel.members}")
            }
            Row(modifier = Modifier.weight(1f).padding(AppSpacing.medium), horizontalArrangement = Arrangement.End)
            {
                Spacer(Modifier.width(AppSpacing.large))
                Image(painter = painterResource(channel.resourceId),contentDescription = null,modifier = Modifier
                    .size(48.dp)
                    .border(1.dp, Color.Black, CircleShape)
                    .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
    HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(horizontal = AppSpacing.large))
}

