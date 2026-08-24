package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.BadgeEntity

/** 12 insignias (Regla: minimo 12), alineadas 1:1 con BadgeEngine.rules. */
object SeedBadges {
    val badges = listOf(
        BadgeEntity("gran_organizador", "Gran Organizador", "Organizaste el horario del aula sin dejar nada suelto.", "icon_badge_organizador", "ORGANIZACION"),
        BadgeEntity("maestro_del_tiempo", "Maestro del Tiempo", "Sabes calcular cuanto dura cada cosa y que cabe en la jornada.", "icon_badge_tiempo", "TIEMPO"),
        BadgeEntity("equipo_imparable", "Equipo Imparable", "Formaste equipos donde nadie se quedaba sin apoyo.", "icon_badge_equipo", "EQUIPO"),
        BadgeEntity("guardian_de_materiales", "Guardian de Materiales", "Preparaste los materiales exactos para cada actividad.", "icon_badge_materiales", "MATERIALES"),
        BadgeEntity("solucionador_de_problemas", "Solucionador de Problemas", "Encontraste buenas soluciones en situaciones dificiles.", "icon_badge_solucionador", "CONVIVENCIA"),
        BadgeEntity("gran_comunicador", "Gran Comunicador", "Ayudaste a que todos se entendieran mejor.", "icon_badge_comunicador", "COMUNICACION"),
        BadgeEntity("constructor_de_ideas", "Constructor de Ideas", "Llenaste la pizarra de ideas conectadas.", "icon_badge_ideas", "PROYECTO"),
        BadgeEntity("ayudante_del_aula", "Ayudante del Aula", "Resolviste muchas situaciones ayudando a companeros.", "icon_badge_ayudante", "CONVIVENCIA"),
        BadgeEntity("planificador_creativo", "Planificador Creativo", "Tus horarios funcionan una y otra vez.", "icon_badge_planificador", "ORGANIZACION"),
        BadgeEntity("experto_en_colaboracion", "Experto en Colaboracion", "Formaste muchos equipos que se complementan.", "icon_badge_colaboracion", "EQUIPO"),
        BadgeEntity("cuidador_del_espacio", "Cuidador del Espacio", "Cuidaste y decoraste el aula con esfuerzo real.", "icon_badge_cuidador", "ORGANIZACION"),
        BadgeEntity("lider_de_proyecto", "Lider de Proyecto", "Llevaste un gran proyecto del aula hasta el final.", "icon_badge_lider", "PROYECTO")
    )
}
