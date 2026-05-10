package com.example.studygether.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.R
import com.example.studygether.ui.theme.MainTheme
import com.example.studygether.ui.theme.TextColor

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
fun ProfileBody(){
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
                Column (
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
                    )}
                Row (
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("PROFILE", style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = TextColor.copy(0.7f)
                    ))
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
                        "STUDY2GETHER", style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Left,
                            color = Color.Black.copy(0.5f)
                        ),
                        modifier = Modifier.padding(5.dp)
                    )
                        Text("Study2Gether@gmail.com",style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Left,
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
            Text("    Account", style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                textAlign = TextAlign.Start,
                color = Color.Gray
            ))
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
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
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
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
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
            Text("    Preferences", style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                textAlign = TextAlign.Start,
                color = Color.Gray
            ))
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
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
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
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
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
            modifier = Modifier.height(100.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .height(100.dp).weight(1f)
                    .border(0.dp, color = Color.Transparent, RoundedCornerShape(0.dp)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 30.dp
                ),
                colors = CardDefaults.cardColors(containerColor = MainTheme),

                ) {
                Spacer(
                    modifier= Modifier.height(40.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column() {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(90.dp),
                                painter = painterResource(R.drawable.baseline_home_24),
                                contentDescription = "Account profile"
                            )
                        }
                        Text(text = "Home", style = TextStyle(
                            fontSize = 15.sp,
                            color = TextColor.copy(0.7f),
                        ))
                    }
                    Column() {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(90.dp),
                                painter = painterResource(R.drawable.baseline_chat_24),
                                contentDescription = "Account profile"
                            )
                        }
                        Text(text = "Chats", style = TextStyle(
                            fontSize = 15.sp,
                            color = TextColor.copy(0.7f),
                        ))
                    }
                    Column() {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(90.dp),
                                painter = painterResource(R.drawable.baseline_notifications_24),
                                contentDescription = "Account profile"
                            )
                        }
                        Text(text = "Notifications", style = TextStyle(
                            fontSize = 15.sp,
                            color = TextColor.copy(0.7f),
                        ))
                    }
                    Column() {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(90.dp),
                                painter = painterResource(R.drawable.baseline_settings_24),
                                contentDescription = "Account profile"
                            )
                        }
                        Text(text = "Settings", style = TextStyle(
                            fontSize = 15.sp,
                            color =TextColor.copy(0.7f),
                        ))
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