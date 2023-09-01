package fr.iutna.lpmiar.td1.util

import main.kotlin.fr.iutna.lpmiar.td1.util.Event
import org.jsoup.Jsoup
import java.io.File
import java.net.URL
import org.springframework.util.FileCopyUtils

class ICSUtil {
    companion object {

        // Récupérer la liste des ics
        fun ICSLister(type : String): Map <String, String> {
            //configuration de l'url
            val url = "https://edt.univ-nantes.fr/iut_nantes"
            val finalUrl = when (type) {
                "groupe" -> url + "/gindex.html"
                "eleve" -> url + "/pindex.html"
                "salle" -> url + "_pers/rindex.html"
                "personnel" -> url + "_pers/sindex.html"
                else -> url
            }
            // Récupérer les données
            try {
                val doc = Jsoup.connect(finalUrl).get()
                // Récupérer les éléments intéressants
                val elements = doc.select("p.nav a")
                val resultMap = mutableMapOf<String, String>()
                for (element in elements) {
                    val href = element.attr("href")
                    val idElement = href.substringBeforeLast(".html")
                    resultMap[idElement] = element.text()
                }
                return resultMap
            } catch (e: Exception) {
                e.printStackTrace()
                println("Erreur lors de la récupération des données")
            }
            return emptyMap()
        }

        // Récupérer l'ics de l'id
        fun ICSScrapper(id: String, type: String) {
            //configuration de l'url
            val url = "https://edt.univ-nantes.fr/iut_nantes"
            val finalUrlString = when (type) {
                "groupe" -> url + "/$id.ics"
                "eleve" -> url + "/$id.ics"
                "salle" -> url + "_pers/$id.ics"
                "personnel" -> url + "_pers/$id.ics"
                else -> url
            }
            val finalUrl = URL(finalUrlString)
            //Gestion du fichier
            val fileName = "src/main/resources/ics/${id}.ics" // Chemin absolu du fichier
            val icsFile = File(fileName)
            if (!icsFile.exists()) { icsFile.createNewFile() } // Créer le fichier s'il n'existe pas

            try {
                // Télécharger et enregistrer le fichier
                icsFile.outputStream().use { outputStream ->
                    FileCopyUtils.copy(finalUrl.openStream(), outputStream)
                }
            } catch (e: Exception) {
                println("Erreur lors de la récupération des données")
            }
        }
        // Parser l'ics
        fun ICSParser(id: String): List<Event> {
            val fileName = "src/main/resources/ics/${id}.ics" // Chemin absolu du fichier
            val icsFile = File(fileName)
            val events = mutableListOf<Event>()

            try {
                val lines = icsFile.readLines()
                var currentEvent: Event? = null

                lines.forEach { line ->
                    when {
                        line.startsWith("BEGIN:VEVENT") -> currentEvent = Event("", "", "", "", "", "", "", null, null, null, null)
                        line.startsWith("DTSTART:") -> currentEvent?.startTime = line.substringAfter("DTSTART:")
                        line.startsWith("DTEND:") -> currentEvent?.endTime = line.substringAfter("DTEND:")
                        line.startsWith("UID:") -> currentEvent?.uid = line.substringAfter("UID:")
                        line.startsWith("SUMMARY:") -> currentEvent?.summary = line.substringAfter("SUMMARY:")
                        line.startsWith("LOCATION:") -> currentEvent?.location = line.substringAfter("LOCATION:")
                        line.startsWith("DESCRIPTION:") -> {
                            currentEvent?.description = line.substringAfter("DESCRIPTION:")
                            extractDetailsFromDescription(currentEvent)
                        }
                        line.startsWith("CATEGORIES:") -> currentEvent?.categories = line.substringAfter("CATEGORIES:")
                        line.startsWith("END:VEVENT") -> {
                            currentEvent?.let {
                                events.add(it)
                            }
                            //println(currentEvent.toString() + "\n")
                            currentEvent = null
                        }
                    }
                }
            } catch (e: Exception) {
                println("Erreur lors de la récupération des données")
            }

            return events
        }
        // Extraire les détails de la description
        private fun extractDetailsFromDescription(event: Event?) {

            val description = event?.description
            val matiere = description?.substringAfter("Matière : ")?.substringBefore("\\n")
            val personnel = description?.substringAfter("Personnel : ")?.substringBefore("\\n")
            val groupe = description?.substringAfter("Groupe : ")?.substringBefore("\\n")
            val salle = description?.substringAfter("Salle : ")?.substringBefore("\\n")

            event?.matiere = matiere
            event?.personnel = personnel
            event?.groupe = groupe
            event?.salle = salle
        }

    }
}
