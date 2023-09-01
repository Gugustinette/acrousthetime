package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.model.Salle
import fr.iutna.lpmiar.td1.repositories.SalleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Date

@Service
class SalleService(@Autowired val db: SalleRepository) {
    fun save(salle: Salle) = db.save(salle)
    fun find(salle: String?) = db.findById(salle!!).orElse(null)

    fun updateLastSeen(salle: Salle) {
        salle.lastUpdate = Date()
        db.save(salle)
    }

    fun deleteAllNotUpdate() {
        val yesterday = Date.from(LocalDateTime.now().minusDays(1).toInstant(java.time.ZoneOffset.UTC))
        db.deleteByLastUpdateLessThan(yesterday)
    }

    fun findAll(): List<Salle> = db.findAll()

    fun deleteAll() = db.deleteAll()
    fun findAllContains(search: String): List<Salle> = db.findAllByNameContains(search)

    fun findSalleByDate(debut: Date, fin: Date): List<Salle> = db.findSalleNotBetweenCreneauxDates(debut, fin)
}