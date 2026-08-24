package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.ActivityStepEntity
import com.educalab.clasesmart.data.local.entity.ClassActivityEntity
import com.educalab.clasesmart.data.local.entity.TimeBlockEntity

object SeedActivitiesAndSchedule {

    val activities = listOf(
        ClassActivityEntity("act_lectura", "Lectura compartida", "LECTURA", 20, "8-9", "mat_libro_cuentos,mat_marcapaginas"),
        ClassActivityEntity("act_ciencias", "Experimento de observacion", "CIENCIAS", 40, "8-9", "mat_lupa,mat_recipiente,mat_cuaderno_campo"),
        ClassActivityEntity("act_arte", "Mural colaborativo", "ARTE", 35, "8-9", "mat_papel_color,mat_pinceles,mat_pintura"),
        ClassActivityEntity("act_matematicas", "Reto de calculo en equipo", "MATEMATICAS", 30, "10-12", "mat_pizarra_mini,mat_lapiz"),
        ClassActivityEntity("act_trabajo_grupal", "Investigacion en grupos", "TRABAJO_GRUPAL", 35, "10-12", "mat_diccionario,mat_carpeta"),
        ClassActivityEntity("act_limpieza", "Orden del aula", "LIMPIEZA", 10, "8-9", ""),
        ClassActivityEntity("act_exposicion", "Presentacion de proyectos", "EXPOSICION", 25, "10-12", "mat_pizarra_mini"),
        ClassActivityEntity("act_recreo", "Recreo", "RECREO", 20, "8-9", "")
    )

    val steps = listOf(
        ActivityStepEntity(activityId = "act_ciencias", orderIndex = 0, instruction = "Observa el objeto con la lupa."),
        ActivityStepEntity(activityId = "act_ciencias", orderIndex = 1, instruction = "Anota lo que ves en el cuaderno de campo."),
        ActivityStepEntity(activityId = "act_ciencias", orderIndex = 2, instruction = "Comparte tu observacion con el grupo."),
        ActivityStepEntity(activityId = "act_arte", orderIndex = 0, instruction = "Elige los colores del mural."),
        ActivityStepEntity(activityId = "act_arte", orderIndex = 1, instruction = "Reparte las zonas entre el equipo."),
        ActivityStepEntity(activityId = "act_arte", orderIndex = 2, instruction = "Pinta y deja secar antes de tocar.")
    )

    /** Jornada de 8 bloques (08:00-13:00 aprox, minutos relativos desde 0). */
    val timeBlocks = listOf(
        TimeBlockEntity("block_1", "Hoy", 0, 40),
        TimeBlockEntity("block_2", "Hoy", 40, 80),
        TimeBlockEntity("block_3", "Hoy", 80, 100, isRecess = true),
        TimeBlockEntity("block_4", "Hoy", 100, 140),
        TimeBlockEntity("block_5", "Hoy", 140, 175),
        TimeBlockEntity("block_6", "Hoy", 175, 210),
        TimeBlockEntity("block_7", "Hoy", 210, 235),
        TimeBlockEntity("block_8", "Hoy", 235, 255)
    )
}
