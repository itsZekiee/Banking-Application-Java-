package com.bankingapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BrandGreen,
    onPrimary = SurfaceLight,
    primaryContainer = BrandGreenSoft,
    onPrimaryContainer = BrandGreenDark,
    secondary = BrandGreenBright,
    onSecondary = SurfaceLight,
    secondaryContainer = BrandGreenMist,
    onSecondaryContainer = BrandGreenDark,
    background = SurfaceLight,
    onBackground = TextInk,
    surface = SurfaceLight,
    onSurface = TextInk,
    surfaceVariant = FieldGray,
    onSurfaceVariant = TextMuted,
    outline = BorderGray,
    error = ErrorRed,
    onError = SurfaceLight
)

@Composable
fun BankingAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
