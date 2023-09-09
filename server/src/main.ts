import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';

// Drizzle ORM
/*
import { drizzle } from 'drizzle-orm/postgres-js';
import { migrate } from 'drizzle-orm/postgres-js/migrator';
import postgres from 'postgres';
const sql = postgres(Bun.env.DATABASE_URL, { max: 1 });
const db = drizzle(sql);
*/

async function bootstrap() {
  // Migrate the database
  // await migrate(db, { migrationsFolder: 'migrations' });
  // Start the server
  const app = await NestFactory.create(AppModule);
  await app.listen(3000);
}
bootstrap();
