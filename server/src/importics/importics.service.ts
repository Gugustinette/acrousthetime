import { Injectable, Logger } from '@nestjs/common';
// import { Cron } from '@nestjs/schedule';
import DOM from '@mojojs/dom';
import { DateUtil } from '../util/DateUtil';

type Event = {
  uid: string;
  summary: string;
  location: string;
  description: string;
  categories: string;
  startTime: Date;
  endTime: Date;
  matiere?: string;
  personnel?: string;
  groupe?: string;
  salle?: string;
};

@Injectable()
export class ImporticsService {
  private readonly logger = new Logger(ImporticsService.name);

  // @Cron('45 * * * * *')
  async ImportICS(): Promise<any> {
    this.logger.debug('Called when the current second is 45');

    // Liste de tous les évènements
    const events = [];

    // Récupère tous les personnels
    const personnels = await this.ICSLister('personnel');
    // Nombre de personnels
    const personnelLength = Object.keys(personnels).length;
    this.logger.debug('Nombre de personnels : ' + personnelLength);
    let countPersonnels = 0;
    // Pour chaque personnel
    for (const id of Object.keys(personnels)) {
      countPersonnels++;
      this.logger.debug(
        'Personnel ' + countPersonnels + ' / ' + personnelLength,
      );
      // Récupère le fichier ICS
      const icsFile = await this.ICSScrapper(id, 'personnel');
      // Parse le fichier ICS
      const newEvents = await this.ICSParser(icsFile);
      // Ajoute les évènements à la liste
      events.push(...newEvents);
    }

    this.logger.debug(events);
    this.logger.debug("Nombre d'évènements : " + events.length);

    return events;
  }

  /**
   * Permet de récupérer la liste des groupes, élèves, salles ou personnels
   * @param type Type de liste à récupérer
   * @returns Map contenant les id et les noms
   */
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

  /**
   * Permet de récupérer le fichier ICS d'un groupe, élève, salle ou personnel
   * @param id L'id du groupe, élève, salle ou personnel
   * @param type Le type de l'id
   * @returns Le fichier ICS au format string
   */
  async ICSScrapper(id: string, type: string): Promise<string> {
    // URL de base
    const url = 'https://edt.univ-nantes.fr/iut_nantes';
    // Url final
    let finalUrl = '';
    switch (type) {
      case 'groupe':
        finalUrl = url + '/' + id + '.ics';
        break;
      case 'eleve':
        finalUrl = url + '/' + id + '.ics';
        break;
      case 'salle':
        finalUrl = url + '_pers/' + id + '.ics';
        break;
      case 'personnel':
        finalUrl = url + '_pers/' + id + '.ics';
        break;
      default:
        finalUrl = url;
        break;
    }
    // Récupérer le fichier ics
    try {
      const icsFile = await fetch(finalUrl).then((res) => res.text());
      return icsFile;
    } catch (e) {
      this.logger.error('Erreur lors de la récupération des données');
    }
    return '';
  }

  /**
   * Permet de parser un fichier ICS
   * @param ics Le fichier ICS
   * @returns La liste des évènements du fichier ICS
   */
  async ICSParser(ics: string): Promise<Event[]> {
    // Parse le fichier ics
    const lines = ics.split('\n');
    // Créer la liste des évènements
    const events = new Array<Event>();

    // Créer l'évènement courant
    let currentEvent: Event = {
      uid: '',
      summary: '',
      location: '',
      description: '',
      categories: '',
      startTime: new Date(),
      endTime: new Date(),
      matiere: '',
      personnel: '',
      groupe: '',
      salle: '',
    };

    // Parcourir les lignes du fichier
    for (const line of lines) {
      if (line.startsWith('DTSTART')) {
        currentEvent.startTime = DateUtil.convertDate(line);
      }
      if (line.startsWith('DTEND'))
        currentEvent.endTime = DateUtil.convertDate(line);
      if (line.startsWith('UID'))
        currentEvent.uid = line.substring(line.indexOf(':') + 1).split('\r')[0];
      if (line.startsWith('SUMMARY'))
        currentEvent.summary = line
          .substring(line.indexOf(':') + 1)
          .split('\r')[0];
      if (line.startsWith('LOCATION'))
        currentEvent.location = line
          .substring(line.indexOf(':') + 1)
          .split('\r')[0];
      if (line.startsWith('DESCRIPTION'))
        currentEvent.description = line
          .substring(line.indexOf(':') + 1)
          .split('\r')[0];
      if (line.startsWith('CATEGORIES'))
        currentEvent.categories = line
          .substring(line.indexOf(':') + 1)
          .split('\r')[0];
      if (line.startsWith('END:VEVENT')) {
        currentEvent = this.extractDetailsFromDescription(currentEvent);
        events.push(currentEvent);
        currentEvent = {
          uid: '',
          summary: '',
          location: '',
          description: '',
          categories: '',
          startTime: new Date(),
          endTime: new Date(),
          matiere: '',
          personnel: '',
          groupe: '',
          salle: '',
        };
      }
    }

    return events;
  }

  /**
   * Permet d'extraire les détails d'un évènement à partir de sa description
   * @param event L'évènement
   * @returns L'évènement avec les détails
   */
  extractDetailsFromDescription(event: Event): Event {
    // Récupère la description
    const description = event.description;

    // Vérifie qu'une matière est présente
    if (description.includes('Matière : ')) {
      // Récupère la matière
      let matiere = description.substring(
        description.indexOf('Matière : ') + 10,
      );
      // Coupe la matière jusqu'au premier \
      matiere = matiere.substring(0, matiere.indexOf('\\'));

      // Défini la matière
      event.matiere = matiere;
    }
    // Vérifie qu'un personnel est présent
    if (description.includes('Personnel : ')) {
      // Récupère le personnel
      let personnel = description.substring(
        description.indexOf('Personnel : ') + 12,
      );
      // Coupe le personnel jusqu'au premier \
      personnel = personnel.substring(0, personnel.indexOf('\\'));
      // Défini le personnel
      event.personnel = personnel;
    }
    // Vérifie qu'un groupe est présent
    if (description.includes('Groupe : ')) {
      // Récupère le groupe
      let groupe = description.substring(description.indexOf('Groupe : ') + 9);
      // Coupe le groupe jusqu'au premier \
      groupe = groupe.substring(0, groupe.indexOf('\\'));
      // Défini le groupe
      event.groupe = groupe;
    }
    // Vérifie qu'une salle est présente
    if (description.includes('Salle : ')) {
      // Récupère la salle
      let salle = description.substring(description.indexOf('Salle : ') + 8);
      // Coupe la salle jusqu'au premier \
      salle = salle.substring(0, salle.indexOf('\\'));
      // Défini la salle
      event.salle = salle;
    }

    // Renvoie l'évènement
    return event;
  }
}
