package com.example.studygether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.ui.theme.loginbg
import com.example.studygether.ui.theme.textcolor


val MyFontFamily = FontFamily(
    Font(R.font.doppioone)
)
class ForgetPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ForgetPasswordBody()
        }
    }
}

@Composable
fun ForgetPasswordBody() {

    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.white),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(75.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier.size(150.dp),
                    painter = painterResource(id = R.drawable.logoapp),
                    contentDescription = "Logo"
                )
            }

            Text(
                text = "STUDY2GETHER",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = myFontFamily
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(
                    modifier = Modifier
                        .height(380.dp)
                        .width(300.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = loginbg
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Forgot Password",
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontFamily = myFontFamily,
                                color = textcolor,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Enter your registered email address and we'll send you a password reset link.",
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Email",
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                            },
                            shape = RoundedCornerShape(25.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            placeholder = {
                                Text(
                                    text = "Enter your email",
                                    style = TextStyle(
                                        fontFamily = myFontFamily,
                                        fontSize = 15.sp
                                    )
                                )
                            },
                            colors = colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Gray.copy(alpha = 0.1f),
                                focusedContainerColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        ElevatedButton(
                            onClick = {
                            }
                        ) {
                            Text(
                                text = "Send Reset Link",
                                style = TextStyle(
                                    fontFamily = myFontFamily
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = "Back to Login",
                            color = Color.Blue,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgetPasswordPreview() {
    ForgetPasswordBody()
}