package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.ClassroomSituationEntity
import com.educalab.clasesmart.data.local.entity.SituationOptionEntity
import com.educalab.clasesmart.data.local.entity.SituationOutcomeEntity

/**
 * NOTA DE ALCANCE: la especificacion sugiere 10 situaciones por categoria
 * (60 en total). Para esta entrega v1.0.0 se sembraron 5 situaciones REALES
 * y completas por categoria (30 en total: ver tambien SeedSituationsTiempoMateriales.kt
 * y SeedSituationsEquipoPlanificacion.kt), priorizando profundidad y calidad
 * narrativa sobre cantidad. Ver docs/BUILD_REPORT.md, seccion "Simplificaciones".
 */
object SeedSituationsConvivenciaOrganizacion {

    val situations = listOf(
        ClassroomSituationEntity("sit_conv_1", "El mismo material", "Alex y Mia quieren usar la unica lupa al mismo tiempo.", "CONVIVENCIA", "8-9", "alex,mia"),
        ClassroomSituationEntity("sit_conv_2", "El material olvidado", "Leo llego sin su carpeta de trabajo grupal.", "CONVIVENCIA", "8-9", "leo,sam"),
        ClassroomSituationEntity("sit_conv_3", "El companero interrumpe", "Dani sigue hablando mientras Noa intenta explicar su idea.", "CONVIVENCIA", "10-12", "dani,noa"),
        ClassroomSituationEntity("sit_conv_4", "Alguien se quedo fuera", "El grupo eligio equipo y Cata se quedo sin sitio.", "CONVIVENCIA", "8-9", "cata,teo"),
        ClassroomSituationEntity("sit_conv_5", "Un error sin querer", "Bruno tiro sin querer el mural que preparaba Vera.", "CONVIVENCIA", "10-12", "bruno,vera"),

        ClassroomSituationEntity("sit_org_1", "El aula quedo desordenada", "Despues de arte, los pinceles quedaron por todas las mesas.", "ORGANIZACION", "8-9", "mia,teo"),
        ClassroomSituationEntity("sit_org_2", "El grupo no sabe por donde empezar", "El equipo de ciencias mira el material sin decidir el primer paso.", "ORGANIZACION", "10-12", "leo,vera"),
        ClassroomSituationEntity("sit_org_3", "Demasiadas cosas sobre la mesa", "La mesa de Sam tiene materiales de tres actividades distintas mezclados.", "ORGANIZACION", "8-9", "sam"),
        ClassroomSituationEntity("sit_org_4", "Nadie recogio la pizarra", "La pizarra sigue llena de la clase anterior y hace falta para hoy.", "ORGANIZACION", "8-9", "alex,dani"),
        ClassroomSituationEntity("sit_org_5", "El plan cambio a mitad de actividad", "A mitad del mural, el equipo decide cambiar el diseno sin avisar a todos.", "ORGANIZACION", "10-12", "noa,cata,bruno")
    )

    val options = listOf(
        // sit_conv_1
        SituationOptionEntity("opt_conv1_a", "sit_conv_1", "Repartir el tiempo de uso de la lupa", 2),
        SituationOptionEntity("opt_conv1_b", "sit_conv_1", "Decirle a Mia que espere, sin mas", 1),
        SituationOptionEntity("opt_conv1_c", "sit_conv_1", "Quedarse Alex con la lupa todo el rato", 0),
        // sit_conv_2
        SituationOptionEntity("opt_conv2_a", "sit_conv_2", "Compartir la carpeta de Sam por turnos", 2),
        SituationOptionEntity("opt_conv2_b", "sit_conv_2", "Dejar que Leo trabaje sin carpeta como pueda", 1),
        SituationOptionEntity("opt_conv2_c", "sit_conv_2", "Decirle a Leo que deberia haberse acordado", 0),
        // sit_conv_3
        SituationOptionEntity("opt_conv3_a", "sit_conv_3", "Proponer turnos para hablar", 2),
        SituationOptionEntity("opt_conv3_b", "sit_conv_3", "Pedirle a Dani que baje la voz", 1),
        SituationOptionEntity("opt_conv3_c", "sit_conv_3", "Dejar que Noa no termine de explicar", 0),
        // sit_conv_4
        SituationOptionEntity("opt_conv4_a", "sit_conv_4", "Abrir un puesto mas en el equipo para Cata", 2),
        SituationOptionEntity("opt_conv4_b", "sit_conv_4", "Decirle a Cata que se una a otro grupo cualquiera", 1),
        SituationOptionEntity("opt_conv4_c", "sit_conv_4", "No hacer nada y seguir sin ella", 0),
        // sit_conv_5
        SituationOptionEntity("opt_conv5_a", "sit_conv_5", "Ayudar entre todos a rehacer la parte danada", 2),
        SituationOptionEntity("opt_conv5_b", "sit_conv_5", "Pedir disculpas y seguir sin arreglarlo", 1),
        SituationOptionEntity("opt_conv5_c", "sit_conv_5", "Culpar a Bruno delante del grupo", 0),
        // sit_org_1
        SituationOptionEntity("opt_org1_a", "sit_org_1", "Organizar entre todos donde va cada pincel", 2),
        SituationOptionEntity("opt_org1_b", "sit_org_1", "Dejarlos amontonados en el estante", 1),
        SituationOptionEntity("opt_org1_c", "sit_org_1", "Dejarlos donde estan y empezar otra actividad", 0),
        // sit_org_2
        SituationOptionEntity("opt_org2_a", "sit_org_2", "Escribir juntos los pasos antes de tocar el material", 2),
        SituationOptionEntity("opt_org2_b", "sit_org_2", "Empezar cada uno por su cuenta", 1),
        SituationOptionEntity("opt_org2_c", "sit_org_2", "Esperar a que alguien mas decida por ellos", 0),
        // sit_org_3
        SituationOptionEntity("opt_org3_a", "sit_org_3", "Separar los materiales por actividad antes de seguir", 2),
        SituationOptionEntity("opt_org3_b", "sit_org_3", "Apartar solo lo que se usa ahora mismo", 1),
        SituationOptionEntity("opt_org3_c", "sit_org_3", "Seguir trabajando con todo mezclado", 0),
        // sit_org_4
        SituationOptionEntity("opt_org4_a", "sit_org_4", "Borrarla entre dos antes de empezar", 2),
        SituationOptionEntity("opt_org4_b", "sit_org_4", "Esperar a que la profesora la borre", 1),
        SituationOptionEntity("opt_org4_c", "sit_org_4", "Escribir encima sin borrar", 0),
        // sit_org_5
        SituationOptionEntity("opt_org5_a", "sit_org_5", "Reunir al equipo un minuto para contar el cambio", 2),
        SituationOptionEntity("opt_org5_b", "sit_org_5", "Avisar solo a quien esta al lado", 1),
        SituationOptionEntity("opt_org5_c", "sit_org_5", "Seguir cada uno con el plan que tenia antes", 0)
    )

