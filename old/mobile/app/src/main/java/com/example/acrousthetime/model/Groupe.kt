package com.example.acrousthetime.model

import java.util.*

data class Groupe (
    val id: String,
    val name: String,

    var lastUpdate: Date = Date(),
){
    companion object {
        fun exampleList(): List<Groupe> {
            return List(3){
                Groupe(
                    UUID.randomUUID().toString(),
                    "name",
                )
            }

        }

        fun readFromJson(map: Map<String, *>): Groupe {
            return Groupe(
                map["id"] as String,
                map["name"] as String
            )
        }
    }
}




