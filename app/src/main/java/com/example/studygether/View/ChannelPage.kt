package com.example.studygether.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.R
import com.example.studygether.ui.theme.CardColour
import com.example.studygether.ui.theme.MainTheme
import com.example.studygether.ui.theme.TextColor

class ChannelPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChannelBody()
        }
    }
}

@Composable
fun ChannelBody() {
    var isJoined by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    val context = LocalContext.current
    var selectedItem by remember { mutableIntStateOf(0) }

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
                                tint = if (selectedItem == index) Color.White else Color.White.copy(alpha = 0.6f)
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                color = if (selectedItem == index) Color.White else Color.White.copy(alpha = 0.6f),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .height(100.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 30.dp),
                    colors = CardDefaults.cardColors(containerColor = MainTheme),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        verticalArrangement = Arrangement.Center
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "  STUDY2GETHER",
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = TextColor,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardColour),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(60.dp).clip(CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                                        contentDescription = "Profile",
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Is Java an Interpreter or a Compiler?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                                    Text("Subash Gurung", fontSize = 16.sp, color = Color.Gray)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(text = "", fontSize = 17.sp, lineHeight = 26.sp, color = Color.Black)
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardColour),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(60.dp).clip(CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                                        contentDescription = "Profile",
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Is Java an Interpreter or a Compiler?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                                    Text("Subash Gurung", fontSize = 16.sp, color = Color.Gray)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(text = "", fontSize = 17.sp, lineHeight = 26.sp, color = Color.Black)
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardColour),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(60.dp).clip(CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                                        contentDescription = "Profile",
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Is Java an Interpreter or a Compiler?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                                    Text("Subash Gurung", fontSize = 16.sp, color = Color.Gray)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(text = "", fontSize = 17.sp, lineHeight = 26.sp, color = Color.Black)
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardColour),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(60.dp).clip(CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                                        contentDescription = "Profile",
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Is Java an Interpreter or a Compiler?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                                    Text("Subash Gurung", fontSize = 16.sp, color = Color.Gray)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(text = "", fontSize = 17.sp, lineHeight = 26.sp, color = Color.Black)
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardColour),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(60.dp).clip(CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                                        contentDescription = "Profile",
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Is Java an Interpreter or a Compiler?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                                    Text("Subash Gurung", fontSize = 16.sp, color = Color.Gray)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(text = "", fontSize = 17.sp, lineHeight = 26.sp, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChannelPagePreview() {
    ChannelBody()
}