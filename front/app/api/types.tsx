import { type } from "os"
import { Type } from "typescript"

// The type definitions for the backend API
// Export all types at once to be used in the requests
export type {
    Creneaux,
    Etudiant,
    Groupes,
    Personnel,
    Reservation,
    Salle,
    User,
    Favori,
    AuthResponse
}

    type Creneaux = {
        uid: String,
        dt_start: Date,
        dt_end: Date,
        summary: String,
        matiere: String,
        etudiant: Etudiant[],
        salle: Salle[],
        groupe: Groupes[],
        personnel: Personnel[]
    }
    
    type Etudiant = {
        id: String,
        name: String,
        creneaux: Creneaux[]
    }
    
    type Groupes = {
        id: String,
        name: String,
        creneaux: Creneaux[]
    }

    type Personnel = {
        id: String,
        name: String,
        creneaux: Creneaux[]
    }

    type Reservation = {
        id?: String,
        dt_start: Date,
        dt_end: Date,
        description: String,
        salle: Salle,
        personnel?: Personnel,
        status: StatusReservation
    }

    type Salle = {
        id: String,
        name: String,
        capacity: number,
        type: TypeSalle,
        creneaux: Creneaux[],
        reservations: Reservation[]
    }
    
    type User = {
        nom: String,
        prenom: String,
        email: String,
        password: String,
        roles: Role
    }

    type Favori = {
        etudiant: Etudiant[],
        groupe: Groupes[],
        personnel: Personnel[]
        salle: Salle[]
    }

    type AuthResponse = {
        token: String
    }
    
    export enum Role {
        ADMIN = "ADMIN",
        USER = "USER"
    }
    
    export enum TypeSalle {
        TD = "TD",
        TP = "TP",
        COURS = "COURS",
        TEST = "test"
    }

    export enum StatusReservation {
        ACCEPTED = "ACCEPTED",
        REFUSED = "REFUSED",
        PENDING = "PENDING",
    }
    
