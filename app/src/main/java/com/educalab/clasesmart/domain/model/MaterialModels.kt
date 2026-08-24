package com.educalab.clasesmart.domain.model

enum class MaterialCategory { ESCRITURA, CIENCIAS, ARTE, LECTURA, ORGANIZACION }
enum class MaterialZone { ESTANTE, MOCHILA, MESA_TRABAJO, PAPELERA }

data class SchoolMaterial(
    val materialId: String,
    val name: String,
    val category: MaterialCategory,
    val isFragile: Boolean = false,
    val currentZone: MaterialZone = MaterialZone.ESTANTE,
    val distanceUnits: Int = 1
)

data class MaterialMission(
    val missionId: String,
    val activityTitle: String,
    val requiredMaterialIds: List<String>
)

sealed class MaterialIssue(val message: String) {
    data class Falta(val materialName: String) : MaterialIssue("Falta \"$materialName\" para poder empezar.")
    data class DemasiadoLejos(val materialName: String, val distancia: Int) :
        MaterialIssue("\"$materialName\" quedo demasiado lejos (a $distancia pasos de la mesa de trabajo).")
    data class ExtraSinUso(val materialName: String) :
        MaterialIssue("\"$materialName\" no hace falta para esta actividad; puede volver a su sitio.")
}

data class MaterialEvaluation(
    val placedOnTable: List<SchoolMaterial>,
    val issues: List<MaterialIssue>,
    val isReady: Boolean
)
