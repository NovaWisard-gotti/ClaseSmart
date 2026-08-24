package com.educalab.clasesmart.ui.scene

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.domain.model.ModuleState
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C

/**
 * Envoltorio interactivo para CUALQUIER objeto del aula: aplica el estado
 * visual (Regla 19: bloqueado/disponible/iniciado/completado/dominado con
 * iconografia, nunca solo color) y una animacion idle sutil de "objeto vivo".
 */
@Composable
fun ClassroomObjectSlot(
    state: ModuleState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infinite = rememberInfiniteTransition(label = "idle")
    val bobbing by infinite.animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1400), repeatMode = RepeatMode.Reverse),
        label = "bobbing"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = if (state != ModuleState.BLOQUEADO) (bobbing - 0.5f) * 4f else 0f
            }
            .alpha(if (state == ModuleState.BLOQUEADO) 0.55f else 1f)
            .clickable(enabled = true, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()

        if (state == ModuleState.BLOQUEADO) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(28.dp)
                    .background(Color.Black.copy(alpha = 0.55f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Lock, contentDescription = "Bloqueado", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }

        if (state == ModuleState.DOMINADO) {
            Box(modifier = Modifier.align(Alignment.TopEnd).padding(2.dp)) {
                Icon(Icons.Filled.Star, contentDescription = "Dominado", tint = C.TizaAmarilla, modifier = Modifier.size(18.dp))
            }
        }

        if (state == ModuleState.COMPLETADO) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(14.dp)
                    .background(C.EstadoCompletado, CircleShape)
            )
        }

        if (state == ModuleState.INICIADO) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(14.dp)
                    .background(C.EstadoIniciado, CircleShape)
            )
        }
    }
}
