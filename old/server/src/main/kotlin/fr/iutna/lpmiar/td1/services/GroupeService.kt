package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.repositories.GroupeRepository
import org.springframework.stereotype.Service
import fr.iutna.lpmiar.td1.model.Groupe
import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@Service
class GroupeService(@Autowired val db: GroupeRepository) {

    fun find(id: String): Groupe? = db.findById(id).orElse(null)

    fun save(groupe: Groupe) = db.save(groupe)
    fun updateLastSeen(groupe: Groupe) {
        groupe.lastUpdate = Date()
        db.save(groupe)
    }

    fun deleteAllNotUpdate() {
        val yesterday = Date.from(LocalDateTime.now().minusDays(1).toInstant(java.time.ZoneOffset.UTC))
        db.deleteByLastUpdateLessThan(yesterday)
    }

    fun findAll(): List<Groupe> = db.findAll()
    fun findAllContains(search: String): List<Groupe> = db.findAllByNameContains(search)
}