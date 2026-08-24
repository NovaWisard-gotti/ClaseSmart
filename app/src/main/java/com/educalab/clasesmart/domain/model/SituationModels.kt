package com.educalab.clasesmart.domain.model

enum class SituationCategory { CONVIVENCIA, ORGANIZACION, TIEMPO, MATERIALES, EQUIPO, PLANIFICACION }

data class SituationChoice(
    val optionId: String,
    val actionLabel: String,
    val qualityLevel: Int // 0..2, nunca binario correcto/incorrecto
)

data class SituationOutcome(val consequenceText: String, val xpAwarded: Int, val qualityLevel: Int)

data class ClassroomSituation(
    val situationId: String,
    val title: String,
    val sceneDescription: String,
    val category: SituationCategory,
    val involvedCharacterIds: List<String>,
    val options: List<SituationChoice>
)
