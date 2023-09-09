import { pgTable, text, varchar, date } from 'drizzle-orm/pg-core';

export const creneaux = pgTable('creneaux', {
  // ID
  uid: varchar('uid').primaryKey(),
  // Date de début du créneau
  dt_start: date('dt_start'),
  // Date de fin du créneau
  dt_end: date('dt_end'),
  // Résumé du créneau
  summary: text('summary'),
  // Matière du créneau
  matiere: text('matiere'),
  // Date de dernière mise à jour
  last_update: date('last_update'),
});

// Type pour la sélection des créneaux
export type Creneau = typeof creneaux.$inferSelect;
// Type pour l'insertion d'un nouveau créneau
export type NewCreneau = typeof creneaux.$inferInsert;
