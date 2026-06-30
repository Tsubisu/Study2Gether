package com.example.studygether.View

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.studygether.R
import com.example.studygether.ui.theme.CardColour
import com.example.studygether.ui.theme.MainTheme
import com.example.studygether.ui.theme.TextColor
import com.example.studygether.ViewModels.ChannelViewModel

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
        var Search by remember { mutableStateOf("") }
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
        ) {}
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
                .padding(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardColour
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape) ,
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

                                Text(
                                    text = "Is Java an Interpreter or a Compiler?",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )

                                Text(
                                    text = "Subash Gurung",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "",
                            fontSize = 17.sp,
                            lineHeight = 26.sp,
                            color = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardColour
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape) ,
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

                                Text(
                                    text = "Is Java an Interpreter or a Compiler?",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )

                                Text(
                                    text = "Subash Gurung",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "",
                            fontSize = 17.sp,
                            lineHeight = 26.sp,
                            color = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardColour
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape) ,
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

                                Text(
                                    text = "Is Java an Interpreter or a Compiler?",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )

                                Text(
                                    text = "Subash Gurung",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "",
                            fontSize = 17.sp,
                            lineHeight = 26.sp,
                            color = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardColour
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape) ,
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

                                Text(
                                    text = "Is Java an Interpreter or a Compiler?",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )

                                Text(
                                    text = "Subash Gurung",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "",
                            fontSize = 17.sp,
                            lineHeight = 26.sp,
                            color = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardColour
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape) ,
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

                                Text(
                                    text = "Is Java an Interpreter or a Compiler?",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )

                                Text(
                                    text = "Subash Gurung",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "",
                            fontSize = 17.sp,
                            lineHeight = 26.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
                }
            }
        }}
@Preview(showBackground = true)
@Composable
fun ChannelPreview() {
    ChannelBody()
}