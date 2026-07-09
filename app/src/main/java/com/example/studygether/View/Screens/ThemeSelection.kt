package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.R
import com.example.studygether.ui.theme.MainTheme
import com.example.studygether.ui.theme.TextColor
import com.example.studygether.ui.theme.AppThemeStyle
import com.example.studygether.ui.theme.ThemeManager
import com.example.studygether.ui.theme.Typography
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ViewModels.BottomBarState
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ThemeSelectionScreen(
    modifier: Modifier,
    onNavigateBack: () -> Unit
) {
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)

    val currentTheme by ThemeManager.currentTheme.collectAsStateWithLifecycle()
    val isDarkMode by ThemeManager.isDarkMode.collectAsStateWithLifecycle()
    
    val barBgColor = MaterialTheme.colorScheme.background
    LaunchedEffect(currentTheme, isDarkMode) {
        appBarsViewModel.setTitleBar(
            title = { Text("Choose Theme", style = Typography.headlineMedium) },
            showBackButton = true,
            actions = {},
            barColor = barBgColor
        )
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
    }

    val themes = listOf(
        ThemeOption(
            name = "Default",
            themeStyle = AppThemeStyle.DEFAULT,
            primaryColor = if (isDarkMode) Color(0xFFD0BCFF) else Color(0xFF6650A4),
            textColor = if (isDarkMode) Color(0xFF381E72) else Color(0xFFFFFFFF)
        ),
        ThemeOption(
            name = "Blue Sky",
            themeStyle = AppThemeStyle.BlueTheme,
            primaryColor = if (isDarkMode) Color(0xFF60A5FA) else Color(0xFF1E40AF),
            textColor = if (isDarkMode) Color.Black else Color.White
        ),
        ThemeOption(
            name = "Sunset",
            themeStyle = AppThemeStyle.SunsetTheme,
            primaryColor = if (isDarkMode) Color(0xFFFFB74D) else Color(0xFFE65100),
            textColor = if (isDarkMode) Color.Black else Color.White
        ),
        ThemeOption(
            name = "Forest",
            themeStyle = AppThemeStyle.GreenTheme,
            primaryColor = if (isDarkMode) Color(0xFF81C784) else Color(0xFF1B5E20),
            textColor = if (isDarkMode) Color.Black else Color.White
        ),
        ThemeOption(
            name = "Midnight",
            themeStyle = AppThemeStyle.MidnightTheme,
            primaryColor = if (isDarkMode) Color(0xFF90A4AE) else Color(0xFF263238),
            textColor = if (isDarkMode) Color.Black else Color.White
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        // Light / Dark Mode selector tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { ThemeManager.setDarkMode(false) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isDarkMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (!isDarkMode) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Light Mode")
            }
            Button(
                onClick = { ThemeManager.setDarkMode(true) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isDarkMode) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Dark Mode")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(themes) { theme ->
                ThemeCard(
                    theme = theme,
                    isSelected = theme.themeStyle == currentTheme,
                    onSelect = { ThemeManager.setTheme(theme.themeStyle) }
                )
            }
        }
    }
}

data class ThemeOption(
    val name: String,
    val themeStyle: AppThemeStyle,
    val primaryColor: Color,
    val textColor: Color
)

@Composable
fun ThemeCard(
    theme: ThemeOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderStroke = if (isSelected) {
        androidx.compose.foundation.BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
    } else {
        null
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onSelect() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = theme.primaryColor),
        border = borderStroke
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            theme.textColor,
                            RoundedCornerShape(10.dp)
                        )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = theme.name,
                    style = TextStyle(
                        color = theme.textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}