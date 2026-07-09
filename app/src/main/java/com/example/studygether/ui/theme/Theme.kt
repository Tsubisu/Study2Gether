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
    tertiary = Pink80,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFB0B0B0)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = Color(0xFF6B7280)
)

private val LightBlueColorScheme = lightColorScheme(
    primary = Color(0xFF1E40AF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E40AF),
    secondary = Color(0xFF3B82F6),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEFF6FF),
    onSecondaryContainer = Color(0xFF1E40AF),
    tertiary = Color(0xFF0369A1),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE0F2FE),
    onTertiaryContainer = Color(0xFF0369A1),
    background = Color.White,
    onBackground = Color(0xFF1E293B),
    surface = Color.White,
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFF1F5F9),
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
    primary = Color(0xFF60A5FA),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = Color(0xFF93C5FD),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF172554),
    onSecondaryContainer = Color(0xFFEFF6FF),
    tertiary = Color(0xFF7DD3FC),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF0C4A6E),
    onTertiaryContainer = Color(0xFFE0F2FE),
    background = Color.Black,
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF1F2937),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF4B5563),
    outlineVariant = Color(0xFF1F2937),
    surfaceContainerLowest = Color(0xFF030712),
    surfaceContainerLow = Color(0xFF111827),
    surfaceContainer = Color(0xFF1F2937),
    surfaceContainerHigh = Color(0xFF374151),
    surfaceContainerHighest = Color(0xFF4B5563)
)

private val LightGreenColorScheme = lightColorScheme(
    primary = Color(0xFF1B5E20),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F5E9),
    onPrimaryContainer = Color(0xFF1B5E20),
    secondary = Color(0xFF4CAF50),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF1F8E9),
    onSecondaryContainer = Color(0xFF1B5E20),
    tertiary = Color(0xFF2E7D32),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE8F5E9),
    onTertiaryContainer = Color(0xFF2E7D32),
    background = Color.White,
    onBackground = Color(0xFF1A1D19),
    surface = Color.White,
    onSurface = Color(0xFF1A1D19),
    surfaceVariant = Color(0xFFF4F6F4),
    onSurfaceVariant = Color(0xFF5D6B5D),
    outline = Color(0xFF758F75),
    outlineVariant = Color(0xFFE1E5DC),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF1F5F1),
    surfaceContainer = Color(0xFFE8ECE8),
    surfaceContainerHigh = Color(0xFFE0E5E0),
    surfaceContainerHighest = Color(0xFFD0D5D0)
)

private val DarkGreenColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFFE8F5E9),
    secondary = Color(0xFFA5D6A7),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF0F3E10),
    onSecondaryContainer = Color(0xFFF1F8E9),
    tertiary = Color(0xFFA5D6A7),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF1B5E20),
    onTertiaryContainer = Color(0xFFE8F5E9),
    background = Color.Black,
    onBackground = Color(0xFFE2E3DF),
    surface = Color(0xFF121812),
    onSurface = Color(0xFFE2E3DF),
    surfaceVariant = Color(0xFF1C241C),
    onSurfaceVariant = Color(0xFF9CA3AF),
    outline = Color(0xFF4D564D),
    outlineVariant = Color(0xFF1C241C),
    surfaceContainerLowest = Color(0xFF0A0E0A),
    surfaceContainerLow = Color(0xFF121812),
    surfaceContainer = Color(0xFF1C241C),
    surfaceContainerHigh = Color(0xFF2C352C),
    surfaceContainerHighest = Color(0xFF4D564D)
)

private val LightSunsetColorScheme = lightColorScheme(
    primary = Color(0xFFE65100),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFF3E0),
    onPrimaryContainer = Color(0xFFE65100),
    secondary = Color(0xFFF57C00),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFFE65100),
    tertiary = Color(0xFFD84315),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFCC80),
    onTertiaryContainer = Color(0xFFD84315),
    background = Color.White,
    onBackground = Color(0xFF3E2723),
    surface = Color.White,
    onSurface = Color(0xFF3E2723),
    surfaceVariant = Color(0xFFFAF2ED),
    onSurfaceVariant = Color(0xFF7D6B65),
    outline = Color(0xFFFFB74D),
    outlineVariant = Color(0xFFFFECB3),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFF7F2),
    surfaceContainer = Color(0xFFFAF2ED),
    surfaceContainerHigh = Color(0xFFF5E7DF),
    surfaceContainerHighest = Color(0xFFFFE0B2)
)

