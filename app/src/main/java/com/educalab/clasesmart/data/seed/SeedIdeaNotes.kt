package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.IdeaNoteEntity

/** Notas iniciales de la Pizarra de ideas, para que el tablero no empiece vacio. */
object SeedIdeaNotes {
    val notes = listOf(
        IdeaNoteEntity("note_seed_1", "Feria de ciencias", "AMARILLO", 40f, 60f, 1L),
        IdeaNoteEntity("note_seed_2", "Mural del aula", "AZUL", 220f, 100f, 2L),
        IdeaNoteEntity("note_seed_3", "Repartir tareas", "ROSA", 80f, 260f, 3L),
        IdeaNoteEntity("note_seed_4", "Elegir fecha", "AMARILLO", 240f, 300f, 4L)
    )
}
