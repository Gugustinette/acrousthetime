import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { CreneauModule } from './creneau/creneau.module';
import { ImporticsModule } from './importics/importics.module';
import { ScheduleModule } from '@nestjs/schedule';

@Module({
  imports: [ScheduleModule.forRoot(), CreneauModule, ImporticsModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
