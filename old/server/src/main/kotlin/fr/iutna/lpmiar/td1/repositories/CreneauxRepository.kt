package fr.iutna.lpmiar.td1.repositories

import fr.iutna.lpmiar.td1.model.Creneaux
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface CreneauxRepository: JpaRepository<Creneaux, String> {

    @Query("""
    SELECT c
    FROM Creneaux c
    JOIN c.groupe g
    WHERE WEEK(c.dt_start) = WEEK(:semaine)
      AND YEAR(c.dt_start) = YEAR(:semaine)
      AND g.id = :id
    """)
    fun findCreneauxByGroupeAndSemaine(id: String, semaine: Date): List<Creneaux>

    @Query("""
    SELECT c
    FROM Creneaux c
    JOIN c.groupe g
    WHERE WEEK(c.dt_start) = WEEK(:jour)
      AND YEAR(c.dt_start) = YEAR(:jour)
      AND DAYOFWEEK(c.dt_start) = DAYOFWEEK(:jour)
      AND g.id = :id
    """)
    fun findCreneauxByGroupeAndJour(id: String, jour: Date): List<Creneaux>


    @Query("""
    SELECT c
    FROM Creneaux c
    JOIN c.etudiant e
    WHERE WEEK(c.dt_start) = WEEK(:semaine)
      AND YEAR(c.dt_start) = YEAR(:semaine)
      AND e.id = :id
    """)
    fun findCreneauxByEtudiantAndSemaine(id: String, semaine: Date): List<Creneaux>

    @Query("""
    SELECT c
    FROM Creneaux c
    JOIN c.etudiant e
    WHERE WEEK(c.dt_start) = WEEK(:jour)
      AND YEAR(c.dt_start) = YEAR(:jour)
      AND DAYOFWEEK(c.dt_start) = DAYOFWEEK(:jour)
      AND e.id = :id
    """)
    fun findCreneauxByEtudiantAndJour(id: String, jour: Date): List<Creneaux>


    @Query("""
    SELECT c
    FROM Creneaux c
    JOIN c.personnel p
    WHERE WEEK(c.dt_start) = WEEK(:semaine)
      AND YEAR(c.dt_start) = YEAR(:semaine)
      AND p.id = :id
    """)
    fun findCreneauxByPersonnelAndSemaine(id: String, semaine: Date): List<Creneaux>

    @Query("""
    SELECT c
    FROM Creneaux c
    JOIN c.personnel p
    WHERE WEEK(c.dt_start) = WEEK(:jour)
      AND YEAR(c.dt_start) = YEAR(:jour)
      AND DAYOFWEEK(c.dt_start) = DAYOFWEEK(:jour)
      AND p.id = :id
    """)
    fun findCreneauxByPersonnelAndJour(id: String, jour: Date): List<Creneaux>

    @Query("""
    SELECT c
    FROM Creneaux c
    JOIN c.salle s
    WHERE WEEK(c.dt_start) = WEEK(:semaine)
      AND YEAR(c.dt_start) = YEAR(:semaine)
      AND s.id = :id
    """)
    fun findCreneauxBySalleAndSemaine(id: String, semaine: Date): List<Creneaux>

    @Query("""
    SELECT c
    FROM Creneaux c
    JOIN c.salle s
    WHERE WEEK(c.dt_start) = WEEK(:jour)
      AND YEAR(c.dt_start) = YEAR(:jour)
      AND DAYOFWEEK(c.dt_start) = DAYOFWEEK(:jour)
      AND s.id = :id
    """)
    fun findCreneauxBySalleAndJour(id: String, jour: Date): List<Creneaux>
}