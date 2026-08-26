package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.entity.ClassroomSituationEntity
import com.educalab.clasesmart.data.local.entity.SituationOptionEntity
import com.educalab.clasesmart.data.local.entity.SituationOutcomeEntity

object SeedSituationsTiempoMateriales {

    val situations = listOf(
        ClassroomSituationEntity("sit_tiempo_1", "Poco tiempo antes del recreo", "Quedan 15 minutos y todavia falta terminar el mural.", "TIEMPO", "8-9", "mia,teo"),
        ClassroomSituationEntity("sit_tiempo_2", "La exposicion se alarga", "Cada equipo esta usando mas tiempo del previsto para presentar.", "TIEMPO", "10-12", "bruno,vera"),
        ClassroomSituationEntity("sit_tiempo_3", "El experimento va mas lento de lo pensado", "El equipo de ciencias necesita mas minutos de los que quedan.", "TIEMPO", "10-12", "leo,noa"),
        ClassroomSituationEntity("sit_tiempo_4", "Llego tarde el material", "El material para matematicas llego 10 minutos tarde a la mesa.", "TIEMPO", "8-9", "alex,sam"),
        ClassroomSituationEntity("sit_tiempo_5", "Demasiadas tareas para hoy", "El horario de hoy tiene mas actividades de las que caben.", "TIEMPO", "10-12", "cata,dani"),

        ClassroomSituationEntity("sit_mat_1", "Se perdio un material", "La lupa que se uso ayer no aparece en el estante.", "MATERIALES", "8-9", "leo"),
        ClassroomSituationEntity("sit_mat_2", "Falta un material clave", "Para el experimento hace falta el recipiente y no esta preparado.", "MATERIALES", "8-9", "vera,noa"),
        ClassroomSituationEntity("sit_mat_3", "Material fragil en manos poco cuidadosas", "Dani lleva los botes de pintura corriendo hacia la mesa.", "MATERIALES", "8-9", "dani"),
        ClassroomSituationEntity("sit_mat_4", "Todos quieren el mismo color", "Varios companeros quieren el mismo papel de color para el mural.", "MATERIALES", "8-9", "mia,cata,bruno"),
        ClassroomSituationEntity("sit_mat_5", "Material compartido sin devolver", "El diccionario compartido sigue en la mesa de ayer.", "MATERIALES", "10-12", "sam,teo"),

        ClassroomSituationEntity("sit_tiempo_6", "El reloj de arena se acabo rapido", "El equipo penso que tenia mas tiempo del que en verdad quedaba para terminar el reto.", "TIEMPO", "8-9", "alex,mia"),
        ClassroomSituationEntity("sit_tiempo_7", "Dos actividades a la misma hora", "Por error, el horario puso lectura compartida y experimento en el mismo bloque.", "TIEMPO", "10-12", "dani,leo"),
        ClassroomSituationEntity("sit_tiempo_8", "El cronometro no se detuvo a tiempo", "El equipo se distrajo y el tiempo del reto de matematicas se paso sin darse cuenta.", "TIEMPO", "10-12", "bruno,sam"),

        ClassroomSituationEntity("sit_mat_6", "Las tijeras andan sueltas", "Las tijeras de punta quedaron sobre la mesa sin su funda despues de manualidades.", "MATERIALES", "8-9", "cata,teo"),
        ClassroomSituationEntity("sit_mat_7", "No alcanzan los cuadernos de campo", "Hay un cuaderno de campo menos que integrantes en el equipo de observacion.", "MATERIALES", "10-12", "vera,alex"),
        ClassroomSituationEntity("sit_mat_8", "El pegamento se quedo destapado", "El bote de pegamento quedo abierto despues de la ultima actividad de manualidades.", "MATERIALES", "8-9", "mia,noa")
    )

