package com.example.acrousthetime.model

class Favoris {
    fun getListItem(): MutableList<FavorisItem> {
        val liste : MutableList<FavorisItem> = mutableListOf()

        etudiant.forEach {
            liste.add(FavorisItem(it.id, it.name, TypeRecherche.ETUDIANT))
        }

        groupe.forEach {
            liste.add(FavorisItem(it.id, it.name, TypeRecherche.GROUPE))
        }

        salle.forEach {
            liste.add(FavorisItem(it.id, it.name, TypeRecherche.SALLE))
        }

        personnel.forEach {
            liste.add(FavorisItem(it.id, it.name, TypeRecherche.PERSONNEL))
        }

        return liste
    }

    val etudiant: MutableList<Etudiant> = mutableListOf()
    val groupe: MutableList<Groupe> = mutableListOf()
    val salle: MutableList<Salle> = mutableListOf()
    val personnel: MutableList<Personnel> = mutableListOf()
}

class FavorisItem(val id: String, val name: String, val type: TypeRecherche) {
    override fun toString(): String {
        return name
    }
}