package fr.iutna.lpmiar.td1.repositories

import fr.iutna.lpmiar.td1.model.Etudiant
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

interface EtudiantRepository: JpaRepository<Etudiant, String> {

    @Transactional
    fun deleteByLastUpdateLessThan(lastUpdate: Date)


    fun findAllByNameContains(search: String): List<Etudiant>
}