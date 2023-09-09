import { Module } from '@nestjs/common';
import { drizzle } from 'drizzle-orm/postgres-js';
import postgres from 'postgres';
// import { creneaux } from './schemas/creneau';
const CONNECTION_STRING = 'postgres://acrousthetime:acrousthetime@db:5432/db';

@Module({
  providers: [
    {
      provide: 'PG_CONNECTION',
      useFactory: () => {
        const client = postgres(CONNECTION_STRING);
        return drizzle(client);
      },
    },
  ],
  exports: ['PG_CONNECTION'],
})
export class DrizzleModule {}
