package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.ClassroomDecorationEntity
import com.educalab.clasesmart.data.local.entity.ClassroomObjectEntity

/**
 * 30 objetos interactivos colocados en la escena del aula (Regla del master
 * spec: minimo 30 objetos). Las coordenadas (zoneX, zoneY) son relativas
 * (0f..1f) sobre el lienzo de ClassroomScene.
 */
object SeedClassroomObjects {

    val objects = listOf(
        ClassroomObjectEntity("obj_pizarra", objectType = "PIZARRA", zoneX = 0.5f, zoneY = 0.18f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_reloj", objectType = "RELOJ", zoneX = 0.82f, zoneY = 0.14f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_estante", objectType = "ESTANTE", zoneX = 0.12f, zoneY = 0.32f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_pupitres", objectType = "PUPITRES", zoneX = 0.5f, zoneY = 0.6f, state = "BLOQUEADO", unlockLevel = 2),
        ClassroomObjectEntity("obj_biblioteca", objectType = "BIBLIOTECA", zoneX = 0.85f, zoneY = 0.55f, state = "BLOQUEADO", unlockLevel = 3),
        ClassroomObjectEntity("obj_mochila", objectType = "MOCHILA", zoneX = 0.2f, zoneY = 0.78f, state = "BLOQUEADO", unlockLevel = 4),
        ClassroomObjectEntity("obj_puerta", objectType = "PUERTA", zoneX = 0.06f, zoneY = 0.6f, state = "BLOQUEADO", unlockLevel = 5),
        ClassroomObjectEntity("obj_cartel_normas", objectType = "CARTEL", zoneX = 0.32f, zoneY = 0.1f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_cartel_cumples", objectType = "CARTEL", zoneX = 0.68f, zoneY = 0.08f, state = "BLOQUEADO", unlockLevel = 4),
        ClassroomObjectEntity("obj_planta_1", objectType = "PLANTA", zoneX = 0.05f, zoneY = 0.15f, state = "BLOQUEADO", unlockLevel = 6),
        ClassroomObjectEntity("obj_planta_2", objectType = "PLANTA", zoneX = 0.95f, zoneY = 0.78f, state = "BLOQUEADO", unlockLevel = 6),
        ClassroomObjectEntity("obj_papelera", objectType = "PAPELERA", zoneX = 0.9f, zoneY = 0.9f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_ventana_1", objectType = "CARTEL", zoneX = 0.15f, zoneY = 0.05f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_ventana_2", objectType = "CARTEL", zoneX = 0.85f, zoneY = 0.05f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_mesa_1", objectType = "PUPITRES", zoneX = 0.3f, zoneY = 0.55f, state = "BLOQUEADO", unlockLevel = 2),
        ClassroomObjectEntity("obj_mesa_2", objectType = "PUPITRES", zoneX = 0.7f, zoneY = 0.55f, state = "BLOQUEADO", unlockLevel = 2),
        ClassroomObjectEntity("obj_mesa_3", objectType = "PUPITRES", zoneX = 0.5f, zoneY = 0.72f, state = "BLOQUEADO", unlockLevel = 2),
        ClassroomObjectEntity("obj_estante_2", objectType = "ESTANTE", zoneX = 0.12f, zoneY = 0.45f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_libro_1", objectType = "BIBLIOTECA", zoneX = 0.83f, zoneY = 0.45f, state = "BLOQUEADO", unlockLevel = 3),
        ClassroomObjectEntity("obj_libro_2", objectType = "BIBLIOTECA", zoneX = 0.88f, zoneY = 0.62f, state = "BLOQUEADO", unlockLevel = 3),
        ClassroomObjectEntity("obj_reloj_arena", objectType = "RELOJ", zoneX = 0.75f, zoneY = 0.2f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_tiza_caja", objectType = "PIZARRA", zoneX = 0.42f, zoneY = 0.24f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_borrador", objectType = "PIZARRA", zoneX = 0.58f, zoneY = 0.24f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_silla_1", objectType = "PUPITRES", zoneX = 0.28f, zoneY = 0.63f, state = "BLOQUEADO", unlockLevel = 2),
        ClassroomObjectEntity("obj_silla_2", objectType = "PUPITRES", zoneX = 0.72f, zoneY = 0.63f, state = "BLOQUEADO", unlockLevel = 2),
        ClassroomObjectEntity("obj_alfombra", objectType = "CARTEL", zoneX = 0.5f, zoneY = 0.85f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_perchero", objectType = "MOCHILA", zoneX = 0.08f, zoneY = 0.78f, state = "BLOQUEADO", unlockLevel = 4),
        ClassroomObjectEntity("obj_calendario", objectType = "RELOJ", zoneX = 0.9f, zoneY = 0.28f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_organizador", objectType = "ESTANTE", zoneX = 0.18f, zoneY = 0.38f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_letrero_puerta", objectType = "PUERTA", zoneX = 0.1f, zoneY = 0.52f, state = "BLOQUEADO", unlockLevel = 5)
    )

    /** 20 decoraciones desbloqueables por XP real (Regla: minimo 20 decoraciones). */
    val decorations = listOf(
        ClassroomDecorationEntity("dec_cartel_estrellas", "Cartel de estrellas", "CARTEL", "Alcanza el nivel 2", 60),
        ClassroomDecorationEntity("dec_cartel_planetas", "Cartel de planetas", "CARTEL", "Alcanza el nivel 3", 150),
        ClassroomDecorationEntity("dec_planta_cactus", "Cactus de escritorio", "PLANTA", "Completa 3 misiones de materiales", 90),
        ClassroomDecorationEntity("dec_planta_grande", "Planta grande de rincon", "PLANTA", "Alcanza el nivel 6", 660),
        ClassroomDecorationEntity("dec_pizarra_marco_madera", "Marco de madera para la pizarra", "PIZARRA_BORDE", "Organiza 3 horarios sin problemas", 100),
        ClassroomDecorationEntity("dec_pizarra_marco_color", "Marco de colores para la pizarra", "PIZARRA_BORDE", "Organiza 5 horarios sin problemas", 200),
        ClassroomDecorationEntity("dec_estante_cestas", "Cestas organizadoras", "ESTANTE_ADORNO", "Prepara 5 misiones de materiales", 180),
        ClassroomDecorationEntity("dec_estante_etiquetas", "Etiquetas de colores", "ESTANTE_ADORNO", "Prepara 8 misiones de materiales", 260),
        ClassroomDecorationEntity("dec_color_mesa_azul", "Mesas color azul", "COLOR_MUEBLE", "Forma 2 equipos completos", 80),
        ClassroomDecorationEntity("dec_color_mesa_verde", "Mesas color verde", "COLOR_MUEBLE", "Forma 4 equipos completos", 160),
        ClassroomDecorationEntity("dec_color_mesa_naranja", "Mesas color naranja", "COLOR_MUEBLE", "Forma 6 equipos completos", 280),
        ClassroomDecorationEntity("dec_cartel_cumples", "Cartel de cumpleanos", "CARTEL", "Alcanza el nivel 4", 280),
        ClassroomDecorationEntity("dec_alfombra_circulo", "Alfombra de circulo", "CARTEL", "Resuelve 5 situaciones de convivencia", 120),
        ClassroomDecorationEntity("dec_alfombra_mapa", "Alfombra de mapa del mundo", "CARTEL", "Resuelve 10 situaciones de convivencia", 240),
        ClassroomDecorationEntity("dec_movil_estrellas", "Movil de estrellas colgante", "CARTEL", "Completa el primer proyecto del aula", 300),
        ClassroomDecorationEntity("dec_guirnalda_banderines", "Guirnalda de banderines", "CARTEL", "Completa 2 proyectos del aula", 500),
        ClassroomDecorationEntity("dec_reloj_sol", "Reloj de sol decorativo", "CARTEL", "Resuelve 5 retos de tiempo", 150),
        ClassroomDecorationEntity("dec_perchero_colores", "Perchero de colores", "CARTEL", "Alcanza el nivel 4", 280),
        ClassroomDecorationEntity("dec_marco_fotos", "Marco de fotos del equipo", "CARTEL", "Forma 8 equipos completos", 360),
        ClassroomDecorationEntity("dec_luces_calidas", "Luces calidas de rincon", "CARTEL", "Alcanza el nivel 6", 660)
    )
}
