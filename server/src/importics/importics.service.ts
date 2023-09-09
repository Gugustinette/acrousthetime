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
}
