package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Material escolar fisico representado en la estanteria/mochila. */
@Entity(tableName = "material")
data class MaterialEntity(
    @PrimaryKey val materialId: String,
    val name: String,
    val category: String, // ESCRITURA, CIENCIAS, ARTE, LECTURA, ORGANIZACION
    val iconAssetId: String,
    val isFragile: Boolean = false,
    val isShared: Boolean = false
)

/** Ubicacion actual de un material dentro de la escena (estante, mesa, mochila...). */
@Entity(
    tableName = "material_location",
    foreignKeys = [
        ForeignKey(
            entity = MaterialEntity::class,
            parentColumns = ["materialId"],
            childColumns = ["materialId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("materialId")]
)
data class MaterialLocationEntity(
    @PrimaryKey(autoGenerate = true) val locationRowId: Long = 0,
    val materialId: String,
    val zone: String, // ESTANTE, MOCHILA, MESA_TRABAJO, PAPELERA
    val distanceUnits: Int = 1 // usado por el motor de consecuencias (desplazamiento)
)
