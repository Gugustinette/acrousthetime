package fr.iutna.lpmiar.td1.dtos

import fr.iutna.lpmiar.td1.model.Etudiant
import fr.iutna.lpmiar.td1.model.Groupe
import fr.iutna.lpmiar.td1.model.Personnel
import fr.iutna.lpmiar.td1.model.Salle

class SearchResponse {
    val etudiant: MutableList<Etudiant> = mutableListOf()
    val groupe: MutableList<Groupe> = mutableListOf()
    val personnel: MutableList<Personnel> = mutableListOf()
    val salle: MutableList<Salle> = mutableListOf()
}