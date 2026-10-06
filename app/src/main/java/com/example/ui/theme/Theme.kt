package com.example.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = CorporateNavyDark,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = WhatsAppGreen,
    onSecondary = Color(0xFF064E3B),
    secondaryContainer = Color(0xFF065F46),
    onSecondaryContainer = Color(0xFFA7F3D0),
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = OutlineDark,
    error = Color(0xFFF87171),
    onError = Color(0xFF450A0A)
)

private val LightColorScheme = lightColorScheme(
    primary = CorporateBlue,
    onPrimary = Color.White,
    primaryContainer = CorporateBlueContainer,
    onPrimaryContainer = CorporateOnBlueContainer,
    secondary = WhatsAppGreenDark,
    onSecondary = Color.White,
    secondaryContainer = WhatsAppGreenContainer,
    onSecondaryContainer = Color(0xFF064E3B),
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = OutlineLight,
    error = DangerRed,
    onError = Color.White,
    errorContainer = DangerRedContainer,
    onErrorContainer = DangerRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep corporate navy identity consistent
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
