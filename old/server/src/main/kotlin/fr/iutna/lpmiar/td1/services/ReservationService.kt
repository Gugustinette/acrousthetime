package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.model.Reservation
import fr.iutna.lpmiar.td1.model.User
import fr.iutna.lpmiar.td1.repositories.ReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReservationService(@Autowired val db: ReservationRepository) {

    fun findAll(): List<Reservation> = db.findAll().toList()

    fun findReservation(id: String): Reservation? = db.findById(id).orElse(null)

    fun post(reservation: Reservation) : Reservation = db.save(reservation)

    fun delete(id: String) = db.deleteById(id)

    fun edit(newReservation: Reservation) : Reservation = db.save(newReservation)
    fun findByUser(user: User): List<Reservation> = db.findByUser(user)

}