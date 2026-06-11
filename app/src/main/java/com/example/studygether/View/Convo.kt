package com.example.studygether.View

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.Model.Users
import com.example.studygether.ViewModels.MainActivityViewModel
import com.example.studygether.ViewModels.TopBarState
import com.example.studygether.ui.theme.Typography


@Composable
fun ConvoScreen(mainViewModel: MainActivityViewModel= viewModel(),
                name:String,
                image:Int,
                onGoBack:()->Unit)
{
    val barColor: Color= MaterialTheme.colorScheme.background
    LaunchedEffect(name, image)
    {

        mainViewModel.setTopBar(TopBarState(
            //title ={Text("Jane Doe",style= Typography.headlineMedium)},
            title = {ChatUserLabelCard(name, profileImage =image,onClick ={})},
            showBackButton = true,
            actions = { IconButton(onClick = {})
            {
                Icon(Icons.Outlined.Phone,contentDescription = null)
            } },
            barColor = barColor
        ))
        Log.d("Test","Printed")

    }




}

//@Preview
//@Composable
//fun DmPreview()
//{
//   ConvoScreen()
//}