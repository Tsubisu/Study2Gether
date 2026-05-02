package com.example.studygether

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.ui.theme.Topic
import com.example.studygether.ui.theme.loginbg
import com.example.studygether.ui.theme.input
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.studygether.ui.theme.Typography

val myFontFamily = FontFamily(
    Font(R.font.doppioone)
)

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           LoginBody()
           }        }
    }


@Composable
fun LoginBody(){
    var email by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    Column(modifier = Modifier.fillMaxSize())
    {   Spacer(modifier = Modifier.height(height = 75.dp))
        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()) {
            Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(R.drawable.study2gether),
                contentDescription = "Logo",
            )
        }
        Text(text = "STUDY2GETHER", style = TextStyle(
            textAlign = TextAlign.Center,
            color = Topic,
            fontSize = 29.sp,
            fontWeight = FontWeight(400),
            fontFamily = myFontFamily
        ),
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )
        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
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
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Text(text = "LOGIN", style = TextStyle(
                        fontSize = 32.sp,
                        fontFamily = myFontFamily,
                        color = Color(183,183,183),
                        textAlign = TextAlign.Center
                    ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Email")
                    OutlinedTextField(
                        value = email,
                                onValueChange = {
                            email = it
                        },
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        placeholder = {
                            Text("Enter your Email", style = TextStyle(
                                fontFamily = myFontFamily,
                                fontSize = 24.sp
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
                    Text("Password")
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        placeholder = {
                            Text("* * * * * * * *", style = TextStyle(
                                textAlign = TextAlign.Center,
                                fontFamily = myFontFamily,
                                fontSize = 24.sp,
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
                }
            }
        }}
}
}
@Preview
@Composable
fun LoginPreview(){
    LoginBody()
}
