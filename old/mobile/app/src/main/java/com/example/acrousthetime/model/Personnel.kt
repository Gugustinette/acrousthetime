package com.example.acrousthetime.model

import java.util.*

data class Personnel (
    val id: String,
    val name: String,
    var lastUpdate: Date = Date()
){
    companion object {
        fun exampleList(): List<Personnel> {
            return List(3){
                Personnel(
                    UUID.randomUUID().toString(),
                    "name",
                )
            }

        }

        fun readFromJson(map: Map<String, *>): Personnel {
            return Personnel(
                map["id"] as String,
                map["name"] as String
            )
        }
    }
}