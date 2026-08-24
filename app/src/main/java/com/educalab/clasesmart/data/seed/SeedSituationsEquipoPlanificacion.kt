package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.ClassroomSituationEntity
import com.educalab.clasesmart.data.local.entity.SituationOptionEntity
import com.educalab.clasesmart.data.local.entity.SituationOutcomeEntity

object SeedSituationsEquipoPlanificacion {

    val situations = listOf(
        ClassroomSituationEntity("sit_equipo_1", "Un equipo no se organiza", "El grupo de matematicas lleva 5 minutos sin decidir quien hace que.", "EQUIPO", "10-12", "alex,dani,cata"),
        ClassroomSituationEntity("sit_equipo_2", "Todos quieren dibujar", "En el equipo de arte, nadie quiere encargarse de explicar el resultado.", "EQUIPO", "8-9", "mia,bruno"),
        ClassroomSituationEntity("sit_equipo_3", "Falta alguien que investigue", "El equipo de ciencias no tiene a nadie comodo buscando informacion.", "EQUIPO", "10-12", "teo,sam"),
        ClassroomSituationEntity("sit_equipo_4", "Un compañero se queda callado", "Vera no esta participando en las decisiones del equipo.", "EQUIPO", "10-12", "vera,leo"),
        ClassroomSituationEntity("sit_equipo_5", "Dos lideres a la vez", "Alex y Noa quieren decidir el mismo paso del proyecto por su cuenta.", "EQUIPO", "10-12", "alex,noa"),

        ClassroomSituationEntity("sit_plan_1", "El proyecto no tiene primer paso", "El equipo de la Feria de Ideas no sabe por donde empezar a planificar.", "PLANIFICACION", "10-12", "bruno,vera,teo"),
        ClassroomSituationEntity("sit_plan_2", "Cambio de ultimo momento", "El espacio reservado para la exposicion ya no esta disponible.", "PLANIFICACION", "10-12", "cata,alex"),
        ClassroomSituationEntity("sit_plan_3", "Demasiadas ideas a la vez", "La pizarra de ideas se lleno de propuestas sin ningun orden.", "PLANIFICACION", "10-12", "mia,leo,dani"),
        ClassroomSituationEntity("sit_plan_4", "Nadie anoto el plan", "El equipo decidio los pasos de palabra, pero nadie los escribio.", "PLANIFICACION", "10-12", "sam,noa"),
        ClassroomSituationEntity("sit_plan_5", "El plan no dejo tiempo para imprevistos", "El proyecto va bien, pero un imprevisto puede dejarlos sin margen.", "PLANIFICACION", "10-12", "teo,cata,bruno")
    )

    val options = listOf(
        SituationOptionEntity("opt_equipo1_a", "sit_equipo_1", "Repartir tareas segun lo que cada uno hace mejor", 2),
        SituationOptionEntity("opt_equipo1_b", "sit_equipo_1", "Repartir tareas al azar para empezar ya", 1),
        SituationOptionEntity("opt_equipo1_c", "sit_equipo_1", "Seguir sin repartir nada", 0),

        SituationOptionEntity("opt_equipo2_a", "sit_equipo_2", "Buscar quien se sienta comodo explicando, aunque no sea quien mas dibuja", 2),
        SituationOptionEntity("opt_equipo2_b", "sit_equipo_2", "Elegir a quien este mas cerca de la pizarra", 1),
        SituationOptionEntity("opt_equipo2_c", "sit_equipo_2", "No presentar el resultado del equipo", 0),

        SituationOptionEntity("opt_equipo3_a", "sit_equipo_3", "Sumar a alguien de otro equipo que investiga bien", 2),
        SituationOptionEntity("opt_equipo3_b", "sit_equipo_3", "Repartir la busqueda entre todos igual, sepan o no", 1),
        SituationOptionEntity("opt_equipo3_c", "sit_equipo_3", "Dejar la investigacion sin hacer", 0),

        SituationOptionEntity("opt_equipo4_a", "sit_equipo_4", "Preguntarle a Vera directamente que opina", 2),
        SituationOptionEntity("opt_equipo4_b", "sit_equipo_4", "Seguir decidiendo sin contar con ella", 1),
        SituationOptionEntity("opt_equipo4_c", "sit_equipo_4", "Decirle que no participa nunca delante de todos", 0),

        SituationOptionEntity("opt_equipo5_a", "sit_equipo_5", "Repartir ese paso en dos partes complementarias", 2),
        SituationOptionEntity("opt_equipo5_b", "sit_equipo_5", "Decidir con una moneda quien manda ese paso", 1),
        SituationOptionEntity("opt_equipo5_c", "sit_equipo_5", "Discutir sin llegar a ponerse de acuerdo", 0),

        SituationOptionEntity("opt_plan1_a", "sit_plan_1", "Escribir juntos los primeros tres pasos", 2),
        SituationOptionEntity("opt_plan1_b", "sit_plan_1", "Empezar por lo primero que se les ocurra", 1),
        SituationOptionEntity("opt_plan1_c", "sit_plan_1", "Seguir sin decidir nada mas tiempo", 0),

        SituationOptionEntity("opt_plan2_a", "sit_plan_2", "Buscar juntos un espacio alternativo ahora mismo", 2),
        SituationOptionEntity("opt_plan2_b", "sit_plan_2", "Esperar a ver si el espacio se libera solo", 1),
        SituationOptionEntity("opt_plan2_c", "sit_plan_2", "Cancelar la exposicion sin buscar alternativa", 0),

        SituationOptionEntity("opt_plan3_a", "sit_plan_3", "Agrupar las ideas parecidas antes de seguir", 2),
        SituationOptionEntity("opt_plan3_b", "sit_plan_3", "Elegir la primera idea que aparecio", 1),
        SituationOptionEntity("opt_plan3_c", "sit_plan_3", "Dejar todas las ideas sueltas sin ordenar", 0),

        SituationOptionEntity("opt_plan4_a", "sit_plan_4", "Escribir el plan en la pizarra antes de seguir", 2),
        SituationOptionEntity("opt_plan4_b", "sit_plan_4", "Confiar en que todos lo recuerden igual", 1),
        SituationOptionEntity("opt_plan4_c", "sit_plan_4", "Seguir sin anotar nada", 0),

        SituationOptionEntity("opt_plan5_a", "sit_plan_5", "Dejar un bloque de tiempo libre por si surge algo", 2),
        SituationOptionEntity("opt_plan5_b", "sit_plan_5", "Seguir con el plan tal cual, sin margen", 1),
        SituationOptionEntity("opt_plan5_c", "sit_plan_5", "No pensar en los imprevistos hasta que pasen", 0)
    )

