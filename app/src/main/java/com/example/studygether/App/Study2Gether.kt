package com.example.studygether.App

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studygether.Navigation.AppGraph
import com.example.studygether.Repository.AppRepositories
import com.example.studygether.Utility.DatabaseMigration
import com.example.studygether.ui.theme.StudyGetherTheme

class Study2Gether : Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepositories.init(this)
        com.example.studygether.ui.theme.ThemeManager.init(this)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentTheme by com.example.studygether.ui.theme.ThemeManager.currentTheme.collectAsStateWithLifecycle()
            val isDarkMode by com.example.studygether.ui.theme.ThemeManager.isDarkMode.collectAsStateWithLifecycle()
            StudyGetherTheme(
                darkTheme = isDarkMode,
                dynamicColor = false,
                themeStyle = currentTheme
            ) {
                App()
            }
        }
    }
}

@Composable
fun App()
{
    val isHydrating by SessionState.isHydrating.collectAsStateWithLifecycle()
    val isLoggingOut by SessionState.isLoggingOut.collectAsStateWithLifecycle()

    if (isHydrating || isLoggingOut) {
        val message = if (isHydrating) "Loading..." else "Logging out..."
        SplashScreen(message = message)
    } else {
        AppGraph()
    }
}


@Composable
fun SplashScreen(message: String = "") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = com.example.studygether.R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Study2Gether",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(36.dp)
            )
            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}



