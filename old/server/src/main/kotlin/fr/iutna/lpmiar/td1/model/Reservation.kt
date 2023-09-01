package fr.iutna.lpmiar.td1.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "RESERVATION")
data class Reservation(
    @Id val id: String?,
    val dt_start: Date,
    val dt_end: Date,
    val description: String,

    @Enumerated(EnumType.STRING)
    val status: StatusReservation = StatusReservation.PENDING,

    @ManyToOne
    @JoinColumn(name = "salle_id")
    var salle: Salle? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("reservations", "password")
    var user: User? = null
)