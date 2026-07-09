package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.R
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ViewModels.CommunityCreationViewModel
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ui.theme.StudyGetherTheme
import com.example.studygether.ui.theme.tokens.AppSpacing







@Composable
fun CommunityCreationScreen(onBackToLogin: () -> Unit = {}) {
    val activity = LocalActivity.current as ComponentActivity
    val viewModel: CommunityCreationViewModel = viewModel()
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by remember{ mutableStateOf(false) }
    var rePasswordVisible by remember{ mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.registrationError) {
        if (uiState.registrationError.isNotEmpty()) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    LaunchedEffect(uiState.registrationSuccess)
    {
        if (uiState.registrationSuccess) {
            onBackToLogin()
        }
    }

    LaunchedEffect(Unit) {
        appBarsViewModel.hideTopBar()

        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
    }

    





    Box(modifier = Modifier.fillMaxSize())
    {
        Image(
            painter = painterResource(id =R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
            )


        Column(modifier= Modifier.fillMaxSize().verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Row(Modifier.fillMaxWidth(),
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
            Row(Modifier.fillMaxSize().padding(horizontal = AppSpacing.extraLarge, vertical = AppSpacing.medium))
            {
                Card(modifier = Modifier.fillMaxSize(),
                    colors= CardDefaults.cardColors(
                        containerColor = Color.Transparent,
                        contentColor =Color.Black)
                    )
                {
                    Column(){
                        Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = AppSpacing.tiny).padding(bottom = 2.dp)
                    )
                    {
                        Button(onClick = onBackToLogin,
                            colors= ButtonDefaults.buttonColors
                                (
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(0.dp),
                            )
                        {
                            Icon(
                                painter = painterResource(R.drawable.baseline_arrow_back_24),
                                contentDescription = "Back button"
                            )
                            Spacer(Modifier.width(AppSpacing.medium))
                            Text("Back to login", Modifier.padding(0.dp))
                        }
                    }
                    }
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.tiny)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = AppSpacing.tiny),
                            horizontalArrangement = Arrangement.spacedBy(AppSpacing.medium)
                        )
                        {
                            OutlinedTextField(
                                value = uiState.firstName,
                                onValueChange = {viewModel.onFirstNameChange(it) },
                                label = {Text("First Name")},
                                placeholder = {
                                    Text("First Name")
                                },
                                isError = uiState.firstNameError.isNotEmpty(),
                                supportingText = { Text(uiState.firstNameError) },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = uiState.lastName,
                                onValueChange = { viewModel.onLastNameChange(it) },
                                label = {Text("Last Name")},
                                placeholder = {
                                    Text("Last Name")
                                },
                                modifier = Modifier.weight(1f),
                                isError = uiState.lastNameError.isNotEmpty(),
                                supportingText = { Text(uiState.lastNameError)},

                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal=AppSpacing.tiny),
                        )
                        {

                            OutlinedTextField(
                                value = uiState.communityName,
                                onValueChange = { viewModel.onCommunityNameChange(it)  },
                                label = {Text("Community Name")},
                                placeholder = {
                                    Text("Enter Community Name")
                                },
                                isError = uiState.communityNameError.isNotEmpty(),
                                supportingText = { Text(uiState.communityNameError) },
                                modifier = Modifier.weight(1f)
                            )


                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal=AppSpacing.tiny),
                        )
                        {
                            OutlinedTextField(
                                value =uiState.email,
                                onValueChange = { viewModel.onEmailChange(it) },
                                label = {Text("Creator Mail")},
                                placeholder = {
                                    Text("Enter Creator Mail")
                                },
                                isError = uiState.emailError.isNotEmpty(),
                                supportingText = { Text(uiState.emailError) },
                                modifier = Modifier.weight(1f),

                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal=AppSpacing.tiny),
                        )
                        {
                            OutlinedTextField(
                                value = uiState.password,
                                onValueChange = { viewModel.onPasswordChange(it)},
                                label = {Text("Password")},
                                placeholder = {
                                    Text("Enter Strong Password")
                                },
                                isError = uiState.passwordError.isNotEmpty(),
                                supportingText = { Text(uiState.passwordError) },
                                modifier = Modifier.weight(1f),
                                visualTransformation = if (passwordVisible) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Filled.Visibility
                                            else Icons.Filled.VisibilityOff,
                                            contentDescription = if (passwordVisible) "Hide password"
                                            else "Show password"
                                        )
                                    }
                                }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal=AppSpacing.tiny),
                        )
                        {
                            OutlinedTextField(
                                value = uiState.confirmPassword,
                                onValueChange = { viewModel.onConfirmPasswordChange(it)},
                                label = {Text("Confirm Password")},
                                placeholder = {
                                    Text("Confirm your Password")
                                },
                                isError = uiState.confirmPasswordError.isNotEmpty(),
                                supportingText = { Text(uiState.confirmPasswordError) },
                                modifier = Modifier.weight(1f),
                                visualTransformation = if (rePasswordVisible) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { rePasswordVisible = !rePasswordVisible }) {
                                        Icon(
                                            imageVector = if (rePasswordVisible) Icons.Filled.Visibility
                                            else Icons.Filled.VisibilityOff,
                                            contentDescription = if (passwordVisible) "Hide password"
                                            else "Show password"
                                        )
                                    }
                                }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal=AppSpacing.tiny),
                            horizontalArrangement = Arrangement.Center
                        )
                        {

                            Button(onClick = {viewModel.onRegister()},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,),
                                enabled = !uiState.isRegistering

                                )
                            {
                                Text(if(uiState.isRegistering)
                                  "Registering..."
                                else
                                    "Register"
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal=AppSpacing.tiny),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            if(uiState.registrationError.isNotEmpty())
                            {
                                Text(uiState.registrationError,color=MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center)
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
    StudyGetherTheme(false,false) {
        CommunityCreationScreen()
    }
}

