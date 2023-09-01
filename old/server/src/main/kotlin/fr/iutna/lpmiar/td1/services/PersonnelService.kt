package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.repositories.PersonnelRepository
import fr.iutna.lpmiar.td1.model.Personnel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Date

@Service
class PersonnelService(@Autowired val db: PersonnelRepository) {
    fun save(personnel: Personnel) = db.save(personnel)
    fun find(personnelId: String): Personnel? = db.findById(personnelId).orElse(null)
    fun updateLastSeen(personnel: Personnel) {
        personnel.lastUpdate = Date()
        db.save(personnel)
    }

    fun deleteAllNotUpdate() {
        val yesterday = Date.from(LocalDateTime.now().minusDays(1).toInstant(java.time.ZoneOffset.UTC))
        db.deleteByLastUpdateLessThan(yesterday)
    }

    fun findAll(): List<Personnel> = db.findAll()
    fun findAllContains(search: String): List<Personnel> = db.findAllByNameContains(search)

}