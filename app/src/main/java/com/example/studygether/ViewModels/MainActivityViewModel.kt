package com.example.studygether.ViewModels

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.studygether.View.BottomBars


data class TopBarState(
    val title: @Composable () -> Unit={},
    val showBackButton: Boolean=false,
    val barColor: Color =Color.Unspecified,
    val actions: @Composable RowScope.() -> Unit={}
)


data class BottomBarState(
    var bottomBar: BottomBars= BottomBars.None
)

class MainActivityViewModel : ViewModel()
{
    var topBarState by mutableStateOf(TopBarState())
        private set

    var bottomBarState by mutableStateOf(BottomBarState())
            private set

    fun setTopBar(newState:TopBarState)
    {
        topBarState=newState
    }

    fun setTitle(title:@Composable ()-> Unit)
    {
        topBarState=topBarState.copy(title =title )
    }

    fun setBottomBarType(bottomBars:BottomBarState)
    {
        bottomBarState=bottomBars
    }



}