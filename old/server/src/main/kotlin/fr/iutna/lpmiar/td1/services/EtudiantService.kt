package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.model.Etudiant
import fr.iutna.lpmiar.td1.repositories.EtudiantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class EtudiantService (@Autowired val db: EtudiantRepository) {
    fun save(etudiant: Etudiant) = db.save(etudiant)
    fun find(etudiantId: String): Etudiant? = db.findById(etudiantId).orElse(null)

    fun updateLastSeen(etudiant: Etudiant) {
        etudiant.lastUpdate = Date()
        db.save(etudiant)
    }

    fun deleteAllNotUpdate() {
        val yesterday = Date.from(LocalDateTime.now().minusDays(1).toInstant(java.time.ZoneOffset.UTC))
        db.deleteByLastUpdateLessThan(yesterday)
    }

    fun findAll(): List<Etudiant> = db.findAll()

    fun findAllContains(search: String): List<Etudiant> = db.findAllByNameContains(search)

}