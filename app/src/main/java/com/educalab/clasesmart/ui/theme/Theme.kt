package com.educalab.clasesmart.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * ClaseSmart usa siempre la misma paleta tematica de aula, sin importar si
 * el sistema esta en modo oscuro o claro (Regla: la app no debe cambiar de
 * colores segun el tema del dispositivo, para mantener consistencia visual
 * infantil/educativa).
 */
private val AulaScheme = lightColorScheme(
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

@Composable
fun ClaseSmartTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AulaScheme,
        typography = ClaseSmartTypography,
        content = content
    )
}
