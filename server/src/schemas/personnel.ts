import { pgTable, text, serial, date } from 'drizzle-orm/pg-core';

export const personnels = pgTable('personnels', {
  // ID
  id: serial('id').primaryKey(),
  // Nom de l'étudiant
  name: text('name'),
  // Date de dernière mise à jour
  last_update: date('last_update'),
});

// Type pour la sélection des créneaux
export type Personnel = typeof personnels.$inferSelect;
// Type pour l'insertion d'un nouveau créneau
export type NewPersonnel = typeof personnels.$inferInsert;
