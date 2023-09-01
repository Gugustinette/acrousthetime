package fr.iutna.lpmiar.td1.batch

// Spring Imports
import fr.iutna.lpmiar.td1.model.Creneaux
import fr.iutna.lpmiar.td1.services.ICSService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import fr.iutna.lpmiar.td1.util.ICSUtil

// ImportICS Class
@Component
class ImportICS(val icsService: ICSService? = null) {

    /**
     * Import ICS
     */
    // Tous les jours à 2h du matin
    @Scheduled(cron = "0 0 2 * * *")
    fun importIcs() {
        // Variable pour le calcul du temps total
        val totalTime = System.currentTimeMillis()

        // Variable pour le calcul du temps
        var startTime = System.currentTimeMillis()

        // Récupération des listes pour les ics
        val groupes = ICSUtil.ICSLister("groupe")
        val profs = ICSUtil.ICSLister("personnel")
        val salles = ICSUtil.ICSLister("salle")
        val eleves = ICSUtil.ICSLister("eleve")

        // Sauvegarde des listes
        println("Sauvegarde des groupes (" + groupes.size + ")")
        icsService?.saveAllGroupes(groupes)
        println("Sauvegarde des personnel (" + profs.size + ")")
        icsService?.saveAllPersonnel(profs)
        println("Sauvegarde des salles (" + salles.size + ")")
        icsService?.saveAllSalles(salles)
        println("Sauvegarde des eleves (" + eleves.size + ")")
        icsService?.saveAllEtudiant(eleves)

        // Suppression des listes non mises à jour
        println("suppression des listes non mises à jour")
        icsService?.deleteAllNotUpdate()

        var endTime = System.currentTimeMillis()
        var timeElapsed = endTime - startTime
        println("Temps d'import et de sauvegarde des listes : $timeElapsed ms")

        // Création de la liste pour les créneaux
        val creneauxList : MutableList<Creneaux> = mutableListOf()

        // ===== GROUPES ====== //
        println("Import des ics des groupes")
        startTime = System.currentTimeMillis()
        for ((id, _) in groupes) {
            ICSUtil.ICSScrapper(id, "groupe")
            val events = ICSUtil.ICSParser(id)
            icsService?.retrieveCreneauGroupe(events, id, creneauxList)
        }
        endTime = System.currentTimeMillis()
        timeElapsed = endTime - startTime
        println("Temps d'import des ics des groupes : $timeElapsed ms")
        println("Nombre de créneaux : " + creneauxList.size)


        // ===== PERSONNEL ====== //
        println("Import des ics des personnel")
        startTime = System.currentTimeMillis()
        for ((id, _) in profs) {
            ICSUtil.ICSScrapper(id, "personnel")
            val events = ICSUtil.ICSParser(id)
            icsService?.retrieveCreneauPersonnel(events, id, creneauxList)
        }
        endTime = System.currentTimeMillis()
        timeElapsed = endTime - startTime
        println("Temps d'import des ics des profs : $timeElapsed ms")
        println("Nombre de créneaux : " + creneauxList.size)


        // ===== SALLES ====== //
        println("Import des ics des salles")
        startTime = System.currentTimeMillis()
        for ((id, _) in salles) {
            ICSUtil.ICSScrapper(id, "salle")
            val events = ICSUtil.ICSParser(id)
            icsService?.retrieveCreneauSalle(events, id, creneauxList)
        }
        endTime = System.currentTimeMillis()
        timeElapsed = endTime - startTime
        println("Temps d'import des ics des salles : $timeElapsed ms")
        println("Nombre de créneaux : " + creneauxList.size)


        // ===== ELEVES ====== //
        println("Import des ics des eleves")
        startTime = System.currentTimeMillis()
        for ((id, _) in eleves) {
            ICSUtil.ICSScrapper(id, "eleve")
            val events = ICSUtil.ICSParser(id)
            icsService?.retrieveCreneauEtudiant(events, id, creneauxList)
        }
        endTime = System.currentTimeMillis()
        timeElapsed = endTime - startTime
        println("Temps d'import des ics des eleves : $timeElapsed ms")
        println("Nombre de créneaux : " + creneauxList.size)


        // Sauvegarde des creneaux de la liste
        println("Sauvegarde des creneaux")
        startTime = System.currentTimeMillis()
        icsService?.saveAllCreneaux(creneauxList)
        endTime = System.currentTimeMillis()
        timeElapsed = endTime - startTime
        println("Temps de sauvegarde des creneaux : $timeElapsed ms")

        // Calcul du temps total en seconde
        val totalTimeElapsed = (System.currentTimeMillis() - totalTime) / 1000
        println("Temps total : $totalTimeElapsed s")
    }

}

