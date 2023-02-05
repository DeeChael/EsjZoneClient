package net.deechael.esjzone

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import net.deechael.esjzone.colors.Catppuccin
import net.deechael.esjzone.ui.theme.Typography

private val latteTheme = lightColorScheme(
    background = Catppuccin.Latte.Base,
    primary = Catppuccin.Latte.Lavender,
    secondary = Catppuccin.Latte.Sky,
    tertiary = Catppuccin.Latte.Sapphire,
    error = Catppuccin.Latte.Flamingo,
    outline = Catppuccin.Latte.Overlay2
)

private val mochaTheme = darkColorScheme(
    background = Catppuccin.Mocha.Base,
    primary = Catppuccin.Mocha.Lavender,
    secondary = Catppuccin.Mocha.Sky,
    tertiary = Catppuccin.Mocha.Sapphire,
    error = Catppuccin.Mocha.Flamingo,
    outline = Catppuccin.Mocha.Overlay2
)

@Composable
fun LatteTheme(
    lightTheme: Boolean = !isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = latteTheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MochaTheme(
    lightTheme: Boolean = !isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = mochaTheme,
        typography = Typography,
        content = content
    )
}