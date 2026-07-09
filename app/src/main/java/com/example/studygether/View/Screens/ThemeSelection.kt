package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ViewModels.BottomBarState
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ThemeSelectionScreen(onNavigateBack: () -> Unit) {
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)
    LaunchedEffect(Unit) {
        appBarsViewModel.hideTopBar()
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
    }

    val currentTheme by ThemeManager.currentTheme.collectAsStateWithLifecycle()

    val themes = listOf(
        ThemeOption("Default", AppThemeStyle.DEFAULT, Color(0xFF6650A4), Color(0xFFFFFFFF)),
        ThemeOption("Blue Sky", AppThemeStyle.BlueTheme, Color(0xFF8AB2F5), Color(0xFF154B7C)),
        ThemeOption("Sunset", AppThemeStyle.SunsetTheme, Color(0xFFFFB74D), Color(0xFFE65100)),
        ThemeOption("Forest", AppThemeStyle.GreenTheme, Color(0xFF81C784), Color(0xFF1B5E20)),
        ThemeOption("Midnight", AppThemeStyle.MidnightTheme, Color(0xFF37474F), Color(0xFFCFD8DC))
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ElevatedButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(45.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.White),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Choose Theme",
                style = TextStyle(
                    color = TextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

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