    val outcomes = listOf(
        SituationOutcomeEntity("out_equipo1_a", "opt_equipo1_a", "Cada quien trabajo en lo que se le daba mejor y avanzaron rapido.", 15),
        SituationOutcomeEntity("out_equipo1_b", "opt_equipo1_b", "El equipo avanzo, aunque algunas tareas costaron mas de lo normal.", 8),
        SituationOutcomeEntity("out_equipo1_c", "opt_equipo1_c", "El equipo siguio parado sin avanzar en la actividad.", 2),

        SituationOutcomeEntity("out_equipo2_a", "opt_equipo2_a", "La presentacion salio clara y todos se sintieron utiles.", 15),
        SituationOutcomeEntity("out_equipo2_b", "opt_equipo2_b", "La presentacion salio adelante, aunque con algo de improvisacion.", 8),
        SituationOutcomeEntity("out_equipo2_c", "opt_equipo2_c", "El trabajo del equipo no se llego a mostrar.", 2),

        SituationOutcomeEntity("out_equipo3_a", "opt_equipo3_a", "El equipo consiguio la informacion que necesitaba a tiempo.", 15),
        SituationOutcomeEntity("out_equipo3_b", "opt_equipo3_b", "Consiguieron algo de informacion, aunque les costo bastante.", 8),
        SituationOutcomeEntity("out_equipo3_c", "opt_equipo3_c", "El proyecto quedo sin la informacion que necesitaba.", 2),

        SituationOutcomeEntity("out_equipo4_a", "opt_equipo4_a", "Vera compartio una idea que mejoro el plan del equipo.", 15),
        SituationOutcomeEntity("out_equipo4_b", "opt_equipo4_b", "El equipo siguio adelante sin la idea de Vera.", 8),
        SituationOutcomeEntity("out_equipo4_c", "opt_equipo4_c", "Vera se sintio peor y participo todavia menos.", 2),

        SituationOutcomeEntity("out_equipo5_a", "opt_equipo5_a", "Alex y Noa combinaron sus ideas en un paso mejor.", 15),
        SituationOutcomeEntity("out_equipo5_b", "opt_equipo5_b", "Se resolvio rapido, aunque uno de los dos quedo algo descontento.", 8),
        SituationOutcomeEntity("out_equipo5_c", "opt_equipo5_c", "El equipo perdio tiempo sin avanzar ese paso.", 2),

        SituationOutcomeEntity("out_plan1_a", "opt_plan1_a", "El proyecto arranco con una base clara para seguir.", 15),
        SituationOutcomeEntity("out_plan1_b", "opt_plan1_b", "El proyecto arranco, aunque tuvieron que corregir el rumbo despues.", 8),
        SituationOutcomeEntity("out_plan1_c", "opt_plan1_c", "El proyecto sigue sin un primer paso claro.", 2),

        SituationOutcomeEntity("out_plan2_a", "opt_plan2_a", "Encontraron un espacio nuevo y la exposicion siguio en pie.", 15),
        SituationOutcomeEntity("out_plan2_b", "opt_plan2_b", "El espacio aparecio justo a tiempo, con poco margen.", 8),
        SituationOutcomeEntity("out_plan2_c", "opt_plan2_c", "La exposicion se quedo sin lugar donde hacerse.", 2),

        SituationOutcomeEntity("out_plan3_a", "opt_plan3_a", "La pizarra quedo ordenada y facil de seguir para todos.", 15),
        SituationOutcomeEntity("out_plan3_b", "opt_plan3_b", "Avanzaron con una idea, aunque quiza no era la mas completa.", 8),
        SituationOutcomeEntity("out_plan3_c", "opt_plan3_c", "La pizarra siguio siendo dificil de entender para el equipo.", 2),

        SituationOutcomeEntity("out_plan4_a", "opt_plan4_a", "Todo el equipo pudo consultar el plan cuando hizo falta.", 15),
        SituationOutcomeEntity("out_plan4_b", "opt_plan4_b", "Casi todos recordaron el plan, con alguna pequena diferencia.", 8),
        SituationOutcomeEntity("out_plan4_c", "opt_plan4_c", "Cada uno recordo el plan de una forma distinta.", 2),

        SituationOutcomeEntity("out_plan5_a", "opt_plan5_a", "Cuando surgio un imprevisto, el equipo tuvo tiempo para resolverlo.", 15),
        SituationOutcomeEntity("out_plan5_b", "opt_plan5_b", "El imprevisto se resolvio, pero atraso el resto del plan.", 8),
        SituationOutcomeEntity("out_plan5_c", "opt_plan5_c", "El imprevisto dejo al equipo sin tiempo para terminar.", 2)
    )
}
