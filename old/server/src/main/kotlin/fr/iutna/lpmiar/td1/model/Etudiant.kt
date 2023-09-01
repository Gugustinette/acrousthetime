package fr.iutna.lpmiar.td1.model;

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "ETUDIANT")
data class Etudiant(
        @Id val id: String,
        val name: String,
        @JsonIgnore
        var lastUpdate: Date = Date()
)