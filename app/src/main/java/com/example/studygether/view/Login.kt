package com.example.studygether.view

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.example.studygether.R
import com.example.studygether.ui.theme.Topic
import com.example.studygether.ui.theme.input
import com.example.studygether.ui.theme.loginbg
import com.example.studygether.ui.theme.loginbutton
import com.example.studygether.ui.theme.myFontFamily

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           LoginBody()
           }
    }

}


@Composable
fun LoginBody(){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.login),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    Column(modifier = Modifier.fillMaxSize())
    {
        Spacer(modifier = Modifier.height(height = 75.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(R.drawable.applogo),
                contentDescription = "Logo",
            )
        }
        Text(
            text = "STUDY2GETHER", style = TextStyle(
                textAlign = TextAlign.Center,
                color = Topic,
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
                    .height(450.dp)
                    .width(300.dp),
                RoundedCornerShape(20),
                colors = CardDefaults.cardColors(
                    containerColor = loginbg
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(5.dp),
//                    verticalArrangement = Arrangement.SpaceEvenly,
//                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "LOGIN", style = TextStyle(
                            fontSize = 32.sp,
                            fontFamily = myFontFamily,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W400
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        "Email", style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        placeholder = {
                            Text(
                                "Enter your Email", style = TextStyle(
                                    fontFamily = myFontFamily,
                                    fontSize = 24.sp,
                                    color = Color.Black
                                )
                            )
                        },
                        colors = TextFieldDefaults.colors(

                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = input,
                            focusedIndicatorColor = Color.Gray.copy(alpha = 0.1f),
                            focusedContainerColor = Color.Blue,
                        )
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        "Password", style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            email = it
                        },
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        placeholder = {
                            Text(
                                "* * * * * * * *",
                                style = TextStyle(
                                    textAlign = TextAlign.Center,
                                    fontFamily = myFontFamily,
                                    fontSize = 24.sp,
                                    color = Color.Black
    
                                ),
                            )
                        },
                        colors = TextFieldDefaults.colors(

                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = input,
                            focusedIndicatorColor = Color.Gray.copy(alpha = 0.1f),
                            focusedContainerColor = Color.Blue,
                        )
                    )

                    Row(modifier = Modifier.padding(10.dp)) {
                        Text(
                            "Forget Password",
                            modifier = Modifier.clickable{
//                                val intent = Intent(context,
//                                    ForgotPassword::class.java)
//                                context.startActivity(intent)
//                                activity.finish()
                            }
                        )
                    }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(5.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    val sharedPreferences = context.getSharedPreferences(
                                        "User",
                                        Context.MODE_PRIVATE
                                    )


                                    val emailStorage: String? = sharedPreferences.getString("email", "")
                                    val passwordStorage: String? = sharedPreferences.getString("password", "")

                                    if (email == emailStorage && password == passwordStorage) {
                                        Toast.makeText(
                                            context,
                                            "Login success",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        val editor = sharedPreferences.edit()

                                        editor.putBoolean("isLoggedIn", true)

//                                        val intent = Intent(context, NaviigationActivity::class.java)
//                                        context.startActivity(intent)
//                                        activity.finish()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Login failed",
                                            Toast.LENGTH_LONG
                                        ).show()

                                    }
                                },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = loginbutton
                                )
                            ) {
                                Text(
                                    "Login", style = TextStyle(
                                        fontFamily = myFontFamily,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                    }
                }
        }
    }
    }
}
@Preview
@Composable
fun LoginPreview(){
    LoginBody()
}
