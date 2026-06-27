package com.example.studygether.ui.theme

import android.app.Activity
import android.hardware.lights.Light
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.studygether.ui.theme.DarkColorScheme

private val DarkColorScheme = darkColorScheme(
    primary = MainTheme,
    secondary = TextColor,
    tertiary = CardColour
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
private val LightBlueColorScheme = lightColorScheme(
    primary = Color(0xFF8AB2F5),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBFDBFE),
    onPrimaryContainer = Color(0xFF7389CE),
    secondary = Color(0xFF6A87B7),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE2ECFC),
    onSecondaryContainer = Color(0xFF5C71AB),
    tertiary = Color(0xFF4E889D),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFDCFCE7),
    onTertiaryContainer = Color(0xFF3B8196),
    background = Color(0xFFF4F7FC),
    onBackground = Color(0xFF334562),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF2C4977),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF64748B),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainer = Color(0xFFF1F5F9),
    surfaceContainerHigh = Color(0xFFE2E8F0),
    surfaceContainerHighest = Color(0xFFCBD5E1)
)
private val DarkBlueColorScheme = darkColorScheme(
    primary = Color(0xFF1F60BB),
    onPrimary = Color(0xFF3750A4),
    primaryContainer = Color(0xFF576DAF),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = Color(0xFF2D5594),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF46608A),
    onSecondaryContainer = Color(0xFF93C5FD),
    tertiary = Color(0xFF478186),
    onTertiary = Color(0xFF4DBEB5),
    tertiaryContainer = Color(0xFF66BBB9),
    onTertiaryContainer = Color(0xFFDCFCE7),
    background = Color(0xFF4B5F85),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF20304B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF647998),
    outlineVariant = Color(0xFF334155),
    surfaceContainerLowest = Color(0xFF3E5281),
    surfaceContainerLow = Color(0xFF0F172A),
    surfaceContainer = Color(0xFF1E293B),
    surfaceContainerHigh = Color(0xFF334155),
    surfaceContainerHighest = Color(0xFF475569)
)

private val LightGreenColorScheme = lightColorScheme(
    primary = Color(0xFF8BA96E),
    onPrimary = Color(0xFF84A17B),
    primaryContainer = Color(0xFFBED2BA),
    onPrimaryContainer = Color(0xFF394D2C),
    secondary = Color(0xFF8DAF9B),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFAAC4BC),
    onSecondaryContainer = Color(0xFF4DA183),
    tertiary = Color(0xFF58AD9D),
    onTertiary = Color(0xFF29674E),
    tertiaryContainer = Color(0xFFF5F0E6),
    onTertiaryContainer = Color(0xFF95AF9F),
    background = Color(0xFFF4F7F3),
    onBackground = Color(0xFFBBC5C0),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF8EB980),
    surfaceVariant = Color(0xFFE1E5DC),
    onSurfaceVariant = Color(0xFF84AF68),
    outline = Color(0xFF75915E),
    outlineVariant = Color(0xFFC3C8BC),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFEEF2EB),
    surfaceContainer = Color(0xFFE8ECE5),
    surfaceContainerHigh = Color(0xFFE2E6DF),
    surfaceContainerHighest = Color(0xFFDCDFD8)
)
private val DarkGreenColorScheme = darkColorScheme(
    primary = Color(0xFF516240),
    onPrimary = Color(0xFF50624B),
    primaryContainer = Color(0xFF626C60),
    onPrimaryContainer = Color(0xFF394D2C),
    secondary = Color(0xFF4C6958),
    onSecondary = Color(0xFF647A77),
    secondaryContainer = Color(0xFF8DA29D),
    onSecondaryContainer = Color(0xFF31624E),
    tertiary = Color(0xFF58AD9D),
    onTertiary = Color(0xFF29674E),
    tertiaryContainer = Color(0xFF3C523E),
    onTertiaryContainer = Color(0xFF7E9887),
    background = Color(0xFFF4F7F3),
    onBackground = Color(0xFF525E4D),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1D19),
    surfaceVariant = Color(0xFFE1E5DC),
    onSurfaceVariant = Color(0xFF43493F),
    outline = Color(0xFF73796E),
    outlineVariant = Color(0xFFC3C8BC),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFEEF2EB),
    surfaceContainer = Color(0xFFE8ECE5),
    surfaceContainerHigh = Color(0xFFE2E6DF),
    surfaceContainerHighest = Color(0xFFDCDFD8)
)

@Composable
fun StudyGetherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+

    dynamicColor: Boolean = true,
    themeStyle: AppThemeStyle = AppThemeStyle.BlueTheme,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            when (themeStyle) {
                AppThemeStyle.DEFAULT -> {
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
                }
                AppThemeStyle.BlueTheme -> {
                    if (darkTheme) DarkBlueColorScheme else LightBlueColorScheme
                }
                AppThemeStyle.GreenTheme -> {
                    if (darkTheme) DarkGreenColorScheme else LightGreenColorScheme
                }
            }
        }
        darkTheme -> {
            when (themeStyle) {
                AppThemeStyle.DEFAULT -> DarkColorScheme
                AppThemeStyle.BlueTheme -> DarkBlueColorScheme
                AppThemeStyle.GreenTheme -> DarkGreenColorScheme
            }
        }
        else -> {
            when (themeStyle) {
                AppThemeStyle.DEFAULT -> LightColorScheme
                AppThemeStyle.BlueTheme -> LightBlueColorScheme
                AppThemeStyle.GreenTheme -> LightGreenColorScheme

            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

}

enum class AppThemeStyle {
    DEFAULT, BlueTheme, GreenTheme
}
