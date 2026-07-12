package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.App.SessionState
import com.example.studygether.R
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ViewModels.ForgetPasswordScreenViewModel
import com.example.studygether.ViewModels.LoginScreenViewModel
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.StudyGetherTheme
import com.example.studygether.ui.theme.AppThemeStyle
import com.example.studygether.ui.theme.tokens.AppColors
import com.example.studygether.ui.theme.tokens.AppShape
import com.example.studygether.ui.theme.tokens.AppSpacing

@Composable
fun LoginScreen(onLoginSuccess:()->Unit={},
                onSignIn:()-> Unit={},
                onCreateCommunity:()->Unit={},
                onForgetPassword:()->Unit={}){
    StudyGetherTheme(dynamicColor = false, themeStyle = AppThemeStyle.DEFAULT) {
        val activity = LocalActivity.current as ComponentActivity
        val loginViewModel: LoginScreenViewModel = viewModel()
        val appBarsViewModel: AppBarsViewModel = viewModel(activity)
        val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
        var passwordVisible by remember {mutableStateOf(false)}

        LaunchedEffect(Unit)
        {
            appBarsViewModel.hideTopBar()

            appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
        }
        val isLoggedIn by SessionState.isLoggedIn.collectAsStateWithLifecycle()

        LaunchedEffect(isLoggedIn)
        {

            if(isLoggedIn)
            onLoginSuccess()
        }



        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
            Column(modifier = Modifier.fillMaxSize())
            {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = AppSpacing.massive)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Study2Gether", style = MaterialTheme.typography.headlineMedium)

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
                        )
                        {
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

                            OutlinedTextField(
                                value = loginState.email,
                                onValueChange = {loginViewModel.onEmailChange(it) },
                                label = {Text("Email")},
                                placeholder = {
                                    Text("Email")
                                },
                                isError = loginState.emailError.isNotEmpty(),
                                supportingText = { if (loginState.emailError.isNotEmpty()) Text(loginState.emailError, color = Color(0xFFFFD2D2)) },
                                shape = RoundedCornerShape(14.dp),
                                colors = inputColors,
                                modifier = Modifier.fillMaxWidth().testTag("email_input")
                            )

                            OutlinedTextField(
                                value = loginState.password,
                                onValueChange = {loginViewModel.onPasswordChange(it) },
                                label = {Text("Password")},
                                placeholder = {
                                    Text("Password")
                                },
                                isError = loginState.passwordError.isNotEmpty(),
                                supportingText = { if (loginState.passwordError.isNotEmpty()) Text(loginState.passwordError, color = Color(0xFFFFD2D2)) },
                                shape = RoundedCornerShape(14.dp),
                                colors = inputColors,
                                modifier = Modifier.fillMaxWidth().testTag("password_input"),
                                visualTransformation = if (passwordVisible) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(
                                        onClick = { passwordVisible = !passwordVisible },
                                        modifier = Modifier.testTag("password_toggle")
                                    ) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Filled.Visibility
                                            else Icons.Filled.VisibilityOff,
                                            contentDescription = if (passwordVisible) "Hide password"
                                            else "Show password"
                                        )
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { loginViewModel.onLogin() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF7E9BC5)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("login_button")
                            )
                            {
                                Text("Login")
                            }

                            OutlinedButton(
                                onClick = { onSignIn() },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color.White),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("sign_in_button")
                            )
                            {
                                Text("Sign In")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            TextButton(onClick = onForgetPassword) {
                                Text("Forget Password?", style = Typography.bodyMedium, color = Color.White)
                            }

                            TextButton(onClick = onCreateCommunity) {
                                Text("Create Your Community Here", style = Typography.bodyMedium, color = Color.White)
                            }
                        }
                    }
                }

            }

            if (loginState.isLoading) {
                com.example.studygether.App.SplashScreen(message = "Logging in...")
            }
        }
    }
}





@Composable
fun ForgetPasswordScreen(onSendResetLink:()->Unit={},
                         onBackToLogin:()->Unit={}) {
    StudyGetherTheme(dynamicColor = false, themeStyle = AppThemeStyle.DEFAULT) {
        val forgetPasswordViewModel : ForgetPasswordScreenViewModel =viewModel()
        val forgetPassworeScreenState by forgetPasswordViewModel.forgetPasswordState.collectAsStateWithLifecycle()

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
            Column(modifier = Modifier.fillMaxSize())
            {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = AppSpacing.massive)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Study2Gether", style = MaterialTheme.typography.headlineMedium)

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
                        ){

                            Text(
                                text = "Forget Password",
                                style = Typography.titleLarge,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Enter your registered email address and we'll send you a password reset link.",
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )

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

                            OutlinedTextField(
                                onValueChange = {
                                    forgetPasswordViewModel.onEmailChange(it)
                                },
                                value = forgetPassworeScreenState.email,
                                shape = RoundedCornerShape(14.dp),
                                colors = inputColors,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                placeholder = {
                                    Text(
                                        text = "Enter your email"
                                    )
                                },
                                isError = forgetPassworeScreenState.emailError.isNotEmpty(),
                                supportingText = { if (forgetPassworeScreenState.emailError.isNotEmpty()) Text(forgetPassworeScreenState.emailError, color=Color(0xFFFFD2D2)) },
                            )
                            Button(
                                onClick = { forgetPasswordViewModel.onSendResendLink() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF7E9BC5)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text(
                                    text = "Send Reset Link"
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            TextButton(onClick = onBackToLogin) {
                                Text("Back to Login", color = Color.White, style = Typography.bodyMedium)
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
fun Preview(){
     LoginScreen()
    //ForgetPasswordScreen()
}

