import { Injectable, Logger } from '@nestjs/common';
// import { Cron } from '@nestjs/schedule';
import DOM from '@mojojs/dom';

@Injectable()
export class ImporticsService {
  private readonly logger = new Logger(ImporticsService.name);

  // @Cron('45 * * * * *')
  async ImportICS(): Promise<any> {
    this.logger.debug('Called when the current second is 45');

    // Récupère tous les groupes
    const result = await this.ICSLister('groupe');

    this.logger.debug('Groupes: ');
    result.forEach((value, key) => {
      this.logger.debug(key + ' ' + value);
    });

    return result;
  }

  async ICSLister(type: string): Promise<Map<string, string>> {
    // URL de base
    const url = 'https://edt.univ-nantes.fr/iut_nantes';

    // Url final
    let finalUrl = '';
    switch (type) {
      case 'groupe':
        finalUrl = url + '/gindex.html';
        break;
      case 'eleve':
        finalUrl = url + '/pindex.html';
        break;
      case 'salle':
        finalUrl = url + '_pers/rindex.html';
        break;
      case 'personnel':
        finalUrl = url + '_pers/sindex.html';
        break;
      default:
        finalUrl = url;
        break;
    }

    // Récupérer les données
    try {
      const doc = await fetch(finalUrl).then((res) => res.text());
      const dom = new DOM(doc);
      // Récupérer les éléments intéressants
      const elements = dom.find('p.nav a');
      const resultMap = new Map<string, string>();
      for (const element of elements) {
        const href = element.attr['href'];
        const idElement = href.substring(
          href.lastIndexOf('/') + 1,
          href.lastIndexOf('.'),
        );
        resultMap[idElement] = element.text();
      }
      return resultMap;
    } catch (e) {
      this.logger.error('Erreur lors de la récupération des données');
    }

    return new Map<string, string>();
  }

  /*
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
  */

  /*
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
  */

  /*
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
  */
}
