package com.example.studygether

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.ViewModel.AuthViewModel
import com.example.studygether.ui.theme.black
import com.example.studygether.ui.theme.loginbg

val myFontFamily = FontFamily(
    Font(R.font.doppioone)
)

class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResetPasswordBody()
        }
    }
}

@Composable
fun ResetPasswordBody(viewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val resetState by viewModel.resetPasswordState

    LaunchedEffect(resetState) {
        resetState?.let { result ->
            if (result.isSuccess) {
                Toast.makeText(context, "Password reset email sent!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.white),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(75.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier.size(150.dp),
                    painter = painterResource(R.drawable.logoapp),
                    contentDescription = "Logo",
                )
            }
            Text(
                text = "STUDY2GETHER",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 29.sp,
                    fontWeight = FontWeight(400),
                    fontFamily = myFontFamily
                ),
                modifier = Modifier.fillMaxWidth().padding(5.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .height(350.dp)
                        .width(300.dp),
                    shape = RoundedCornerShape(20),
                    colors = CardDefaults.cardColors(
                        containerColor = loginbg,
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Reset Password",
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontFamily = myFontFamily,
                                color = black,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = "Email",
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
                            style = TextStyle(fontFamily = myFontFamily)
                        )
                        
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            shape = RoundedCornerShape(25.dp),
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            placeholder = {
                                Text(
                                    "Enter your email",
                                    style = TextStyle(fontFamily = myFontFamily, fontSize = 15.sp)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Gray.copy(alpha = 0.1f),
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        ElevatedButton(
                            onClick = {
                                if (email.isNotEmpty()) {
                                    viewModel.resetPassword(email)
                                } else {
                                    Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                        ) {
                            Text(
                                "Send Reset Email",
                                style = TextStyle(fontFamily = myFontFamily)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ResetPasswordPreview() {
    ResetPasswordBody()
}
