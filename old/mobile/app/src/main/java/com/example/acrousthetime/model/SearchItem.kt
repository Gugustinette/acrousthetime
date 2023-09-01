package com.example.acrousthetime.model

data class SearchItem(
    val id: String,
    val name: String,
    val type: TypeRecherche,
    var isFavoris: Boolean = false
)