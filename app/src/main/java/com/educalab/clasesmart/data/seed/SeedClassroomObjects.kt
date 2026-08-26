package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.ClassroomDecorationEntity
import com.educalab.clasesmart.data.local.entity.ClassroomObjectEntity

/**
 * 30 objetos interactivos colocados en la escena del aula (Regla del master
 * spec: minimo 30 objetos). Las coordenadas (zoneX, zoneY) son relativas
 * (0f..1f) sobre el lienzo de ClassroomScene.
 */
object SeedClassroomObjects {

    /**
     * Posiciones repartidas en 10 "filas" claramente separadas a lo largo del
     * lienzo vertical alto y scrolleable de ClassroomScene (SCENE_HEIGHT), de
     * forma que ningun objeto se superponga con otro (se calculo el hueco
     * necesario segun objectSizeFor(type) * sizeScale). Los objetos que son
     * variantes "accesorio" de un tipo (p.ej. tiza/borrador de la pizarra,
     * libros de la biblioteca) usan sizeScale < 1 para no verse tan grandes
     * como el objeto principal de su mismo tipo.
     */
    val objects = listOf(
        // Fila 1 - pared frontal: ventanas y carteles
        ClassroomObjectEntity("obj_ventana_1", objectType = "CARTEL", zoneX = 0.14f, zoneY = 0.032f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_cartel_normas", objectType = "CARTEL", zoneX = 0.38f, zoneY = 0.032f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_cartel_cumples", objectType = "CARTEL", zoneX = 0.62f, zoneY = 0.032f, state = "BLOQUEADO", unlockLevel = 4),
        ClassroomObjectEntity("obj_ventana_2", objectType = "CARTEL", zoneX = 0.86f, zoneY = 0.032f, state = "DISPONIBLE", unlockLevel = 1),
        // Fila 2 - pizarra y sus accesorios (mas pequenos)
        ClassroomObjectEntity("obj_pizarra", objectType = "PIZARRA", zoneX = 0.5f, zoneY = 0.132f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_tiza_caja", objectType = "PIZARRA", zoneX = 0.18f, zoneY = 0.132f, state = "DISPONIBLE", unlockLevel = 1, sizeScale = 0.4f),
        ClassroomObjectEntity("obj_borrador", objectType = "PIZARRA", zoneX = 0.82f, zoneY = 0.132f, state = "DISPONIBLE", unlockLevel = 1, sizeScale = 0.4f),
        // Fila 3 - relojes
        ClassroomObjectEntity("obj_reloj", objectType = "RELOJ", zoneX = 0.2f, zoneY = 0.214f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_reloj_arena", objectType = "RELOJ", zoneX = 0.5f, zoneY = 0.214f, state = "DISPONIBLE", unlockLevel = 1, sizeScale = 0.8f),
        ClassroomObjectEntity("obj_calendario", objectType = "RELOJ", zoneX = 0.8f, zoneY = 0.214f, state = "DISPONIBLE", unlockLevel = 1, sizeScale = 0.8f),
        // Fila 4 - estantes
        ClassroomObjectEntity("obj_estante", objectType = "ESTANTE", zoneX = 0.18f, zoneY = 0.295f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_estante_2", objectType = "ESTANTE", zoneX = 0.52f, zoneY = 0.295f, state = "DISPONIBLE", unlockLevel = 1, sizeScale = 0.75f),
        ClassroomObjectEntity("obj_organizador", objectType = "ESTANTE", zoneX = 0.84f, zoneY = 0.295f, state = "DISPONIBLE", unlockLevel = 1, sizeScale = 0.75f),
        // Fila 5 - rincon de lectura
        ClassroomObjectEntity("obj_biblioteca", objectType = "BIBLIOTECA", zoneX = 0.8f, zoneY = 0.386f, state = "BLOQUEADO", unlockLevel = 3),
        ClassroomObjectEntity("obj_libro_1", objectType = "BIBLIOTECA", zoneX = 0.5f, zoneY = 0.386f, state = "BLOQUEADO", unlockLevel = 3, sizeScale = 0.5f),
        ClassroomObjectEntity("obj_libro_2", objectType = "BIBLIOTECA", zoneX = 0.2f, zoneY = 0.386f, state = "BLOQUEADO", unlockLevel = 3, sizeScale = 0.5f),
        // Fila 6 - mesas (fila A)
        ClassroomObjectEntity("obj_pupitres", objectType = "PUPITRES", zoneX = 0.2f, zoneY = 0.482f, state = "BLOQUEADO", unlockLevel = 2),
        ClassroomObjectEntity("obj_mesa_1", objectType = "PUPITRES", zoneX = 0.5f, zoneY = 0.482f, state = "BLOQUEADO", unlockLevel = 2),
        ClassroomObjectEntity("obj_mesa_2", objectType = "PUPITRES", zoneX = 0.8f, zoneY = 0.482f, state = "BLOQUEADO", unlockLevel = 2),
        // Fila 7 - mesas (fila B) y sillas
        ClassroomObjectEntity("obj_mesa_3", objectType = "PUPITRES", zoneX = 0.5f, zoneY = 0.555f, state = "BLOQUEADO", unlockLevel = 2),
        ClassroomObjectEntity("obj_silla_1", objectType = "PUPITRES", zoneX = 0.22f, zoneY = 0.555f, state = "BLOQUEADO", unlockLevel = 2, sizeScale = 0.6f),
        ClassroomObjectEntity("obj_silla_2", objectType = "PUPITRES", zoneX = 0.78f, zoneY = 0.555f, state = "BLOQUEADO", unlockLevel = 2, sizeScale = 0.6f),
        // Fila 8 - alfombra central
        ClassroomObjectEntity("obj_alfombra", objectType = "CARTEL", zoneX = 0.5f, zoneY = 0.636f, state = "DISPONIBLE", unlockLevel = 1, sizeScale = 1.6f),
        // Fila 9 - plantas y papelera
        ClassroomObjectEntity("obj_planta_1", objectType = "PLANTA", zoneX = 0.15f, zoneY = 0.718f, state = "BLOQUEADO", unlockLevel = 6),
        ClassroomObjectEntity("obj_papelera", objectType = "PAPELERA", zoneX = 0.5f, zoneY = 0.718f, state = "DISPONIBLE", unlockLevel = 1),
        ClassroomObjectEntity("obj_planta_2", objectType = "PLANTA", zoneX = 0.85f, zoneY = 0.718f, state = "BLOQUEADO", unlockLevel = 6),
        // Fila 10 - pared trasera: mochilas y puerta
        ClassroomObjectEntity("obj_mochila", objectType = "MOCHILA", zoneX = 0.12f, zoneY = 0.8f, state = "BLOQUEADO", unlockLevel = 4),
        ClassroomObjectEntity("obj_perchero", objectType = "MOCHILA", zoneX = 0.38f, zoneY = 0.8f, state = "BLOQUEADO", unlockLevel = 4),
        ClassroomObjectEntity("obj_puerta", objectType = "PUERTA", zoneX = 0.64f, zoneY = 0.8f, state = "BLOQUEADO", unlockLevel = 5),
        ClassroomObjectEntity("obj_letrero_puerta", objectType = "PUERTA", zoneX = 0.88f, zoneY = 0.8f, state = "BLOQUEADO", unlockLevel = 5, sizeScale = 0.5f)
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
