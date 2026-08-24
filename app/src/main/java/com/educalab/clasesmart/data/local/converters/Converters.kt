package com.educalab.clasesmart.data.local.converters

import androidx.room.TypeConverter

/**
 * Conversores minimos de Room. Se evita deliberadamente serializar listas
 * completas como JSON generico: los campos "idsSeparadosPorComa" se
 * modelan como String y se parsean en el Repository, manteniendo el
 * esquema simple e inspeccionable en database/schema.sql.
 */
class Converters {
    @TypeConverter
    fun fromCsv(value: String?): List<String> =
        value?.takeIf { it.isNotBlank() }?.split(",")?.map { it.trim() } ?: emptyList()

    @TypeConverter
    fun toCsv(list: List<String>?): String = list?.joinToString(",") ?: ""
}