    val options = listOf(
        SituationOptionEntity("opt_tiempo1_a", "sit_tiempo_1", "Repartir lo que falta entre todo el equipo", 2),
        SituationOptionEntity("opt_tiempo1_b", "sit_tiempo_1", "Seguir trabajando igual de rapido sin cambiar nada", 1),
        SituationOptionEntity("opt_tiempo1_c", "sit_tiempo_1", "Dejarlo a medias sin avisar a nadie", 0),

        SituationOptionEntity("opt_tiempo2_a", "sit_tiempo_2", "Acordar un tiempo maximo igual para cada equipo", 2),
        SituationOptionEntity("opt_tiempo2_b", "sit_tiempo_2", "Pedir a los ultimos que se den prisa", 1),
        SituationOptionEntity("opt_tiempo2_c", "sit_tiempo_2", "Dejar que unos equipos no lleguen a presentar", 0),

        SituationOptionEntity("opt_tiempo3_a", "sit_tiempo_3", "Simplificar el ultimo paso para llegar a tiempo", 2),
        SituationOptionEntity("opt_tiempo3_b", "sit_tiempo_3", "Seguir igual y terminar tarde", 1),
        SituationOptionEntity("opt_tiempo3_c", "sit_tiempo_3", "Dejar el experimento sin anotar resultados", 0),

        SituationOptionEntity("opt_tiempo4_a", "sit_tiempo_4", "Empezar con otra parte de la actividad mientras llega", 2),
        SituationOptionEntity("opt_tiempo4_b", "sit_tiempo_4", "Esperar sin hacer nada hasta que llegue", 1),
        SituationOptionEntity("opt_tiempo4_c", "sit_tiempo_4", "Cancelar la actividad por el retraso", 0),

        SituationOptionEntity("opt_tiempo5_a", "sit_tiempo_5", "Elegir juntos que actividad mover a manana", 2),
        SituationOptionEntity("opt_tiempo5_b", "sit_tiempo_5", "Intentar hacer todo mas rapido y peor", 1),
        SituationOptionEntity("opt_tiempo5_c", "sit_tiempo_5", "Dejar que el dia se desordene sin decidir nada", 0),

        SituationOptionEntity("opt_mat1_a", "sit_mat_1", "Buscar juntos en las zonas donde pudo quedar", 2),
        SituationOptionEntity("opt_mat1_b", "sit_mat_1", "Usar otra lupa distinta sin buscar la primera", 1),
        SituationOptionEntity("opt_mat1_c", "sit_mat_1", "Empezar la actividad sin lupa", 0),

        SituationOptionEntity("opt_mat2_a", "sit_mat_2", "Prepararlo antes de empezar la actividad", 2),
        SituationOptionEntity("opt_mat2_b", "sit_mat_2", "Improvisar con otro recipiente parecido", 1),
        SituationOptionEntity("opt_mat2_c", "sit_mat_2", "Empezar sin el y ver que pasa", 0),

        SituationOptionEntity("opt_mat3_a", "sit_mat_3", "Pedirle a Dani que camine y lleve los botes con cuidado", 2),
        SituationOptionEntity("opt_mat3_b", "sit_mat_3", "Dejarlo seguir igual, total no paso nada todavia", 1),
        SituationOptionEntity("opt_mat3_c", "sit_mat_3", "No decir nada aunque se pueda romper algo", 0),

        SituationOptionEntity("opt_mat4_a", "sit_mat_4", "Repartir turnos para usar ese color", 2),
        SituationOptionEntity("opt_mat4_b", "sit_mat_4", "Quedarselo quien llego primero sin mas", 1),
        SituationOptionEntity("opt_mat4_c", "sit_mat_4", "Discutir sin llegar a ningun acuerdo", 0),

        SituationOptionEntity("opt_mat5_a", "sit_mat_5", "Devolverlo al estante antes de empezar otra cosa", 2),
        SituationOptionEntity("opt_mat5_b", "sit_mat_5", "Usarlo de todas formas sin devolverlo despues", 1),
        SituationOptionEntity("opt_mat5_c", "sit_mat_5", "Dejarlo donde esta para que otro lo mueva", 0),

        SituationOptionEntity("opt_tiempo6_a", "sit_tiempo_6", "Revisar el tiempo real antes de seguir y ajustar el plan", 2),
        SituationOptionEntity("opt_tiempo6_b", "sit_tiempo_6", "Seguir igual sin mirar el tiempo", 1),
        SituationOptionEntity("opt_tiempo6_c", "sit_tiempo_6", "Detener todo de golpe sin avisar al equipo", 0),

        SituationOptionEntity("opt_tiempo7_a", "sit_tiempo_7", "Elegir juntos cual va primero y mover la otra", 2),
        SituationOptionEntity("opt_tiempo7_b", "sit_tiempo_7", "Intentar hacer las dos mitad y mitad", 1),
        SituationOptionEntity("opt_tiempo7_c", "sit_tiempo_7", "No hacer ninguna de las dos", 0),

        SituationOptionEntity("opt_tiempo8_a", "sit_tiempo_8", "Anotar hasta donde llegaron y seguir la proxima vez", 2),
        SituationOptionEntity("opt_tiempo8_b", "sit_tiempo_8", "Seguir un poco mas aunque el tiempo ya se acabo", 1),
        SituationOptionEntity("opt_tiempo8_c", "sit_tiempo_8", "Borrar todo y no contar lo que hicieron", 0),

        SituationOptionEntity("opt_mat6_a", "sit_mat_6", "Guardarlas en su funda antes de seguir", 2),
        SituationOptionEntity("opt_mat6_b", "sit_mat_6", "Dejarlas a un lado de la mesa", 1),
        SituationOptionEntity("opt_mat6_c", "sit_mat_6", "Dejarlas como estan entre los demas materiales", 0),

        SituationOptionEntity("opt_mat7_a", "sit_mat_7", "Turnarse para anotar por turnos en el mismo cuaderno", 2),
        SituationOptionEntity("opt_mat7_b", "sit_mat_7", "Que uno solo anote por todos sin turnarse", 1),
        SituationOptionEntity("opt_mat7_c", "sit_mat_7", "Empezar la actividad sin anotar nada", 0),

        SituationOptionEntity("opt_mat8_a", "sit_mat_8", "Cerrarlo bien y guardarlo en su lugar", 2),
        SituationOptionEntity("opt_mat8_b", "sit_mat_8", "Dejarlo tapado pero encima de la mesa", 1),
        SituationOptionEntity("opt_mat8_c", "sit_mat_8", "Dejarlo abierto donde esta", 0)
    )

