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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.studygether.ui.theme.tokens.AppColors
import com.example.studygether.ui.theme.tokens.AppShape
import com.example.studygether.ui.theme.tokens.AppSpacing

@Composable
fun LoginScreen(onLoginSuccess:()->Unit={},
                onSignIn:()-> Unit={},
                onCreateCommunity:()->Unit={},
                onForgetPassword:()->Unit={}){
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
                    )

                    Text("Study2Gether", style = Typography.displayMedium)

                }


            }


            Row(Modifier.fillMaxSize()
                .padding(horizontal = AppSpacing.extraLarge).padding(top= AppSpacing.extraLarge),
                verticalAlignment = Alignment.Top

            )
            {
                Card(modifier = Modifier.fillMaxWidth(),
                    colors= CardDefaults.cardColors(
                        containerColor = AppColors.BrandColor,
                        contentColor =Color.Black),
                    shape = AppShape.ExtraLarge
                )
                {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.tiny),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(horizontal=AppSpacing.medium, vertical = AppSpacing.large)
                    )
                    {
                        OutlinedTextField(
                            value = loginState.email,
                            onValueChange = {loginViewModel.onEmailChange(it) },
                            label = {Text("Email")},
                            placeholder = {
                                Text("Email")
                            },
                            isError = loginState.emailError.isNotEmpty(),
                            supportingText = { Text(loginState.emailError) },
                            shape= AppShape.Large,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = loginState.password,
                            onValueChange = {loginViewModel.onPasswordChange(it) },
                            label = {Text("Password")},
                            placeholder = {
                                Text("Password")
                            },
                            isError = loginState.emailError.isNotEmpty(),
                            supportingText = { Text(loginState.passwordError, color=MaterialTheme.colorScheme.error
                                ,style=Typography.bodyLarge) },
                            shape= AppShape.Large,
                            modifier = Modifier.fillMaxWidth(),
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

                        Button(onClick={loginViewModel.onLogin()},
                            modifier = Modifier.padding(AppSpacing.none).fillMaxWidth(),
                            contentPadding = PaddingValues(AppSpacing.none),
                        )
                        {
                            Text("Login")
                        }

                        Button(onClick={onSignIn()},
                            modifier = Modifier.padding(AppSpacing.none).fillMaxWidth(),
                            contentPadding = PaddingValues(AppSpacing.none),
                        )
                        {
                            Text("SignIn")
                        }


                        Text("Forget Password?", modifier = Modifier.clickable(true, onClick = onForgetPassword),color= MaterialTheme.colorScheme.secondary)
                        Text("Create Your Community Here",modifier = Modifier.clickable(true, onClick = onCreateCommunity), color = MaterialTheme.colorScheme.secondary)




                    }
                }
            }

        }
    }
}





@Composable
fun ForgetPasswordScreen(onSendResetLink:()->Unit={},
                         onBackToLogin:()->Unit={}) {

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
                    )

                    Text("Study2Gether", style = Typography.displayMedium)

                }


            }

            Row(Modifier.fillMaxSize()
                .padding(horizontal = AppSpacing.extraLarge).padding(top= AppSpacing.extraLarge),
                verticalAlignment = Alignment.Top

            )
            {
                Card(modifier = Modifier.fillMaxWidth(),
                    colors= CardDefaults.cardColors(
                        containerColor = AppColors.BrandColor,
                        contentColor =Color.Black),
                    shape = AppShape.ExtraLarge
                )
                {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.small),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(horizontal=AppSpacing.medium, vertical = AppSpacing.large)
                    ){

                        Text(
                            text = "Forget Password",
                            style = Typography.titleLarge,

                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Enter your registered email address and we'll send you a password reset link.",
                        )

                        OutlinedTextField(
                            onValueChange = {
                                forgetPasswordViewModel.onEmailChange(it)
                            },
                            value = forgetPassworeScreenState.email,
                            shape = AppShape.Large,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            placeholder = {
                                Text(
                                    text = "Enter your email"
                                )
                            },
                            isError = forgetPassworeScreenState.emailError.isNotEmpty(),
                            supportingText = { Text(forgetPassworeScreenState.emailError, color=MaterialTheme.colorScheme.error
                                ,style=Typography.bodyLarge) },
                        )
                        ElevatedButton(
                            onClick = {forgetPasswordViewModel.onSendResendLink()
                            },
                            colors= ButtonDefaults.elevatedButtonColors(
                                containerColor =MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = "Send Reset Link",
                                color=MaterialTheme.colorScheme.onPrimary

                            )
                        }
//                        Text(
//                            text = "Login With Email verification instead?",
//                            color = MaterialTheme.colorScheme.secondary,
//                            modifier = Modifier.clickable(true, onClick = onBackToLogin)
//                        )



                        Text(
                            text = "Back to Login",
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.clickable(true, onClick = onBackToLogin)
                        )
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

