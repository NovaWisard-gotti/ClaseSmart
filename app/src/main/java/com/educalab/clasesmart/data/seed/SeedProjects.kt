package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.ProjectEntity
import com.educalab.clasesmart.data.local.entity.ProjectTaskEntity

/**
 * NOTA DE ALCANCE: la especificacion sugiere 10 proyectos. Esta entrega
 * siembra 6 proyectos completos y jugables (con sus 7 tareas cada uno,
 * siguiendo fielmente la mecanica del Modulo 10). Ver docs/BUILD_REPORT.md.
 */
object SeedProjects {

    val projects = listOf(
        ProjectEntity("proj_feria_ideas", "Feria de Ideas", "Organiza una feria donde cada equipo presenta un pequeno invento.", "10-12"),
        ProjectEntity("proj_mural_aula", "Gran Mural del Aula", "Disena y construye un mural colaborativo para decorar el aula.", "8-9"),
        ProjectEntity("proj_obra_teatro", "Pequena Obra de Teatro", "Prepara una funcion breve entre varios equipos.", "10-12"),
        ProjectEntity("proj_jardin_clase", "Jardin de la Clase", "Organiza un pequeno rincon de plantas cuidado entre todos.", "8-9"),
        ProjectEntity("proj_periodico_mural", "Periodico Mural", "Crea un periodico con noticias del aula para todo el curso.", "10-12"),
        ProjectEntity("proj_museo_clase", "Museo de la Clase", "Monta una pequena exposicion con objetos y descubrimientos del curso.", "10-12")
    )

    val tasks = projects.flatMap { project ->
        listOf(
            ProjectTaskEntity("${project.projectId}_t1", project.projectId, 0, "DECIDIR", "Decidir que actividad se va a realizar"),
            ProjectTaskEntity("${project.projectId}_t2", project.projectId, 1, "MATERIALES", "Organizar los materiales necesarios"),
            ProjectTaskEntity("${project.projectId}_t3", project.projectId, 2, "EQUIPO", "Distribuir tareas entre el equipo"),
            ProjectTaskEntity("${project.projectId}_t4", project.projectId, 3, "ESPACIO", "Preparar el espacio de trabajo"),
            ProjectTaskEntity("${project.projectId}_t5", project.projectId, 4, "TIEMPO", "Planificar el tiempo disponible"),
            ProjectTaskEntity("${project.projectId}_t6", project.projectId, 5, "IMPREVISTO", "Resolver un imprevisto de ultima hora"),
            ProjectTaskEntity("${project.projectId}_t7", project.projectId, 6, "PRESENTAR", "Presentar el resultado final")
        )
    }
}
