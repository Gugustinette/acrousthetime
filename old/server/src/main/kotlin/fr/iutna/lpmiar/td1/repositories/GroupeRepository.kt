package fr.iutna.lpmiar.td1.repositories

import fr.iutna.lpmiar.td1.model.Creneaux
import fr.iutna.lpmiar.td1.model.Groupe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*
import javax.transaction.Transactional

interface GroupeRepository: JpaRepository<Groupe, String> {
    @Transactional
    fun deleteByLastUpdateLessThan(lastUpdate: Date)

    fun findAllByNameContains(search: String): List<Groupe>
}