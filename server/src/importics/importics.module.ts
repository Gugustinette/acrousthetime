import { Module } from '@nestjs/common';
import { ImporticsService } from './importics.service';
import { ImporticsController } from './importics.controller';

@Module({
  providers: [ImporticsService],
  controllers: [ImporticsController],
})
export class ImporticsModule {}
