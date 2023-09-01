package fr.iutna.lpmiar.td1.repositories

import fr.iutna.lpmiar.td1.model.Reservation
import fr.iutna.lpmiar.td1.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository: JpaRepository<Reservation, String> {

    fun findByUser(user: User): List<Reservation>

}