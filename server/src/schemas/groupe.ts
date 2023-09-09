import { pgTable, text, serial, date } from 'drizzle-orm/pg-core';

export const groupes = pgTable('groupes', {
  // ID
  id: serial('id').primaryKey(),
  // Nom de l'étudiant
  name: text('name'),
  // Date de dernière mise à jour
  last_update: date('last_update'),
});

// Type pour la sélection des créneaux
export type Groupe = typeof groupes.$inferSelect;
// Type pour l'insertion d'un nouveau créneau
export type NewGroupe = typeof groupes.$inferInsert;
