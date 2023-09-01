package fr.iutna.lpmiar.td1.model

import javax.persistence.*
import java.util.*
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
@Table(name = "GROUPE")
data class Groupe (
        @Id val id: String,
        val name: String,

        @JsonIgnore
        var lastUpdate: Date = Date()
)