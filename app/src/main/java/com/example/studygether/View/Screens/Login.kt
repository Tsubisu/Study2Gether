package com.example.studygether.View.Screens


import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
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
import com.example.studygether.ViewModels.LoginViewModel
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ui.theme.MainTheme
import com.example.studygether.ui.theme.StudyGetherTheme
import com.example.studygether.ui.theme.TextColor
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.tokens.AppColors
import com.example.studygether.ui.theme.tokens.AppShape
import com.example.studygether.ui.theme.tokens.AppSpacing

@Composable
fun LoginScreen(loginViewModel: LoginViewModel= viewModel(),
                appBarsViewModel: AppBarsViewModel= viewModel(),
                onLogin:()->Unit={},
                onCreateCommunity:()->Unit={},
                onForgetPassword:()->Unit={}){
    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
    var passwordVisible by remember {mutableStateOf(false)}

    LaunchedEffect(Unit)
    {
        appBarsViewModel.hideTopBar()

        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
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
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.small),
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
                            supportingText = { Text(loginState.emailError) },
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

                        Button(onClick=onLogin,
                            modifier = Modifier.padding(AppSpacing.none).fillMaxWidth(),
                            contentPadding = PaddingValues(AppSpacing.none),
                        )
                        {
                            Text("Login")
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

    var email by remember { mutableStateOf("") }

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
                            color = Color.DarkGray,
                            style = Typography.labelLarge,
                            textAlign = TextAlign.Center
                        )

                       // Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(
                            onValueChange = {
                                email = it
                            },
                            value = email,
                            shape = AppShape.Large,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            placeholder = {
                                Text(
                                    text = "Enter your email",
                                    style = TextStyle(
                                       // fontFamily = myFontFamily,
                                        fontSize = 15.sp
                                    )
                                )
                            },
                        )
                        ElevatedButton(
                            onClick = {
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
    // LoginScreen()
    ForgetPasswordScreen()
}

class ThemeSelection : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyGetherTheme {
                ThemeSelectionScreen()
            }
        }
    }
}

@Composable
fun ThemeSelectionScreen() {
    val context = LocalContext.current

    val themes = listOf(
        ThemeOption("Blue Sky", MainTheme, Color(0xFF154B7C)),
        ThemeOption("Sunset", Color(0xFFFFB74D), Color(0xFFE65100)),
        ThemeOption("Forest", Color(0xFF81C784), Color(0xFF1B5E20)),
        ThemeOption("Midnight", Color(0xFF37474F), Color(0xFFCFD8DC))
    )

    Scaffold(
        modifier = Modifier.Companion.fillMaxSize(),
        containerColor = Color.Companion.White
    ) { innerPadding ->
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.Companion.height(20.dp))
            ElevatedButton(
                onClick = { (context as? Activity)?.finish() },
                modifier = Modifier.Companion.size(45.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.Companion.White),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    tint = Color.Companion.Black,
                    modifier = Modifier.Companion.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.Companion.height(30.dp))

            Text(
                text = "Choose Theme",
                style = TextStyle(
                    color = TextColor,
                    fontWeight = FontWeight.Companion.Bold,
                    fontSize = 28.sp,
                    //fontFamily = myFontFamily
                ),
                modifier = Modifier.Companion.fillMaxWidth(),
                textAlign = TextAlign.Companion.Center
            )

            Spacer(modifier = Modifier.Companion.height(40.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                items(themes) { theme ->
                    ThemeCard(theme)
                }
            }
        }
    }
}

data class ThemeOption(
    val name: String,
    val primaryColor: Color,
    val textColor: Color
)

@Composable
fun ThemeCard(theme: ThemeOption) {
    Card(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .height(150.dp)
            .clickable { /* TODO: Implement theme change logic */ },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = theme.primaryColor)
    ) {
        Box(
            modifier = Modifier.Companion.fillMaxSize(),
            contentAlignment = Alignment.Companion.Center
        ) {
            Column(horizontalAlignment = Alignment.Companion.CenterHorizontally) {
                Box(
                    modifier = Modifier.Companion
                        .size(40.dp)
                        .background(
                            theme.textColor,
                            androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                )
                Spacer(modifier = Modifier.Companion.height(10.dp))
                Text(
                    text = theme.name,
                    style = TextStyle(
                        color = theme.textColor,
                        fontWeight = FontWeight.Companion.Bold,
                        fontSize = 18.sp,
                        //fontFamily = myFontFamily
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThemeSelectionPreview() {
    ThemeSelectionScreen()
}