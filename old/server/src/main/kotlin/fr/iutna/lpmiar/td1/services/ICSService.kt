package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.model.*
import main.kotlin.fr.iutna.lpmiar.td1.util.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

@Service
class ICSService(
    @Autowired val creneauxService: CreneauxService,
    @Autowired val etudiantService: EtudiantService,
    @Autowired val salleService: SalleService,
    @Autowired val personnelService: PersonnelService,
    @Autowired val groupeService: GroupeService
) {

    /**
     * Récupère les créneaux d'un groupe en fonction de sa liste d'évènements
     */
    fun retrieveCreneauGroupe(events: List<Event>, id: String, creneauxList: MutableList<Creneaux>): MutableList<Creneaux> {
        // On récupère le groupe en db
        val groupe = groupeService.find(id)

        // Si le groupe n'existe pas, on le crée
        if (groupe == null) {
            return creneauxList
        }

        // Pour chaque event, on sauvegarde le creneaux
        for (event in events) {
            val creneaux = isListContainsCreneauUid(creneauxList, event.uid)

            if (creneaux == null) {
                val creneau = this.createCreneau(event)
                creneau.addGroupe(groupe)
                creneauxList.add(creneau)
            } else {
                creneaux.addGroupe(groupe)
            }
        }

        return creneauxList
    }

    /**
     * Récupère les créneaux d'un personnel en fonction de sa liste d'évènements
     */
    fun retrieveCreneauPersonnel(events: List<Event>, id: String, creneauxList: MutableList<Creneaux>): MutableList<Creneaux> {
        val personnel = personnelService.find(id)

        if (personnel == null) {
            return creneauxList
        }

        for (event in events) {
            val creneaux = isListContainsCreneauUid(creneauxList, event.uid)

            if (creneaux == null) {
                val creneau = this.createCreneau(event)
                creneau.addPersonnel(personnel)
                creneauxList.add(creneau)
            } else {
                creneaux.addPersonnel(personnel)
            }
        }

        return creneauxList
    }

    /**
     * Récupère les créneaux d'une salle en fonction de sa liste d'évènements
     */
    fun retrieveCreneauSalle(events: List<Event>, id: String, creneauxList: MutableList<Creneaux>): MutableList<Creneaux> {
        val salle = salleService.find(id)

        if (salle == null) {
            return creneauxList
        }

        for (event in events) {
            val creneaux = isListContainsCreneauUid(creneauxList, event.uid)

            if (creneaux == null) {
                val creneau = this.createCreneau(event)
                creneau.addSalle(salle)
                creneauxList.add(creneau)
            } else {
                creneaux.addSalle(salle)
            }
        }

        return creneauxList
    }

    /**
     * Récupère les créneaux d'un étudiant en fonction de sa liste d'évènements
     */
    fun retrieveCreneauEtudiant(events: List<Event>, id: String, creneauxList: MutableList<Creneaux>): MutableList<Creneaux> {
        val etudiant = etudiantService.find(id)

        if (etudiant == null) {
            return creneauxList
        }

        for (event in events) {
            val creneaux = isListContainsCreneauUid(creneauxList, event.uid)

            if (creneaux == null) {
                val creneau = this.createCreneau(event)
                creneau.addEtudiant(etudiant)
                creneauxList.add(creneau)
            } else {
                creneaux.addEtudiant(etudiant)
            }
        }

        return creneauxList
    }


    /**
     * Sauvegarde tous les groupes depuis la map de groupes (id -> nom)
     */
    fun saveAllGroupes(groupes: Map<String, String>) {
        for ((id, nom) in groupes) {
            val groupe = groupeService.find(id)

            if (groupe != null) {
                groupeService.updateLastSeen(groupe)
            } else {
                // get date of yesterday
                groupeService.save(Groupe(id, nom))
            }
        }
    }

    /**
     * Sauvegarde tous les personnels depuis la map de personnels (id -> nom)
     */
    fun saveAllPersonnel(personnels: Map<String, String>) {
        for ((id, nom) in personnels) {
            val personnel = personnelService.find(id)

            if (personnel != null) {
                personnelService.updateLastSeen(personnel)
            } else {
                personnelService.save(Personnel(id, nom))
            }
        }
    }

    /**
     * Sauvegarde toutes les salles depuis la map de salles (id -> nom)
     */
    fun saveAllSalles(salles: Map<String, String>) {
        for ((id, nom) in salles) {
            val salle = salleService.find(id)

            if (salle != null) {
                salleService.updateLastSeen(salle)
            } else {
                salleService.save(Salle(id, nom, 0, TypeSalle.AVEC_PC))
            }
        }
    }

    /**
     * Sauvegarde tous les étudiants depuis la map d'étudiants (id -> nom)
     */
    fun saveAllEtudiant(etudiants: Map<String, String>) {
        for ((id, nom) in etudiants) {
            val etudiant = etudiantService.find(id)

            if (etudiant != null) {
                etudiantService.updateLastSeen(etudiant)
            } else {
                etudiantService.save(Etudiant(id, nom))
            }
        }
    }

    /**
     * Supprime tous les créneaux de la db et sauvegarde la nouvelle liste de creneaux
     */
    fun saveAllCreneaux(creneauxList: MutableList<Creneaux>) {
        creneauxService.deleteAll()
        creneauxService.saveAll(creneauxList)
    }

    /**
     * Supprime tous les éléments (groupes, personnels, salles, étudiants) qui n'ont pas été mis à jour depuis plus de 24h
     */
    fun deleteAllNotUpdate() {
        groupeService.deleteAllNotUpdate()
        personnelService.deleteAllNotUpdate()
        salleService.deleteAllNotUpdate()
        etudiantService.deleteAllNotUpdate()
    }

    /**
     * Vérifie si la liste de créneaux contient un créneau avec l'uid passé en paramètre
     * Si oui, retourne le créneau, sinon retourne null
     */
    private fun isListContainsCreneauUid(listCreneau: MutableList<Creneaux>, uid: String): Creneaux? {
        for (creneau in listCreneau) {
            if (creneau.uid == uid) {
                return creneau
            }
        }
        return null
    }


    /**
     * Crée le début d'un objet Creneaux à partir d'un objet Event
     */
    private fun createCreneau(event: Event): Creneaux {
        val uid = event.uid
        val dateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
        val dt_start = dateFormat.parse(event.startTime)
        val dt_end = dateFormat.parse(event.endTime)
        val summary = event.summary
        val matiere = event.matiere ?: ""
        val last_update = Date.from(Instant.now())

        return Creneaux(
            uid,
            dt_start,
            dt_end,
            summary,
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            matiere,
            last_update
        )
    }

}