package com.example.studygether.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.example.studygether.ui.theme.DarkColorScheme

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,

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
private val LightSunsetColorScheme = lightColorScheme(
    primary = Color(0xFFE65100),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFD180),
    onPrimaryContainer = Color(0xFFE65100),
    secondary = Color(0xFFF57C00),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFFE65100),
    tertiary = Color(0xFFD84315),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFCC80),
    onTertiaryContainer = Color(0xFFD84315),
    background = Color(0xFFFFF8E1),
    onBackground = Color(0xFF5D4037),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF4E342E),
    surfaceVariant = Color(0xFFFFECB3),
    onSurfaceVariant = Color(0xFF5D4037),
    outline = Color(0xFFFFB74D),
    outlineVariant = Color(0xFFFFECB3),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFFDF7),
    surfaceContainer = Color(0xFFFFF8E1),
    surfaceContainerHigh = Color(0xFFFFF3CD),
    surfaceContainerHighest = Color(0xFFFFE0B2)
)

private val DarkSunsetColorScheme = darkColorScheme(
    primary = Color(0xFFFFB74D),
    onPrimary = Color(0xFF5D4037),
    primaryContainer = Color(0xFFE65100),
    onPrimaryContainer = Color(0xFFFFD180),
    secondary = Color(0xFFFFCC80),
    onSecondary = Color(0xFF4E342E),
    secondaryContainer = Color(0xFFF57C00),
    onSecondaryContainer = Color(0xFFFFE0B2),
    tertiary = Color(0xFFFFAB91),
    onTertiary = Color(0xFF5D4037),
    tertiaryContainer = Color(0xFFD84315),
    onTertiaryContainer = Color(0xFFFFCC80),
    background = Color(0xFF3E2723),
    onBackground = Color(0xFFFFE0B2),
    surface = Color(0xFF2D1B18),
    onSurface = Color(0xFFFFF8E1),
    surfaceVariant = Color(0xFF4E342E),
    onSurfaceVariant = Color(0xFFFFD180),
    outline = Color(0xFF8D6E63),
    outlineVariant = Color(0xFF4E342E),
    surfaceContainerLowest = Color(0xFF271512),
    surfaceContainerLow = Color(0xFF35201C),
    surfaceContainer = Color(0xFF3E2723),
    surfaceContainerHigh = Color(0xFF4E332E),
    surfaceContainerHighest = Color(0xFF5D4037)
)

private val LightMidnightColorScheme = lightColorScheme(
    primary = Color(0xFF37474F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFECEFF1),
    onPrimaryContainer = Color(0xFF263238),
    secondary = Color(0xFF455A64),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCFD8DC),
    onSecondaryContainer = Color(0xFF37474F),
    tertiary = Color(0xFF006064),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE0F7FA),
    onTertiaryContainer = Color(0xFF006064),
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF263238),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF212121),
    surfaceVariant = Color(0xFFECEFF1),
    onSurfaceVariant = Color(0xFF455A64),
    outline = Color(0xFFB0BEC5),
    outlineVariant = Color(0xFFCFD8DC),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF1F3F4),
    surfaceContainer = Color(0xFFECEFF1),
    surfaceContainerHigh = Color(0xFFDFE3E6),
    surfaceContainerHighest = Color(0xFFCFD8DC)
)

private val DarkMidnightColorScheme = darkColorScheme(
    primary = Color(0xFF90A4AE),
    onPrimary = Color(0xFF212121),
    primaryContainer = Color(0xFF37474F),
    onPrimaryContainer = Color(0xFFECEFF1),
    secondary = Color(0xFFB0BEC5),
    onSecondary = Color(0xFF263238),
    secondaryContainer = Color(0xFF455A64),
    onSecondaryContainer = Color(0xFFCFD8DC),
    tertiary = Color(0xFF80DEEA),
    onTertiary = Color(0xFF263238),
    tertiaryContainer = Color(0xFF006064),
    onTertiaryContainer = Color(0xFFE0F7FA),
    background = Color(0xFF121212),
    onBackground = Color(0xFFECEFF1),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF263238),
    onSurfaceVariant = Color(0xFFB0BEC5),
    outline = Color(0xFF37474F),
    outlineVariant = Color(0xFF263238),
    surfaceContainerLowest = Color(0xFF0C0C0C),
    surfaceContainerLow = Color(0xFF181818),
    surfaceContainer = Color(0xFF1E1E1E),
    surfaceContainerHigh = Color(0xFF2A2A2A),
    surfaceContainerHighest = Color(0xFF363636)
)
@Composable
fun StudyGetherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+

    dynamicColor: Boolean = true,
    themeStyle: AppThemeStyle = AppThemeStyle.DEFAULT,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.isNavigationBarContrastEnforced =false
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
