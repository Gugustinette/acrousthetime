import { pgTable, text, serial, date, integer } from 'drizzle-orm/pg-core';

export const salles = pgTable('salles', {
  // ID
  id: serial('id').primaryKey(),
  // Nom de le salle
  name: text('name'),
  // Capacité de la salle
  capacity: integer('capacity'),
  // Date de dernière mise à jour
  last_update: date('last_update'),
});

// Type pour la sélection des créneaux
export type Salle = typeof salles.$inferSelect;
// Type pour l'insertion d'un nouveau créneau
export type NewSalle = typeof salles.$inferInsert;
