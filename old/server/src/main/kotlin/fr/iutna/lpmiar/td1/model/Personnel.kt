package fr.iutna.lpmiar.td1.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.ManyToMany
import java.util.*
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
@Entity
@Table(name = "PERSONNEL")
data class Personnel (
        @Id val id: String,
        val name: String,
        @JsonIgnore
        var lastUpdate: Date = Date()
)