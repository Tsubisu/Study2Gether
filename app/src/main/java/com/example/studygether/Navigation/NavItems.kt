package com.example.studygether.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Textsms
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.studygether.R


data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Any
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Outlined.Home, HomePage),
    BottomNavItem("Dms",  Icons.Outlined.Textsms,ConvoList ),
    BottomNavItem("Channels", Icons.Outlined.Groups, Channels)
)

