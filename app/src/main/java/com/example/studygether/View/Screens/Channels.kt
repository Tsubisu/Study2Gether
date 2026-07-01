@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.studygether.View.Screens



import android.os.Bundle
import androidx.activity.ComponentActivity
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
    val appBarsViewModel: AppBarsViewModel = viewModel()
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

class ProfilePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileBody()
        }
    }
}

@Composable
fun ProfileBody() {
    var selectedItem by remember { mutableIntStateOf(0) }
    var usernameText by remember { mutableStateOf("Loading...") }
    var emailText by remember { mutableStateOf("Loading...") }

//    val profileViewModel = remember { ProfileViewModel(repo = ProfileImplementation()) }
//    val profileData by profileViewModel.userProfile.observeAsState(initial = null)
//    val loading by profileViewModel.loading.observeAsState(initial = null)
//    val userId = profileViewModel.currentUserId
//
//    LaunchedEffect(key1 = profileData) {
//        if (userId != null) {
//            profileViewModel.getUserProfile(userId)
//        }
//        profileData?.let {
//            usernameText = it.username
//            emailText = it.email
//        }
//    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(
                containerColor = MainTheme,
                contentColor = Color.White,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                val items = listOf(
                    Pair("Home", R.drawable.baseline_home_24),
                    Pair("Chats", R.drawable.baseline_chat_24),
                    Pair("Notifications", R.drawable.baseline_notifications_24),
                    Pair("Settings", R.drawable.baseline_settings_24),
                )
                items.forEachIndexed { index, (label, icon) ->
                    NavigationBarItem(
                        modifier = Modifier.background(color = MainTheme),
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = {
                            Icon(
                                painter = painterResource(icon),
                                contentDescription = label,
                                tint = if (selectedItem == index) Color.White
                                else Color.White.copy(alpha = 0.6f)
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                color = if (selectedItem == index) Color.White
                                else Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .height(100.dp).weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 30.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = MainTheme),
                    ) {
                        Column(
                            modifier = Modifier
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Top
                            ) {
                                Image(
                                    modifier = Modifier.size(50.dp),
                                    painter = painterResource(R.drawable.applogo),
                                    contentDescription = "App Logo"
                                )
                            }
                            Row(
                                modifier = Modifier,
                            ) {
                                Text(
                                    "  STUDY2GETHER", style = TextStyle(
                                        fontWeight = FontWeight.Companion.SemiBold,
                                        fontSize = 11.sp,
                                        color = TextColor,
                                        textAlign = TextAlign.Companion.Center
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "PROFILE", style = TextStyle(
                                        fontWeight = FontWeight.Companion.ExtraBold,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Companion.Center,
                                        color = TextColor.copy(0.7f)
                                    )
                                )
                            }
                        }
                    }
                }
                Spacer(
                    modifier = Modifier.height(5.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .height(100.dp).weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 30.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.Absolute.Left
                        ) {
                            Image(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(80.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Fit,
                                painter = painterResource(R.drawable.profilepfp),
                                contentDescription = "profile picture"
                            )
                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .height(200.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = usernameText, style = TextStyle(
                                        fontWeight = FontWeight.Companion.Bold,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Companion.Left,
                                        color = Color.Black.copy(0.5f)
                                    ),
                                    modifier = Modifier.padding(5.dp)
                                )
                                Text(
                                    text = emailText, style = TextStyle(
                                        fontWeight = FontWeight.Companion.SemiBold,
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.Companion.Left,
                                        color = Color.Black.copy(0.5f)
                                    ),
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(
                    modifier = Modifier.height(5.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "    Account", style = TextStyle(
                            fontWeight = FontWeight.Companion.SemiBold,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Companion.Start,
                            color = Color.Gray
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .height(180.dp).weight(1f)
                            .padding(5.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 30.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(20.dp),
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(R.drawable.baseline_account_circle_24),
                                contentDescription = "Account profile"
                            )
                            Spacer(
                                modifier = Modifier.width(15.dp)
                            )
                            Text(
                                "   Change Username", style = TextStyle(
                                    fontWeight = FontWeight.Companion.SemiBold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Companion.Center,
                                    color = Color.Gray
                                )
                            )
                            Spacer(
                                modifier = Modifier.width(90.dp)
                            )
                            IconButton(
                                onClick = {},
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(90.dp),
                                    painter = painterResource(R.drawable.baseline_navigate_next_24),
                                    contentDescription = "Account profile"
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                thickness = 2.dp,
                                color = Color.Gray.copy(alpha = 0.4f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(20.dp),
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(R.drawable.baseline_lock_24),
                                contentDescription = "Account profile"
                            )
                            Spacer(
                                modifier = Modifier.width(15.dp)
                            )
                            Text(
                                "   Password & Security", style = TextStyle(
                                    fontWeight = FontWeight.Companion.SemiBold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Companion.Center,
                                    color = Color.Gray
                                )
                            )
                            Spacer(
                                modifier = Modifier.width(70.dp)
                            )
                            IconButton(
                                onClick = {},
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(90.dp),
                                    painter = painterResource(R.drawable.baseline_navigate_next_24),
                                    contentDescription = "Account profile"
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                thickness = 2.dp,
                                color = Color.Gray.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier.height(5.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "    Preferences", style = TextStyle(
                            fontWeight = FontWeight.Companion.SemiBold,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Companion.Start,
                            color = Color.Gray
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .height(180.dp).weight(1f)
                            .padding(5.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 30.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(20.dp),
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(R.drawable.baseline_people_24),
                                contentDescription = "Account profile"
                            )
                            Spacer(
                                modifier = Modifier.width(15.dp)
                            )
                            Text(
                                "   About Us", style = TextStyle(
                                    fontWeight = FontWeight.Companion.SemiBold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Companion.Center,
                                    color = Color.Gray
                                )
                            )
                            Spacer(
                                modifier = Modifier.width(160.dp)
                            )
                            IconButton(
                                onClick = {},
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(90.dp),
                                    painter = painterResource(R.drawable.baseline_navigate_next_24),
                                    contentDescription = "Account profile"
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                thickness = 2.dp,
                                color = Color.Gray.copy(alpha = 0.4f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(20.dp),
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(R.drawable.baseline_color_lens_24),
                                contentDescription = "Account profile"
                            )
                            Spacer(
                                modifier = Modifier.width(20.dp)
                            )
                            Text(
                                "   Themes & Colours", style = TextStyle(
                                    fontWeight = FontWeight.Companion.SemiBold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Companion.Center,
                                    color = Color.Gray
                                )
                            )
                            Spacer(
                                modifier = Modifier.width(70.dp)
                            )
                            IconButton(
                                onClick = {},
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(90.dp),
                                    painter = painterResource(R.drawable.baseline_navigate_next_24),
                                    contentDescription = "Account profile"
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                thickness = 2.dp,
                                color = Color.Gray.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileBody()
}