private val DarkSunsetColorScheme = darkColorScheme(
    primary = Color(0xFFFFB74D),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFE65100),
    onPrimaryContainer = Color(0xFFFFF3E0),
    secondary = Color(0xFFFFCC80),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF5D2B00),
    onSecondaryContainer = Color(0xFFFFE0B2),
    tertiary = Color(0xFFFFAB91),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFD84315),
    onTertiaryContainer = Color(0xFFFFCC80),
    background = Color.Black,
    onBackground = Color(0xFFFFF8E1),
    surface = Color(0xFF1E1410),
    onSurface = Color(0xFFFFF8E1),
    surfaceVariant = Color(0xFF2C1E18),
    onSurfaceVariant = Color(0xFFB09B95),
    outline = Color(0xFF8D6E63),
    outlineVariant = Color(0xFF2C1E18),
    surfaceContainerLowest = Color(0xFF0F0A08),
    surfaceContainerLow = Color(0xFF1E1410),
    surfaceContainer = Color(0xFF2C1E18),
    surfaceContainerHigh = Color(0xFF3C2C25),
    surfaceContainerHighest = Color(0xFF5D4037)
)

private val LightMidnightColorScheme = lightColorScheme(
    primary = Color(0xFF263238),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFECEFF1),
    onPrimaryContainer = Color(0xFF263238),
    secondary = Color(0xFF455A64),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCFD8DC),
    onSecondaryContainer = Color(0xFF263238),
    tertiary = Color(0xFF006064),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE0F7FA),
    onTertiaryContainer = Color(0xFF006064),
    background = Color.White,
    onBackground = Color(0xFF212121),
    surface = Color.White,
    onSurface = Color(0xFF212121),
    surfaceVariant = Color(0xFFF1F3F4),
    onSurfaceVariant = Color(0xFF5A6A72),
    outline = Color(0xFFB0BEC5),
    outlineVariant = Color(0xFFCFD8DC),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF5F7F8),
    surfaceContainer = Color(0xFFECEFF1),
    surfaceContainerHigh = Color(0xFFDFE3E6),
    surfaceContainerHighest = Color(0xFFCFD8DC)
)

private val DarkMidnightColorScheme = darkColorScheme(
    primary = Color(0xFF90A4AE),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF37474F),
    onPrimaryContainer = Color(0xFFECEFF1),
    secondary = Color(0xFFB0BEC5),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1C2428),
    onSecondaryContainer = Color(0xFFCFD8DC),
    tertiary = Color(0xFF80DEEA),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF006064),
    onTertiaryContainer = Color(0xFFE0F7FA),
    background = Color.Black,
    onBackground = Color(0xFFECEFF1),
    surface = Color(0xFF14181B),
    onSurface = Color(0xFFECEFF1),
    surfaceVariant = Color(0xFF20262A),
    onSurfaceVariant = Color(0xFF90A4AE),
    outline = Color(0xFF37474F),
    outlineVariant = Color(0xFF20262A),
    surfaceContainerLowest = Color(0xFF090A0B),
    surfaceContainerLow = Color(0xFF14181B),
    surfaceContainer = Color(0xFF20262A),
    surfaceContainerHigh = Color(0xFF2E373E),
    surfaceContainerHighest = Color(0xFF37474F)
)

@Composable
fun StudyGetherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
                AppThemeStyle.SunsetTheme -> {
                    if (darkTheme) DarkSunsetColorScheme else LightSunsetColorScheme
                }
                AppThemeStyle.MidnightTheme -> {
                    if (darkTheme) DarkMidnightColorScheme else LightMidnightColorScheme
                }
            }
        }
        darkTheme -> {
            when (themeStyle) {
                AppThemeStyle.DEFAULT -> DarkColorScheme
                AppThemeStyle.BlueTheme -> DarkBlueColorScheme
                AppThemeStyle.GreenTheme -> DarkGreenColorScheme
                AppThemeStyle.SunsetTheme -> DarkSunsetColorScheme
                AppThemeStyle.MidnightTheme -> DarkMidnightColorScheme
            }
        }
        else -> {
            when (themeStyle) {
                AppThemeStyle.DEFAULT -> LightColorScheme
                AppThemeStyle.BlueTheme -> LightBlueColorScheme
                AppThemeStyle.GreenTheme -> LightGreenColorScheme
                AppThemeStyle.SunsetTheme -> LightSunsetColorScheme
                AppThemeStyle.MidnightTheme -> LightMidnightColorScheme
            }
        }
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.isNavigationBarContrastEnforced = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

enum class AppThemeStyle {
    DEFAULT, BlueTheme, GreenTheme, SunsetTheme, MidnightTheme
}
