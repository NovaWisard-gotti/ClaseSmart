package com.educalab.clasesmart.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.navigation.NavBackStackEntry

/**
 * Transiciones propias que refuerzan la metafora del aula: al tocar un
 * objeto, la "camara" se acerca (zoom + fade) hacia esa zona; al volver,
 * se aleja de nuevo hacia el aula general. Nunca es un corte instantaneo
 * ni un simple cross-fade generico (Regla: "las transiciones deben
 * reforzar la metafora del aula").
 *
 * Navigation Compose anima cada destino con CUATRO fases independientes
 * (no un unico ContentTransform como en AnimatedContent):
 *  - enterTransition:    este destino aparece al navegar HACIA el (forward)
 *  - exitTransition:     este destino desaparece al navegar DESDE el (forward)
 *  - popEnterTransition: este destino reaparece al volver a el (back)
 *  - popExitTransition:  este destino desaparece al salir de el (back)
 */
object ClassroomTransitions {

    /** Un modulo aparece "acercandose" cuando se entra desde el aula. */
    val enterZoomIn: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        scaleIn(initialScale = 0.85f, animationSpec = tween(320)) + fadeIn(animationSpec = tween(280))
    }

    /** El aula se aleja/desvanece cuando se entra a un modulo. */
    val exitZoomOut: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        scaleOut(targetScale = 1.08f, animationSpec = tween(220)) + fadeOut(animationSpec = tween(180))
    }

    /** El aula reaparece "alejandose de la camara" al volver de un modulo. */
    val popEnterZoomBack: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        scaleIn(initialScale = 1.1f, animationSpec = tween(300)) + fadeIn(animationSpec = tween(260))
    }

    /** Un modulo se aleja/desvanece cuando el nino/a vuelve al aula. */
    val popExitZoomBack: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        scaleOut(targetScale = 0.9f, animationSpec = tween(220)) + fadeOut(animationSpec = tween(180))
    }
}
