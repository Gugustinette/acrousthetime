package fr.iutna.lpmiar.td1.repositories

import fr.iutna.lpmiar.td1.model.Personnel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

interface PersonnelRepository: JpaRepository<Personnel, String> {
    fun findByName(personnel: String): Personnel?

    @Transactional
    fun deleteByLastUpdateLessThan(lastUpdate: Date)

    fun findAllByNameContains(search: String): List<Personnel>
}