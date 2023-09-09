import { Controller, Get } from '@nestjs/common';
import { ImporticsService } from './importics.service';

@Controller('importics')
export class ImporticsController {
  constructor(private readonly importicsService: ImporticsService) {}

  @Get('/')
  async importIcs(): Promise<any> {
    return this.importicsService.ImportICS();
  }
}
