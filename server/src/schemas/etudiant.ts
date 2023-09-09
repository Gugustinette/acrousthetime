import { pgTable, text, serial, date } from 'drizzle-orm/pg-core';

export const etudiants = pgTable('etudiants', {
  // ID
  id: serial('id').primaryKey(),
  // Nom de l'étudiant
  name: text('name'),
  // Date de dernière mise à jour
  last_update: date('last_update'),
});

// Type pour la sélection des créneaux
export type Etudiant = typeof etudiants.$inferSelect;
// Type pour l'insertion d'un nouveau créneau
export type NewEtudiant = typeof etudiants.$inferInsert;
