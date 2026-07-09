package com.example.studygether.ui.theme

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemeManager {
    private val _currentTheme = MutableStateFlow(AppThemeStyle.DEFAULT)
    val currentTheme: StateFlow<AppThemeStyle> = _currentTheme.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private var sharedPreferences: android.content.SharedPreferences? = null

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedTheme = sharedPreferences?.getString("selected_theme", AppThemeStyle.DEFAULT.name)
        val themeStyle = try {
            AppThemeStyle.valueOf(savedTheme ?: AppThemeStyle.DEFAULT.name)
        } catch (e: Exception) {
            AppThemeStyle.DEFAULT
        }
        _currentTheme.value = themeStyle
        _isDarkMode.value = sharedPreferences?.getBoolean("is_dark_mode", false) ?: false
    }

    fun setTheme(theme: AppThemeStyle) {
        _currentTheme.value = theme
        sharedPreferences?.edit()?.putString("selected_theme", theme.name)?.apply()
    }

    fun setDarkMode(dark: Boolean) {
        _isDarkMode.value = dark
        sharedPreferences?.edit()?.putBoolean("is_dark_mode", dark)?.apply()
    }
}