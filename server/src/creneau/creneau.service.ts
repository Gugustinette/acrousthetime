import { Inject, Injectable } from '@nestjs/common';

// Drizzle ORM
/*
import { drizzle } from 'drizzle-orm/postgres-js';
import postgres from 'postgres';
const client = postgres(Bun.env.DATABASE_URL);
const db = drizzle(client);
*/
import { creneaux, Creneau, NewCreneau } from '../schemas/creneau';

@Injectable()
export class CreneauService {
  constructor(@Inject('PG_CONNECTION') private db: any) {}

  async getAllCreneaux(): Promise<Creneau[]> {
    return this.db.select().from(creneaux);
  }

  async addCreneau(creneau: NewCreneau): Promise<Creneau> {
    return this.db
      .insert(creneaux)
      .values(creneau)
      .returning()
      .then((rows) => rows[0]);
  }
}