    val outcomes = listOf(
        SituationOutcomeEntity("out_tiempo1_a", "opt_tiempo1_a", "El mural quedo terminado justo antes del recreo.", 15),
        SituationOutcomeEntity("out_tiempo1_b", "opt_tiempo1_b", "El mural quedo casi listo, con una parte a medias.", 8),
        SituationOutcomeEntity("out_tiempo1_c", "opt_tiempo1_c", "El mural quedo sin terminar y nadie sabia que faltaba.", 2),

        SituationOutcomeEntity("out_tiempo2_a", "opt_tiempo2_a", "Todos los equipos pudieron presentar en el tiempo disponible.", 15),
        SituationOutcomeEntity("out_tiempo2_b", "opt_tiempo2_b", "Se presento todo, pero los ultimos equipos fueron con prisa.", 8),
        SituationOutcomeEntity("out_tiempo2_c", "opt_tiempo2_c", "Algunos equipos se quedaron sin presentar su trabajo.", 2),

        SituationOutcomeEntity("out_tiempo3_a", "opt_tiempo3_a", "El experimento se cerro a tiempo con una conclusion clara.", 15),
        SituationOutcomeEntity("out_tiempo3_b", "opt_tiempo3_b", "El experimento termino, pero se corto el siguiente bloque.", 8),
        SituationOutcomeEntity("out_tiempo3_c", "opt_tiempo3_c", "No quedo registro de lo que se observo.", 2),

        SituationOutcomeEntity("out_tiempo4_a", "opt_tiempo4_a", "El equipo aprovecho el tiempo y no se quedo parado.", 15),
        SituationOutcomeEntity("out_tiempo4_b", "opt_tiempo4_b", "El equipo perdio minutos valiosos esperando sin avanzar.", 8),
        SituationOutcomeEntity("out_tiempo4_c", "opt_tiempo4_c", "La actividad no se llego a hacer ese dia.", 2),

        SituationOutcomeEntity("out_tiempo5_a", "opt_tiempo5_a", "El dia quedo mejor repartido y con tiempo real para cada cosa.", 15),
        SituationOutcomeEntity("out_tiempo5_b", "opt_tiempo5_b", "Todo se hizo, pero con mucha prisa y algo de estres.", 8),
        SituationOutcomeEntity("out_tiempo5_c", "opt_tiempo5_c", "El dia quedo desordenado y varias cosas quedaron a medias.", 2),

        SituationOutcomeEntity("out_mat1_a", "opt_mat1_a", "La lupa aparecio y el equipo entendio donde guardarla despues.", 15),
        SituationOutcomeEntity("out_mat1_b", "opt_mat1_b", "La actividad siguio, pero la lupa original sigue perdida.", 8),
        SituationOutcomeEntity("out_mat1_c", "opt_mat1_c", "El equipo no pudo observar los detalles pequenos.", 2),

        SituationOutcomeEntity("out_mat2_a", "opt_mat2_a", "El experimento empezo sin interrupciones.", 15),
        SituationOutcomeEntity("out_mat2_b", "opt_mat2_b", "El experimento funciono, aunque no de forma exacta.", 8),
        SituationOutcomeEntity("out_mat2_c", "opt_mat2_c", "El experimento no se pudo completar bien.", 2),

        SituationOutcomeEntity("out_mat3_a", "opt_mat3_a", "Los botes de pintura llegaron enteros a la mesa.", 15),
        SituationOutcomeEntity("out_mat3_b", "opt_mat3_b", "Esta vez no paso nada, pero el riesgo seguia ahi.", 8),
        SituationOutcomeEntity("out_mat3_c", "opt_mat3_c", "Uno de los botes se cayo y hubo que limpiar.", 2),

        SituationOutcomeEntity("out_mat4_a", "opt_mat4_a", "Todos pudieron usar el color que necesitaban en algun momento.", 15),
        SituationOutcomeEntity("out_mat4_b", "opt_mat4_b", "Solo uno pudo usarlo y los demas cambiaron de color molestos.", 8),
        SituationOutcomeEntity("out_mat4_c", "opt_mat4_c", "Se perdio tiempo discutiendo en vez de trabajar.", 2),

        SituationOutcomeEntity("out_mat5_a", "opt_mat5_a", "El diccionario quedo listo para quien lo necesite despues.", 15),
        SituationOutcomeEntity("out_mat5_b", "opt_mat5_b", "El diccionario se uso, pero nadie sabra donde buscarlo luego.", 8),
        SituationOutcomeEntity("out_mat5_c", "opt_mat5_c", "El diccionario sigue perdido para el resto de la clase.", 2),

        SituationOutcomeEntity("out_tiempo6_a", "opt_tiempo6_a", "El equipo ajusto el ritmo y termino a tiempo.", 15),
        SituationOutcomeEntity("out_tiempo6_b", "opt_tiempo6_b", "El equipo se quedo sin tiempo para el ultimo paso.", 8),
        SituationOutcomeEntity("out_tiempo6_c", "opt_tiempo6_c", "El equipo se quedo confundido sin saber que hacer.", 2),

        SituationOutcomeEntity("out_tiempo7_a", "opt_tiempo7_a", "Las dos actividades se hicieron completas, una despues de la otra.", 15),
        SituationOutcomeEntity("out_tiempo7_b", "opt_tiempo7_b", "Ninguna de las dos quedo del todo terminada.", 8),
        SituationOutcomeEntity("out_tiempo7_c", "opt_tiempo7_c", "El equipo perdio todo el bloque de tiempo sin avanzar.", 2),

        SituationOutcomeEntity("out_tiempo8_a", "opt_tiempo8_a", "El equipo supo exactamente por donde retomar despues.", 15),
        SituationOutcomeEntity("out_tiempo8_b", "opt_tiempo8_b", "El equipo avanzo algo mas, pero sin saber cuanto tiempo uso en verdad.", 8),
        SituationOutcomeEntity("out_tiempo8_c", "opt_tiempo8_c", "Se perdio todo el trabajo que habian hecho hasta ese momento.", 2),

        SituationOutcomeEntity("out_mat6_a", "opt_mat6_a", "Las tijeras quedaron guardadas de forma segura.", 15),
        SituationOutcomeEntity("out_mat6_b", "opt_mat6_b", "Las tijeras siguieron sueltas, aunque un poco mas alejadas.", 8),
        SituationOutcomeEntity("out_mat6_c", "opt_mat6_c", "Cualquiera podria lastimarse al buscar otro material.", 2),

        SituationOutcomeEntity("out_mat7_a", "opt_mat7_a", "Todos pudieron participar en las anotaciones.", 15),
        SituationOutcomeEntity("out_mat7_b", "opt_mat7_b", "La actividad avanzo, pero solo uno registro las observaciones.", 8),
        SituationOutcomeEntity("out_mat7_c", "opt_mat7_c", "El equipo no tiene registro de lo que observo.", 2),

        SituationOutcomeEntity("out_mat8_a", "opt_mat8_a", "El pegamento seguira sirviendo para la proxima clase.", 15),
        SituationOutcomeEntity("out_mat8_b", "opt_mat8_b", "El pegamento quedo protegido, aunque fuera de su sitio.", 8),
        SituationOutcomeEntity("out_mat8_c", "opt_mat8_c", "El pegamento se seco y no sirvio para la siguiente clase.", 2)
    )
}
