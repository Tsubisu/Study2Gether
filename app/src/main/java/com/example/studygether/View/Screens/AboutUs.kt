package com.example.studygether.View.Screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.R
import com.example.studygether.ui.theme.TextColor




@Composable
fun AboutUsBody(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                ElevatedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(45.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(0.dp),
                    elevation =ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Image(
                painter = painterResource(id = R.drawable.applogo),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "About Us",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    
                    color = TextColor,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Study2Gether is where students stop suffering alone. Post your questions, get answers from people who actually understand the struggle, and chat with classmates all in one place. No randoms, just your university community.",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    
                    lineHeight = 24.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Justify
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "What we offer:",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    
                    color = TextColor,
                    textDecoration = TextDecoration.Underline

                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text =  "• Interactive Chats: Connect with peers instantly to discuss topics and clear doubts.\n\n" +
                        "• Resource Sharing: Upload and access study materials, notes, and previous papers.\n\n" +
                        "• Study Groups: Create or join groups tailored to your specific subjects and interests.",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    lineHeight = 24.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Justify
            )


        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutUsPreview() {
    AboutUsBody(onNavigateBack = {})

}