package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.model.Reservation
import fr.iutna.lpmiar.td1.model.Role
import fr.iutna.lpmiar.td1.model.StatusReservation
import fr.iutna.lpmiar.td1.model.User
import fr.iutna.lpmiar.td1.services.ReservationService
import fr.iutna.lpmiar.td1.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/reservation")
@CrossOrigin(origins = ["*"])
class ReservationController (
    @Autowired val reservationService: ReservationService,
    @Autowired val userService: UserService
){

    @GetMapping
    @Operation(summary = "Récupération de toutes les réservations")
    @ApiResponse(
        responseCode = "200",
        description = "Liste des réservations",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Reservation::class))
            )
        ]
    )
    fun index() = reservationService.findAll()
    @GetMapping("/{id}")
    @Operation(summary = "Récupération des infos d'une réservation")
    @ApiResponse(
        responseCode = "200",
        description = "Infos de la réservation",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Reservation::class))
            )
        ]
    )
    fun show (@PathVariable id: String) : ResponseEntity<Any> {
        val reservation = reservationService.findReservation(id)
        if (reservation == null) {
            return ResponseEntity.notFound().build()
        } else {
            return ResponseEntity.ok(reservation)
        }
    }

    @GetMapping("/user")
    @Operation(summary = "Récupération des réservations d'un utilisateur")
    @ApiResponse(
        responseCode = "200",
        description = "Liste des réservations de l'utilisateur",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Reservation::class))
            )
        ]
    )
    fun getReservationsByUser(): ResponseEntity<Any> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val reservations = reservationService.findByUser(user)
        return ResponseEntity.ok(reservations)
    }

    @PostMapping
    @Operation(summary = "Ajout d'une réservation")
    @ApiResponse(
        responseCode = "200",
        description = "Ajout d'une réservation",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Reservation::class))
            )
        ]
    )
    fun post(@RequestBody reservation: Reservation): ResponseEntity<Any>{
        if (reservation.id != null && reservationService.findReservation(reservation.id) == null) {
            return ResponseEntity.notFound().build()
        }

        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User? ?: return ResponseEntity.notFound().build()


        val newReservation = Reservation(
            reservation.id ?: UUID.randomUUID().toString(),
            reservation.dt_start,
            reservation.dt_end,
            reservation.description,
            StatusReservation.PENDING,
            reservation.salle,
            userAuthenticate
        )
        return ResponseEntity.ok(reservationService.post(newReservation))
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Suppression d'une réservation")
    @ApiResponse(
        responseCode = "200",
        description = "Suppression d'une réservation",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Reservation::class))
            )
        ]
    )
    fun delete (@PathVariable id:String): ResponseEntity<Any>{
        val reservation = reservationService.findReservation(id)

        return if (reservation == null) {
            ResponseEntity.notFound().build()
        } else {
            reservationService.delete(id)
            ResponseEntity.ok().build()
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modification d'une réservation")
    @ApiResponse(
        responseCode = "200",
        description = "Modification d'une réservation",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Reservation::class))
            )
        ]
    )
    fun put (@PathVariable id:String, @RequestBody reservation: Reservation): ResponseEntity<Any>{
        val reservationToUpdate = reservationService.findReservation(id)

        return if (reservationToUpdate == null) {
            ResponseEntity.notFound().build()
        } else {
            val newReservation = Reservation(
                reservation.id ?: UUID.randomUUID().toString(),
                reservation.dt_start,
                reservation.dt_end,
                reservation.description,
                reservation.status,
                reservation.salle,
                reservationToUpdate.user
            )
            ResponseEntity.ok(reservationService.edit(newReservation))
        }
    }

    @PostMapping("/accept/{id}")
    @Operation(summary = "Accepter une réservation")
    @ApiResponse(
        responseCode = "200",
        description = "Accepter une réservation",
        content = [
            Content(
                mediaType = "application/json",
            )
        ]
    )
    fun accept (@PathVariable id:String): ResponseEntity<Reservation>{
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User

        if (userAuthenticate.roles != Role.ADMIN) {
            return ResponseEntity.badRequest().build()
        }

        val reservation = reservationService.findReservation(id)

        return if (reservation == null) {
            ResponseEntity.notFound().build()
        } else {
            val newReservation = Reservation(
                reservation.id,
                reservation.dt_start,
                reservation.dt_end,
                reservation.description,
                StatusReservation.ACCEPTED,
                reservation.salle,
                reservation.user
            )
            ResponseEntity.ok(reservationService.edit(newReservation))
        }
    }

    @PutMapping("/decline/{id}")
    @Operation(summary = "Refuse une réservation")
    @ApiResponse(
        responseCode = "200",
        description = "Refuser une réservation",
        content = [
            Content(
                mediaType = "application/json",
            )
        ]
    )
    fun decline (@PathVariable id:String): ResponseEntity<Reservation> {
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User
        val user = userService.findByEmail(userAuthenticate.email)

        if (user == null || user.roles != Role.ADMIN) {
            return ResponseEntity.badRequest().build()
        }

        val reservation = reservationService.findReservation(id)

        return if (reservation == null) {
            ResponseEntity.notFound().build()
        } else {
            val newReservation = Reservation(
                reservation.id,
                reservation.dt_start,
                reservation.dt_end,
                reservation.description,
                StatusReservation.REFUSED,
                reservation.salle,
                reservation.user
            )
            ResponseEntity.ok(reservationService.edit(newReservation))
        }
    }

}