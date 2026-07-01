package com.example.studygether.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.Navigation.AppGraph
//import com.example.studygether.Navigation.MainGraph
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ui.theme.StudyGetherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyGetherTheme(false,false) {
                App()
            }
        }
    }
}

@Composable
fun App()
{
    val viewModel: AppBarsViewModel = viewModel()
    AppGraph()
}
