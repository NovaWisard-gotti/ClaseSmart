package com.educalab.clasesmart.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = ClaseSmartColors.PizarraVerde,
    onPrimary = ClaseSmartColors.TizaBlanca,
    secondary = ClaseSmartColors.AcentoNaranja,
    onSecondary = ClaseSmartColors.TizaBlanca,
    background = ClaseSmartColors.ParedCrema,
    onBackground = ClaseSmartColors.TextoOscuro,
    surface = ClaseSmartColors.PapelBeige,
    onSurface = ClaseSmartColors.TextoOscuro,
    tertiary = ClaseSmartColors.AcentoAzulCielo
)

private val DarkScheme = darkColorScheme(
    primary = ClaseSmartColors.AcentoAzulCielo,
    onPrimary = ClaseSmartColors.TextoOscuro,
    secondary = ClaseSmartColors.TizaAmarilla,
    background = ClaseSmartColors.PizarraVerdeOscuro,
    onBackground = ClaseSmartColors.ParedCrema,
    surface = ClaseSmartColors.MarcoMaderaOscuro,
    onSurface = ClaseSmartColors.ParedCrema
)

@Composable
fun ClaseSmartTheme(useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) DarkScheme else LightScheme,
        typography = ClaseSmartTypography,
        content = content
    )
}
