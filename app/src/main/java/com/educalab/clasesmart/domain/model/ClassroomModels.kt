package com.educalab.clasesmart.domain.model

/** Estado visual estandar de cualquier modulo/objeto del aula (Regla 19 del master spec). */
enum class ModuleState {
    BLOQUEADO,
    DISPONIBLE,
    INICIADO,
    COMPLETADO,
    DOMINADO
}

enum class ClassroomObjectType {
    PIZARRA, RELOJ, ESTANTE, PUPITRES, BIBLIOTECA, MOCHILA, PUERTA, CARTEL, PLANTA, PAPELERA
}

data class ClassroomObject(
    val objectId: String,
    val type: ClassroomObjectType,
    val zoneX: Float,
    val zoneY: Float,
    val state: ModuleState,
    val unlockLevel: Int,
    val sizeScale: Float = 1f
)

data class ClassroomSnapshot(
    val level: Int,
    val objects: List<ClassroomObject>,
    val activeDecorationIds: Set<String>
)
