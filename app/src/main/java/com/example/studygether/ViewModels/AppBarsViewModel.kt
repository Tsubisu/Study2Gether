package com.example.studygether.ViewModels

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.studygether.View.AppBars.BottomBars




sealed interface TopBarState {
    data object None : TopBarState

    data class TitleBar(
        val title: @Composable () -> Unit = {},
        val showBackButton: Boolean = false,
        val barColor: Color = Color.Unspecified,
        val actions: @Composable RowScope.() -> Unit = {}
    ) : TopBarState
}


data class BottomBarState(
    var bottomBar: BottomBars= BottomBars.None
)

class AppBarsViewModel : ViewModel()
{
    var topBarState by mutableStateOf<TopBarState>(TopBarState.None)
        private set

    var bottomBarState by mutableStateOf(BottomBarState())
            private set

    fun setTitleBar(
        title: @Composable () -> Unit,
        showBackButton: Boolean = false,
        barColor: Color = Color.Unspecified,
        actions: @Composable RowScope.() -> Unit = {}
    ) {
        topBarState = TopBarState.TitleBar(
            title = title,
            showBackButton = showBackButton,
            barColor = barColor,
            actions = actions
        )
    }

    fun hideTopBar() {
        topBarState = TopBarState.None
    }


    fun setBottomBarType(bottomBars:BottomBarState)
    {
        bottomBarState=bottomBars
    }



}