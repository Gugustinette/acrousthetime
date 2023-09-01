package fr.iutna.lpmiar.td1.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "SALLE")
data class Salle(
        @Id val id: String,
        val name: String,
        val capacity: Int,
        @Enumerated(EnumType.STRING) val type: TypeSalle,

        @OneToMany(mappedBy = "salle")
        @JsonIgnore
        val reservations: List<Reservation> = mutableListOf(),

        @JsonIgnore
        var lastUpdate: Date = Date()
)