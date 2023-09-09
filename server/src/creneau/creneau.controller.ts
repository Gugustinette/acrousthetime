import { Controller, Get } from '@nestjs/common';
import { CreneauService } from './creneau.service';
import { Creneau } from 'src/schemas/creneau';

@Controller('creneaux')
export class CreneauController {
  constructor(private readonly creneauService: CreneauService) {}

  @Get('/')
  async getCreneaux(): Promise<Creneau[]> {
    return this.creneauService.getAllCreneaux();
  }
}
