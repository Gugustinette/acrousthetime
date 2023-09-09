import { Module } from '@nestjs/common';
import { CreneauService } from './creneau.service';
import { DrizzleModule } from '../drizzle.module';
import { CreneauController } from './creneau.controller';

@Module({
  providers: [CreneauService],
  imports: [DrizzleModule],
  controllers: [CreneauController],
})
export class CreneauModule {}
