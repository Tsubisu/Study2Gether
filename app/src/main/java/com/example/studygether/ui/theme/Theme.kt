package com.example.studygether

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.studygether.ui.theme.AccentPurple
import com.example.studygether.ui.theme.BackgroundBlue
import com.example.studygether.ui.theme.TextDark

private val LightColorScheme = lightColorScheme(
    primary = AccentPurple,
    background = BackgroundBlue,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = TextDark,
    onSurface = TextDark,
)

@Composable
fun studygetherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}