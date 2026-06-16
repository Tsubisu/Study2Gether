package com.example.studygether.View

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.ViewModels.MainActivityViewModel
import com.example.studygether.ViewModels.TopBarState

@Composable
fun Channel(viewModel: MainActivityViewModel= viewModel(),ChannelName:String,
            Logo:Int, Members:Int)
{

    LaunchedEffect(ChannelName, Logo,Members)
    {
        viewModel.setTopBar(TopBarState
            (
                
                    ))


    }
}