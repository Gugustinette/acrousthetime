package fr.iutna.lpmiar.td1.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "IMAGE")
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val fileName: String,

    @OneToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
)