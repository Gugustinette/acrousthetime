package com.example.acrousthetime.model

import java.util.*

data class Reservation(
    val id: String?,
    val dt_start: Date,
    val dt_end: Date,
    val description: String,

    var salle: Salle? = null,

    var personnel: Personnel? = null,
)