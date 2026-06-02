package com.example.studygether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.ui.theme.AccentPurple
import com.example.studygether.ui.theme.BackgroundBlue
import com.example.studygether.ui.theme.CardGreen
import com.example.studygether.ui.theme.CardPink
import com.example.studygether.ui.theme.CardPurple
import com.example.studygether.ui.theme.PurpleLight
import com.example.studygether.ui.theme.TextDark

data class Channel(
    val label: String,
    val bg: Color,
    val icon: Painter
)

data class DmUser(val name: String)
class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreenCompose()

            }
        }
    }

@Composable
fun HomeScreenCompose() {

    var selectedNav by remember { mutableIntStateOf(0) }

    val channels = listOf(
        Channel(
            "Software\nDevelopment",
            CardPink,
            painterResource(R.drawable.logo)
        ),
        Channel(
            "Mathematics",
            CardPurple,
            painterResource(R.drawable.logo)
        ),
        Channel(
            "Android\n(Kotlin)",
            CardGreen,
            painterResource(R.drawable.logo)
        ),
    )

    val dmUsers = listOf(
        DmUser("Al3rr__"),
        DmUser("tsubitsu56"),
        DmUser("Mimi__"),
        DmUser("Bobamomm"),
    )

    Scaffold(
        containerColor = BackgroundBlue,
        bottomBar = {
            BottomNavBar(
                selected = selectedNav,
                onSelect = { selectedNav = it }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Decorative top blob
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 60.dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                PurpleLight,
                                Color.Transparent
                            )
                        )
                    )
            )

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {

                Spacer(modifier = Modifier.height(52.dp))

                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = null,
                            tint = AccentPurple,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "STUDY2GETHER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentPurple,
                            letterSpacing = 1.5.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "Profile",
                            tint = AccentPurple,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Greeting
                Text(
                    text = "Hello, User!",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Search Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color.White)
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Search",
                        color = Color(0xFFAAAAAA),
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Channels Title
                Text(
                    text = "# Channels",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Channel Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    channels.forEach { channel ->

                        ChannelCard(
                            channel = channel,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "···",
                    color = Color.Gray,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 2.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = Color(0xFFDDE3F0),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // DM Header
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Direct messages",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "···",
                    color = Color.Gray,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // DM List
                dmUsers.forEach { user ->

                    DmItem(name = user.name)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ChannelCard(
    channel: Channel,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(18.dp))
            .background(channel.bg)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            Text(
                text = "···",
                color = Color.Gray,
                fontSize = 10.sp,
                lineHeight = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Icon(
            painter = channel.icon,
            contentDescription = channel.label,
            tint = Color(0xFF444455),
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "# ${channel.label}",
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333344),
            lineHeight = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DmItem(name: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E8)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                tint = Color(0xFF9090A8),
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = name,
            fontSize = 15.sp,
            color = TextDark,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun BottomNavBar(
    selected: Int,
    onSelect: (Int) -> Unit
) {

    data class NavItem(
        val label: String,
        val icon: Painter,
        val selectedIcon: Painter
    )

    val items = listOf(

        NavItem(
            "Home",
            painterResource(R.drawable.logo),
            painterResource(R.drawable.logo)
        ),

        NavItem(
            "Chats",
            painterResource(R.drawable.logo),
            painterResource(R.drawable.logo)
        ),

        NavItem(
            "Notifications",
            painterResource(R.drawable.logo),
            painterResource(R.drawable.logo)
        ),

        NavItem(
            "Settings",
            painterResource(R.drawable.logo),
            painterResource(R.drawable.logo)
        )
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            )
    ) {

        items.forEachIndexed { index, item ->

            NavigationBarItem(
                selected = selected == index,

                onClick = {
                    onSelect(index)
                },

                icon = {

                    Icon(
                        painter = if (selected == index)
                            item.selectedIcon
                        else
                            item.icon,

                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },

                label = {

                    Text(
                        text = item.label,
                        fontSize = 10.sp
                    )
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentPurple,
                    selectedTextColor = AccentPurple,
                    unselectedIconColor = Color(0xFFAAAAAA),
                    unselectedTextColor = Color(0xFFAAAAAA),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenCompose()
}