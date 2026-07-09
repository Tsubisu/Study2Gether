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
        migrate()
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

    if (isHydrating) {
        SplashScreen() // simple loading indicator, no navigation decision made yet
    } else {
        AppGraph()
    }
}


@Composable
fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

fun migrate(){
    DatabaseMigration.migrateCommunityMembersRole()
}