    val outcomes = listOf(
        SituationOutcomeEntity("out_conv1_a", "opt_conv1_a", "Los dos pudieron observar y nadie se quedo esperando de mas.", 15),
        SituationOutcomeEntity("out_conv1_b", "opt_conv1_b", "Mia espero, pero se perdio parte de la actividad mientras tanto.", 8),
        SituationOutcomeEntity("out_conv1_c", "opt_conv1_c", "Mia no pudo observar nada durante toda la actividad.", 2),

        SituationOutcomeEntity("out_conv2_a", "opt_conv2_a", "Ambos pudieron trabajar y Leo aprendio a pedir ayuda a tiempo.", 15),
        SituationOutcomeEntity("out_conv2_b", "opt_conv2_b", "Leo pudo seguir, pero le costo mas de lo necesario.", 8),
        SituationOutcomeEntity("out_conv2_c", "opt_conv2_c", "Leo se quedo sin poder participar en la actividad.", 2),

        SituationOutcomeEntity("out_conv3_a", "opt_conv3_a", "Todos pudieron hablar y Noa termino de explicar su idea.", 15),
        SituationOutcomeEntity("out_conv3_b", "opt_conv3_b", "Dani bajo la voz, pero Noa tuvo que repetir parte de lo dicho.", 8),
        SituationOutcomeEntity("out_conv3_c", "opt_conv3_c", "Noa no pudo compartir su idea con el grupo.", 2),

        SituationOutcomeEntity("out_conv4_a", "opt_conv4_a", "Cata se sumo al equipo y aporto una habilidad que faltaba.", 15),
        SituationOutcomeEntity("out_conv4_b", "opt_conv4_b", "Cata encontro sitio, pero no en el equipo donde queria estar.", 8),
        SituationOutcomeEntity("out_conv4_c", "opt_conv4_c", "Cata se quedo sin equipo para la actividad.", 2),

        SituationOutcomeEntity("out_conv5_a", "opt_conv5_a", "El mural quedo reparado y el equipo aprendio a resolverlo juntos.", 15),
        SituationOutcomeEntity("out_conv5_b", "opt_conv5_b", "Vera acepto las disculpas, pero el mural quedo sin terminar.", 8),
        SituationOutcomeEntity("out_conv5_c", "opt_conv5_c", "Bruno se sintio mal y el mural tampoco se arreglo.", 2),

        SituationOutcomeEntity("out_org1_a", "opt_org1_a", "El estante quedo listo y la siguiente clase encontrara todo rapido.", 15),
        SituationOutcomeEntity("out_org1_b", "opt_org1_b", "Los pinceles estan guardados, pero sera dificil encontrarlos despues.", 8),
        SituationOutcomeEntity("out_org1_c", "opt_org1_c", "El desorden sigue igual para la siguiente actividad.", 2),

        SituationOutcomeEntity("out_org2_a", "opt_org2_a", "El equipo empezo con un plan claro y avanzo mas rapido.", 15),
        SituationOutcomeEntity("out_org2_b", "opt_org2_b", "Cada quien avanzo por su lado y luego costo juntar el trabajo.", 8),
        SituationOutcomeEntity("out_org2_c", "opt_org2_c", "El grupo perdio varios minutos sin empezar nada.", 2),

        SituationOutcomeEntity("out_org3_a", "opt_org3_a", "Sam pudo trabajar con espacio y sin confundir materiales.", 15),
        SituationOutcomeEntity("out_org3_b", "opt_org3_b", "Sam pudo seguir, aunque la mesa siguio algo apretada.", 8),
        SituationOutcomeEntity("out_org3_c", "opt_org3_c", "Sam mezclo materiales de actividades distintas por error.", 2),

        SituationOutcomeEntity("out_org4_a", "opt_org4_a", "La pizarra quedo lista justo a tiempo para la clase.", 15),
        SituationOutcomeEntity("out_org4_b", "opt_org4_b", "La clase empezo un poco tarde esperando a que la borraran.", 8),
        SituationOutcomeEntity("out_org4_c", "opt_org4_c", "La pizarra quedo dificil de leer, mezclada con lo anterior.", 2),

        SituationOutcomeEntity("out_org5_a", "opt_org5_a", "Todo el equipo siguio el nuevo plan a la vez, sin confusiones.", 15),
        SituationOutcomeEntity("out_org5_b", "opt_org5_b", "Una parte del equipo se entero tarde del cambio.", 8),
        SituationOutcomeEntity("out_org5_c", "opt_org5_c", "El mural quedo con dos diseños distintos mezclados.", 2)
    )
}
