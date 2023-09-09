CREATE TABLE IF NOT EXISTS "creneaux" (
	"uid" varchar PRIMARY KEY NOT NULL,
	"dt_start" date,
	"dt_end" date,
	"summary" text,
	"matiere" text,
	"last_update" date
);
--> statement-breakpoint
CREATE TABLE IF NOT EXISTS "etudiants" (
	"id" serial PRIMARY KEY NOT NULL,
	"name" text,
	"last_update" date
);
--> statement-breakpoint
CREATE TABLE IF NOT EXISTS "groupes" (
	"id" serial PRIMARY KEY NOT NULL,
	"name" text,
	"last_update" date
);
--> statement-breakpoint
CREATE TABLE IF NOT EXISTS "personnels" (
	"id" serial PRIMARY KEY NOT NULL,
	"name" text,
	"last_update" date
);
--> statement-breakpoint
CREATE TABLE IF NOT EXISTS "salles" (
	"id" serial PRIMARY KEY NOT NULL,
	"name" text,
	"capacity" integer,
	"last_update" date
);
--> statement-breakpoint
CREATE TABLE IF NOT EXISTS "utilisateurs" (
	"id" serial PRIMARY KEY NOT NULL,
	"nom" text,
	"prenom" text,
	"email" text,
	"password" text,
	"last_update" date
);
