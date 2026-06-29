package com.example.studygether.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.Navigation.AppGraph
import com.example.studygether.Navigation.AuthGraph
//import com.example.studygether.Navigation.MainGraph
import com.example.studygether.ViewModels.MainActivityViewModel
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
fun App(viewModel: MainActivityViewModel= viewModel())
{
    AppGraph()
}
