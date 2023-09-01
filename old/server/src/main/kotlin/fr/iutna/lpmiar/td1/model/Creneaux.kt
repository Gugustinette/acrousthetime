package fr.iutna.lpmiar.td1.model;

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import fr.iutna.lpmiar.td1.util.DateUtil
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CRENEAUX")
data class Creneaux (
        @Id val uid: String,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.FORMAT)
        val dt_start: Date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.FORMAT)
        val dt_end: Date,

        @Column(columnDefinition = "TEXT")
        val summary: String,

        //Etudiant: many to many
        @ManyToMany
        @JoinTable(
                name = "creneaux_etudiant",
                joinColumns = [JoinColumn(name = "creneaux_id")],
                inverseJoinColumns = [JoinColumn(name = "etudiant_id")]
        )
        @LazyCollection(LazyCollectionOption.FALSE)
        var etudiant: List<Etudiant> = mutableListOf(),

        //Salle: many to many
        @ManyToMany
        @JoinTable(
                name = "creneaux_salle",
                joinColumns = [JoinColumn(name = "creneaux_id")],
                inverseJoinColumns = [JoinColumn(name = "salle_id")]
        )
        @LazyCollection(LazyCollectionOption.FALSE)
        @JsonIgnoreProperties("salle")
        var salle: List<Salle> = mutableListOf(),

        //Groupe: many to many
        @ManyToMany
        @JoinTable(
                name = "creneaux_groupe",
                joinColumns = [JoinColumn(name = "creneaux_id")],
                inverseJoinColumns = [JoinColumn(name = "groupe_id")]
        )
        @LazyCollection(LazyCollectionOption.FALSE)
        @JsonIgnoreProperties("groupe")
        var groupe: List<Groupe> = mutableListOf(),

        //Personnel: many to many
        @ManyToMany
        @JoinTable(
                name = "creneaux_personnel",
                joinColumns = [JoinColumn(name = "creneaux_id")],
                inverseJoinColumns = [JoinColumn(name = "personnel_id")]
        )
        @LazyCollection(LazyCollectionOption.FALSE)
        @JsonIgnoreProperties("personnel")
        var personnel: List<Personnel> = mutableListOf(),

        @Column(columnDefinition = "TEXT")
        val matiere: String,
        var last_update: Date
) {

        fun addEtudiant(etudiant: Etudiant) {
                this.etudiant += etudiant
                last_update = Date.from(Instant.now())
        }

        fun addSalle(salle: Salle) {
                this.salle += salle
                last_update = Date.from(Instant.now())
        }

        fun addGroupe(groupe: Groupe) {
                this.groupe += groupe
                last_update = Date.from(Instant.now())
        }

        fun addPersonnel(personnel: Personnel) {
                this.personnel += personnel
                last_update = Date.from(Instant.now())
        }
}