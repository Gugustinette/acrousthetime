import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { CreneauModule } from './creneau/creneau.module';

@Module({
  imports: [CreneauModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
