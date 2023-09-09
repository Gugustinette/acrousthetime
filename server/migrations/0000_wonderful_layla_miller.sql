CREATE TABLE IF NOT EXISTS "creneaux" (
	"uid" varchar PRIMARY KEY NOT NULL,
	"dt_start" date,
	"dt_end" date,
	"summary" text,
	"matiere" text,
	"last_update" date
);
