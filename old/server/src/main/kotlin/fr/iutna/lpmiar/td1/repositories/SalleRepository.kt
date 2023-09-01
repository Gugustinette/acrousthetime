package fr.iutna.lpmiar.td1.repositories

import fr.iutna.lpmiar.td1.model.Salle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Date
import javax.transaction.Transactional

interface SalleRepository: JpaRepository<Salle, String> {
    fun findByName(salle: String): Salle?

    @Transactional
    fun deleteByLastUpdateLessThan(lastUpdate: Date)

    fun findAllByNameContains(search: String): List<Salle>

    @Query("""
    SELECT s
    FROM Salle s
    WHERE NOT EXISTS (
        SELECT c
        FROM Creneaux c
        JOIN c.salle salle
        WHERE salle = s
        AND c.dt_end >= :startDate
        AND c.dt_start <= :endDate
    )
""")
    fun findSalleNotBetweenCreneauxDates(startDate: Date, endDate: Date): List<Salle>
}