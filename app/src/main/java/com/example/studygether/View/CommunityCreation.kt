package com.example.studygether.View

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.R


class CommunityCreation : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CommunityCreationPage()
        }
    }
}

@Composable
fun CommunityCreationPage()
{
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var communityName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize())
    {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
            )

        Column(modifier= Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Row(Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment =Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                    )

                    Text("Get Started", fontSize = 30.sp)
                    Text("Create Your Community", fontSize = 30.sp)


                }

            }


            Row(Modifier.fillMaxWidth().weight(2f).padding(start = 30.dp, end=30.dp,bottom=100.dp,top=20.dp))
            {
                Card(modifier = Modifier.fillMaxSize(),
                    colors= CardColors(
                        containerColor = Color(0xffd1e8fc),
                        contentColor =Color.Red,
                        disabledContainerColor =Color.Green,
                        disabledContentColor =Color(0xffC5E0FD)
                    )
                    )
                {
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        )
                        {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = {Text("First Name")},
                                placeholder = {
                                    Text("First Name")
                                },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = {Text("Last Name")},
                                placeholder = {
                                    Text("Last Name")
                                },
                                modifier = Modifier.weight(1f)

                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                        )
                        {
                            OutlinedTextField(
                                value = communityName,
                                onValueChange = { communityName = it },
                                label = {Text("First Name")},
                                placeholder = {
                                    Text("Enter Community Name")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                        )
                        {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email= it },
                                label = {Text("Creator Mail")},
                                placeholder = {
                                    Text("Enter Creator Mail")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                        )
                        {
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = {Text("Password")},
                                placeholder = {
                                    Text("Enter Strong Password")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                        )
                        {
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = {Text("RePassword")},
                                placeholder = {
                                    Text("Confirm your Password")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            horizontalArrangement = Arrangement.Center
                        )
                        {
                            Icon(
                                painterResource(R.drawable.logo),
                                contentDescription =null

                            )
                            ElevatedButton(onClick = {})
                            {
                                Text("Register")
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
fun preview()
{
    CommunityCreationPage()
}

