package com.example.acrousthetime.model

import java.time.Instant
import java.util.*



data class Creneaux (
    val uid: String,

    val dt_start: Date,
    val dt_end: Date,
    val summary: String,

    //Etudiant: many to many
    var etudiant: List<Etudiant> = mutableListOf(),

    //Salle: many to many
    var salle: List<Salle> = mutableListOf(),

    //Groupe: many to many
    var groupe: List<Groupe> = mutableListOf(),

    //Personnel: many to many
    var personnel: List<Personnel> = mutableListOf(),
    val matiere: String,
    var last_update: Date
) {

    fun addEtudiant(etudiant: Etudiant) {
        this.etudiant += etudiant
        last_update = Date.from(Instant.now())
    }

    fun addSalle(salle: Salle) {
        this.salle += salle
        last_update = Date.from(Instant.now())
    }

    fun addGroupe(groupe: Groupe) {
        this.groupe += groupe
        last_update = Date.from(Instant.now())
    }

    fun addPersonnel(personnel: Personnel) {
        this.personnel += personnel
        last_update = Date.from(Instant.now())
    }
    companion object {
        fun exampleList(): List<Creneaux> {
            return List(3) {
                Creneaux(
                    UUID.randomUUID().toString(),
                    Date.from(Instant.now()),
                    Date.from(Instant.now()),
                    "TD - cours de go",
                    Etudiant.exampleList(),
                    Salle.exempleList(),
                    mutableListOf(),
                    Personnel.exampleList(),
                    "Programmation orientée objet",
                    Date.from(Instant.now())
                )
            }
        }

    }
}