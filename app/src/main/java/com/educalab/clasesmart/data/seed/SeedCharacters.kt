package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.CharacterSkillEntity
import com.educalab.clasesmart.data.local.entity.StudentCharacterEntity

/**
 * 10 personajes del reparto de ClaseSmart. No son avatares intercambiables:
 * cada uno tiene un rasgo y habilidades propias que alimentan el motor de
 * formacion de equipos (Modulo 4).
 */
object SeedCharacters {

    val characters = listOf(
        StudentCharacterEntity("alex", "Alex", "Organizado", "sprite_alex", "CONCENTRADO"),
        StudentCharacterEntity("mia", "Mia", "Creativa", "sprite_mia", "FELIZ"),
        StudentCharacterEntity("leo", "Leo", "Curioso", "sprite_leo", "SORPRENDIDO"),
        StudentCharacterEntity("sam", "Sam", "Tranquilo", "sprite_sam", "PENSATIVO"),
        StudentCharacterEntity("dani", "Dani", "Energico", "sprite_dani", "FELIZ"),
        StudentCharacterEntity("noa", "Noa", "Observadora", "sprite_noa", "CONCENTRADO"),
        StudentCharacterEntity("teo", "Teo", "Constructor", "sprite_teo", "ORGULLOSO"),
        StudentCharacterEntity("vera", "Vera", "Investigadora", "sprite_vera", "PENSATIVO"),
        StudentCharacterEntity("bruno", "Bruno", "Comunicador", "sprite_bruno", "COLABORANDO"),
        StudentCharacterEntity("cata", "Cata", "Cuidadosa", "sprite_cata", "PREOCUPADO")
    )

    val skills = listOf(
        CharacterSkillEntity(characterId = "alex", skill = "ORGANIZAR", strength = 3),
        CharacterSkillEntity(characterId = "alex", skill = "CONSTRUIR", strength = 1),
        CharacterSkillEntity(characterId = "mia", skill = "DIBUJAR", strength = 3),
        CharacterSkillEntity(characterId = "mia", skill = "EXPLICAR", strength = 1),
        CharacterSkillEntity(characterId = "leo", skill = "INVESTIGAR", strength = 3),
        CharacterSkillEntity(characterId = "leo", skill = "OBSERVAR", strength = 2),
        CharacterSkillEntity(characterId = "sam", skill = "OBSERVAR", strength = 3),
        CharacterSkillEntity(characterId = "sam", skill = "ORGANIZAR", strength = 1),
        CharacterSkillEntity(characterId = "dani", skill = "CONSTRUIR", strength = 3),
        CharacterSkillEntity(characterId = "dani", skill = "EXPLICAR", strength = 2),
        CharacterSkillEntity(characterId = "noa", skill = "OBSERVAR", strength = 3),
        CharacterSkillEntity(characterId = "noa", skill = "INVESTIGAR", strength = 2),
        CharacterSkillEntity(characterId = "teo", skill = "CONSTRUIR", strength = 3),
        CharacterSkillEntity(characterId = "teo", skill = "ORGANIZAR", strength = 2),
        CharacterSkillEntity(characterId = "vera", skill = "INVESTIGAR", strength = 3),
        CharacterSkillEntity(characterId = "vera", skill = "DIBUJAR", strength = 1),
        CharacterSkillEntity(characterId = "bruno", skill = "EXPLICAR", strength = 3),
        CharacterSkillEntity(characterId = "bruno", skill = "ORGANIZAR", strength = 1),
        CharacterSkillEntity(characterId = "cata", skill = "ORGANIZAR", strength = 2),
        CharacterSkillEntity(characterId = "cata", skill = "OBSERVAR", strength = 2)
    )
}
