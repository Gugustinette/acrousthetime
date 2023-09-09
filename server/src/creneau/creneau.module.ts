import { Module } from '@nestjs/common';
import { CreneauService } from './creneau.service';
import { DrizzleModule } from '../drizzle.module';

@Module({
  providers: [CreneauService],
  imports: [DrizzleModule],
})
export class CreneauModule {}
