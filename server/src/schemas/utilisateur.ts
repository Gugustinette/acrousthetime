import { pgTable, text, serial, date } from 'drizzle-orm/pg-core';

export const utilisateurs = pgTable('utilisateurs', {
  // ID
  id: serial('id').primaryKey(),
  // Nom de l'Utilisateur
  nom: text('nom'),
  // Prénom de l'Utilisateur
  prenom: text('prenom'),
  // Email de l'Utilisateur
  email: text('email'),
  // Mot de passe de l'Utilisateur
  password: text('password'),
  // Date de dernière mise à jour
  last_update: date('last_update'),
});

// Type pour la sélection des créneaux
export type Utilisateur = typeof utilisateurs.$inferSelect;
// Type pour l'insertion d'un nouveau créneau
export type NewUtilisateur = typeof utilisateurs.$inferInsert;
