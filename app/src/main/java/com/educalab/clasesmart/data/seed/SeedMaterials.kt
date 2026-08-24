package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.MaterialEntity
import com.educalab.clasesmart.data.local.entity.MaterialLocationEntity

/** 20 materiales escolares (Regla: minimo 20 materiales). */
object SeedMaterials {

    val materials = listOf(
        MaterialEntity("mat_lapiz", "Lapiz", "ESCRITURA", "icon_lapiz"),
        MaterialEntity("mat_goma", "Goma de borrar", "ESCRITURA", "icon_goma"),
        MaterialEntity("mat_regla", "Regla", "ESCRITURA", "icon_regla"),
        MaterialEntity("mat_tijeras", "Tijeras de punta redonda", "ARTE", "icon_tijeras", isFragile = true),
        MaterialEntity("mat_pegamento", "Pegamento en barra", "ARTE", "icon_pegamento"),
        MaterialEntity("mat_papel_color", "Papel de colores", "ARTE", "icon_papel"),
        MaterialEntity("mat_pinceles", "Pinceles", "ARTE", "icon_pinceles"),
        MaterialEntity("mat_pintura", "Botes de pintura", "ARTE", "icon_pintura", isFragile = true),
        MaterialEntity("mat_lupa", "Lupa", "CIENCIAS", "icon_lupa", isFragile = true),
        MaterialEntity("mat_recipiente", "Recipiente transparente", "CIENCIAS", "icon_recipiente", isFragile = true),
        MaterialEntity("mat_guantes", "Guantes de observacion", "CIENCIAS", "icon_guantes"),
        MaterialEntity("mat_cuaderno_campo", "Cuaderno de campo", "CIENCIAS", "icon_cuaderno"),
        MaterialEntity("mat_libro_cuentos", "Libro de cuentos", "LECTURA", "icon_libro", isShared = true),
        MaterialEntity("mat_diccionario", "Diccionario ilustrado", "LECTURA", "icon_diccionario", isShared = true),
        MaterialEntity("mat_marcapaginas", "Marcapaginas", "LECTURA", "icon_marcapaginas"),
        MaterialEntity("mat_carpeta", "Carpeta clasificadora", "ORGANIZACION", "icon_carpeta"),
        MaterialEntity("mat_etiquetas", "Etiquetas adhesivas", "ORGANIZACION", "icon_etiquetas"),
        MaterialEntity("mat_reloj_arena", "Reloj de arena", "ORGANIZACION", "icon_reloj_arena", isShared = true),
        MaterialEntity("mat_caja_materiales", "Caja de materiales compartidos", "ORGANIZACION", "icon_caja", isShared = true),
        MaterialEntity("mat_pizarra_mini", "Pizarra individual", "ESCRITURA", "icon_pizarra_mini")
    )

    val locations = materials.mapIndexed { index, m ->
        MaterialLocationEntity(materialId = m.materialId, zone = "ESTANTE", distanceUnits = (index % 4) + 1)
    }
}
