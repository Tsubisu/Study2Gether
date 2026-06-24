package com.example.studygether.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.R
import com.example.studygether.model.ProfileModel
import com.example.studygether.repository.ProfileImplementation
import com.example.studygether.repository.ProfileRepo
import com.example.studygether.viewModel.ProfileViewModel
import com.example.studygether.ui.theme.CardColour
import com.example.studygether.ui.theme.MainTheme
import com.example.studygether.ui.theme.TextColor
import androidx.compose.runtime.LaunchedEffect
import android.widget.Toast

class ChangeUsername : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val profileViewModel = ProfileViewModel(repo = ProfileImplementation())
            ChangeUsernameBody(viewModel = profileViewModel)
        }
    }
}

@Composable
fun ChangeUsernameBody(viewModel: ProfileViewModel? = null) {
    var username by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val profileViewModel = if (isPreview) {
        ProfileViewModel(repo = object : ProfileRepo {
            override fun getUserProfile(id: String, callback: (Boolean, String, ProfileModel?) -> Unit) {
                callback(true, "Mock Success", ProfileModel(username = "MockUser"))
            }
            override fun updateUsername(id: String, newUsername: String, callback: (Boolean, String) -> Unit) {
                callback(true, "Mock Success")
            }
            override fun getCurrentId(): String? = "mock_id"
        })
    } else {
        viewModel ?: ProfileViewModel(repo = ProfileImplementation())
    }

    val profileData by profileViewModel.userProfile.observeAsState(initial = null)
    val isLoading by profileViewModel.loading.observeAsState(initial = false)
    val userId = profileViewModel.currentUserId

    LaunchedEffect(userId) {
        if (userId != null) {
            profileViewModel.getUserProfile(userId)
        }
    }

    LaunchedEffect(profileData) {
        profileData?.let {
            username = it.username
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                                fontSize = 11.sp,
                                fontFamily = myFontFamily
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ElevatedButton(
                onClick = { (context as? android.app.Activity)?.finish() },
                modifier = Modifier.size(45.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.White),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

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
                        modifier = Modifier.padding(8.dp)
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
                        Text(
                            "  STUDY2GETHER", style = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                color = TextColor,
                                textAlign = TextAlign.Center,
                                fontFamily = myFontFamily
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(600.dp),
                shape = RoundedCornerShape(30.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 80.dp),
                colors = CardDefaults.cardColors(containerColor = CardColour)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Change Username", style = TextStyle(
                            color = TextColor,
                            fontWeight = FontWeight.W700,
                            fontSize = 28.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = myFontFamily
                        ),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Current Username",
                        style = TextStyle(color = Color.Black.copy(0.5f), fontFamily = myFontFamily),
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { visibility = !visibility }) {
                                Icon(
                                    painter = if (visibility) painterResource(R.drawable.baseline_visibility_24) else painterResource(
                                        R.drawable.baseline_visibility_off_24
                                    ),
                                    contentDescription = null
                                )
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 15.dp, start = 10.dp, end = 10.dp),
                        placeholder = { Text("", fontFamily = myFontFamily) },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Gray,
                            unfocusedContainerColor = CardColour.copy(0.5f),
                            focusedContainerColor = Color.Gray.copy(0.2f),
                            focusedIndicatorColor = MainTheme
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "New Username",
                        style = TextStyle(color = Color.Black.copy(0.5f), fontFamily = myFontFamily),
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    OutlinedTextField(
                        value = confirm,
                        onValueChange = { confirm = it },
                        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { visibility = !visibility }) {
                                Icon(
                                    painter = if (visibility) painterResource(R.drawable.baseline_visibility_24) else painterResource(
                                        R.drawable.baseline_visibility_off_24
                                    ),
                                    contentDescription = null
                                )
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                        placeholder = { Text("", fontFamily = myFontFamily) },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Gray,
                            unfocusedContainerColor = CardColour.copy(0.5f),
                            focusedContainerColor = Color.Gray.copy(0.2f),
                            focusedIndicatorColor = MainTheme
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ElevatedButton(
                        modifier = Modifier.padding(5.dp)
                            .fillMaxWidth(0.6f)
                            .height(50.dp),
                        onClick = {
                            if (confirm.isNotEmpty()) {
                                if (userId != null) {
                                    profileViewModel.updateUsername(userId, confirm) { success, message ->
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                        if (success) {
                                            (context as? android.app.Activity)?.finish()
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Please enter a new username", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(MainTheme.copy(0.4f)),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                "Save", style = TextStyle(
                                    fontSize = 17.sp,
                                    color = TextColor,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = myFontFamily
                                )
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
fun UsernamePreview() {
    ChangeUsernameBody()
}