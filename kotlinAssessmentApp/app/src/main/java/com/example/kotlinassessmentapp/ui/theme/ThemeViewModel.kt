package com.example.kotlinassessmentapp.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ThemeViewModel following Enterprise Theme Management Patterns
 * 
 * Used by companies like:
 * - Google (Material You dynamic theming)
 * - Netflix (dark/light theme switching)
 * - Spotify (theme persistence)
 * 
 * Features:
 * - Reactive theme state management
 * - System theme detection
 * - Theme persistence ready
 */
class ThemeViewModel : ViewModel() {
    
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    private val _isSystemTheme = MutableStateFlow(true)
    val isSystemTheme: StateFlow<Boolean> = _isSystemTheme.asStateFlow()
    
    fun toggleTheme() {
        _isSystemTheme.value = false
        _isDarkTheme.value = !_isDarkTheme.value
    }
    
    fun setDarkTheme(isDark: Boolean) {
        _isSystemTheme.value = false
        _isDarkTheme.value = isDark
    }
    
    fun useSystemTheme() {
        _isSystemTheme.value = true
    }
    
    fun setSystemTheme(isSystemDark: Boolean) {
        if (_isSystemTheme.value) {
            _isDarkTheme.value = isSystemDark
        }
    }
}
