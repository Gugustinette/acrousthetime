package com.example.acrousthetime.model

import java.util.*

data class Salle(
    val id: String,
    val name: String,
    val capacity: Int,
    val type: TypeSalle,

    val reservations: List<Reservation> = mutableListOf(),

    var lastUpdate: Date = Date()
){
    companion object {
        fun exempleList(): List<Salle> {
            return List(3){
                Salle(
                    UUID.randomUUID().toString(),
                    "name",
                    10,
                    TypeSalle.HAUTE_PERFORMANCE,
                    mutableListOf(),
                )
            }
        }

        fun readFromJson(map: Map<String, *>): Salle {
            return Salle(
                map["id"] as String,
                map["name"] as String,
                // Capacity (Double to Int)
                (map["capacity"] as Double).toInt(),
                // Switch type to TypeSalle
                when (map["type"] as String) {
                    "HAUTE_PERFORMANCE" -> TypeSalle.HAUTE_PERFORMANCE
                    "AVEC_PC" -> TypeSalle.AVEC_PC
                    "SANS_PC" -> TypeSalle.SANS_PC
                    else -> TypeSalle.SANS_PC
                },
                mutableListOf(),
                Date()
            )
        }
    }
}