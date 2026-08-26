package com.educalab.clasesmart.ui.navigation

object ClaseSmartRoutes {
    const val ONBOARDING = "onboarding"
    const val AULA = "aula"
    const val ORGANIZA_DIA = "organiza_dia"
    const val MISION_MATERIALES = "mision_materiales"
    const val EQUIPOS = "equipos"
    const val SITUACIONES = "situaciones"
    const val PIZARRA_IDEAS = "pizarra_ideas"
    const val BIBLIOTECA = "biblioteca"
    const val RELOJ_TIEMPO = "reloj_tiempo"
    const val PROYECTOS = "proyectos"
    const val PROYECTO_DETALLE = "proyecto/{projectId}"
    const val PERFIL = "perfil"
    const val INSIGNIAS = "insignias"

    fun proyecto(projectId: String) = "proyecto/$projectId"
}
