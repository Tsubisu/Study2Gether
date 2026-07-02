package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.View.AppBars.ChatUserLabelCard
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ViewModels.AppBarsViewModel


@Composable
fun ConvoScreen(
    modifier: Modifier,
    name: String,
    image: Int,
    onGoBack: () -> Unit
) {
    val activity = LocalActivity.current as ComponentActivity
    val mainViewModel: AppBarsViewModel = viewModel(activity)
    val barColor: Color = MaterialTheme.colorScheme.background
    LaunchedEffect(name, image)
    {

        mainViewModel.setTitleBar(
            title = { ChatUserLabelCard(name, profileImage = image, onClick = {}) },
            showBackButton = true,
            actions = { IconButton(onClick = {})
            {
                Icon(Icons.Outlined.Phone,contentDescription = null)
            } },
            barColor = barColor
        )


        mainViewModel.setBottomBarType(BottomBarState(BottomBars.MessageBar))

    }




}

//@Preview
//@Composable
//fun DmPreview()
//{
//   ConvoScreen()
//}