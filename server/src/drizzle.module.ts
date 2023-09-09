import { Module } from '@nestjs/common';
import { drizzle } from 'drizzle-orm/postgres-js';
import * as postgres from 'postgres';
const CONNECTION_STRING = Bun.env.DATABASE_URL;
import { PG_CONNECTION } from './constants';

@Module({
  providers: [
    {
      provide: PG_CONNECTION,
      useFactory: () => {
        const client = postgres(CONNECTION_STRING);
        return drizzle(client);
      },
    },
  ],
  exports: [PG_CONNECTION],
})
export class DrizzleModule {}
