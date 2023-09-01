package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.repositories.CreneauxRepository
import fr.iutna.lpmiar.td1.model.Creneaux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreneauxService(@Autowired val db: CreneauxRepository) {

    fun save(creneaux: Creneaux) = db.save(creneaux)
    fun saveAll(creneaux: List<Creneaux>) = db.saveAll(creneaux)
    fun deleteAll() = db.deleteAll()
    fun find(id: String): Creneaux? = db.findById(id).orElse(null)
    fun findAll(): List<Creneaux> = db.findAll()

    fun findCreneauxByGroupeAndSemaine(id: String, semaine: Date): List<Creneaux> {
        return db.findCreneauxByGroupeAndSemaine(id, semaine)
    }

    fun findCreneauxByGroupeAndJour(id: String, jour: Date): List<Creneaux> {
        return db.findCreneauxByGroupeAndJour(id, jour)
    }

    fun findCreneauxByEtudiantAndSemaine(id: String, semaine: Date): List<Creneaux> {
        return db.findCreneauxByEtudiantAndSemaine(id, semaine)
    }

    fun findCreneauxByEtudiantAndJour(id: String, jour: Date): List<Creneaux> {
        return db.findCreneauxByEtudiantAndJour(id, jour)
    }

    fun findCreneauxByPersonnelAndSemaine(id: String, semaine: Date): List<Creneaux> {
        return db.findCreneauxByPersonnelAndSemaine(id, semaine)
    }

    fun findCreneauxByPersonnelAndJour(id: String, jour: Date): List<Creneaux> {
        return db.findCreneauxByPersonnelAndJour(id, jour)
    }

    fun findCreneauxBySalleAndSemaine(id: String, semaine: Date): List<Creneaux> {
        return db.findCreneauxBySalleAndSemaine(id, semaine)
    }

    fun findCreneauxBySalleAndJour(id: String, jour: Date): List<Creneaux> {
        return db.findCreneauxBySalleAndJour(id, jour)
    }
}