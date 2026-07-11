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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.studygether.ViewModels.SignInViewModel
import com.example.studygether.ui.theme.StudyGetherTheme
import com.example.studygether.ui.theme.AppThemeStyle
import com.example.studygether.ui.theme.tokens.AppSpacing







@Composable
fun SignInScreen(onBackToLogin: () -> Unit = {}) {
    StudyGetherTheme(dynamicColor = false, themeStyle = AppThemeStyle.DEFAULT) {
        val activity = LocalActivity.current as ComponentActivity
        val viewModel: SignInViewModel = viewModel()
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
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Welcome to Study2Gether", style = MaterialTheme.typography.headlineMedium)
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.Top
                )
                {
                    Card(modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF7E9BC5).copy(alpha = 0.95f),
                            contentColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    )
                    {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 24.dp)
                        ) {
                            OutlinedButton(
                                onClick = onBackToLogin,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color.White),
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(14.dp)
                            )
                            {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                                    contentDescription = "Back button"
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Back to login")
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            val inputColors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.15f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
                                errorContainerColor = Color.White.copy(alpha = 0.15f),
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                                errorBorderColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                                errorLabelColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                errorTextColor = Color.White,
                                focusedPlaceholderColor = Color.White.copy(alpha = 0.6f),
                                unfocusedPlaceholderColor = Color.White.copy(alpha = 0.6f),
                                focusedTrailingIconColor = Color.White,
                                unfocusedTrailingIconColor = Color.White
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
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
                                    supportingText = { if (uiState.firstNameError.isNotEmpty()) Text(uiState.firstNameError, color = Color(0xFFFFD2D2)) },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = inputColors,
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
                                    supportingText = { if (uiState.lastNameError.isNotEmpty()) Text(uiState.lastNameError, color = Color(0xFFFFD2D2)) },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = inputColors
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                            )
                            {
                                OutlinedTextField(
                                    value = uiState.email,
                                    onValueChange = { viewModel.onEmailChange(it) },
                                    label = {Text("Creator Mail")},
                                    placeholder = {
                                        Text("Enter Creator Mail")
                                    },
                                    isError = uiState.emailError.isNotEmpty(),
                                    supportingText = { if (uiState.emailError.isNotEmpty()) Text(uiState.emailError, color = Color(0xFFFFD2D2)) },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = inputColors,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
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
                                    supportingText = { if (uiState.passwordError.isNotEmpty()) Text(uiState.passwordError, color = Color(0xFFFFD2D2)) },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = inputColors,
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
                                modifier = Modifier.fillMaxWidth(),
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
                                    supportingText = { if (uiState.confirmPasswordError.isNotEmpty()) Text(uiState.confirmPasswordError, color = Color(0xFFFFD2D2)) },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = inputColors,
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

                            Spacer(modifier = Modifier.height(4.dp))

                            Button(
                                onClick = { viewModel.onRegister() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF7E9BC5)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                enabled = !uiState.isRegistering
                            )
                            {
                                Text(if(uiState.isRegistering) "Registering..." else "Register")
                            }
                        }
                    }
                }
            }
        }
    }
}



