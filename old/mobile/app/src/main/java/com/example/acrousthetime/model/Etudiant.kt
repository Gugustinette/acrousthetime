package com.example.acrousthetime.model

import java.util.*

data class Etudiant(
    val id: String,
    val name: String,
    var lastUpdate: Date = Date()
){
    companion object {
        fun exampleList(): List<Etudiant> {
            return List(3){
                Etudiant(
                    UUID.randomUUID().toString(),
                    "name",
                )
            }

        }

        fun readFromJson(map: Map<String, *>): Etudiant {
            return Etudiant(
                map["id"] as String,
                map["name"] as String
            )
        }
    }
}