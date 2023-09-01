//use the base_request to make the requests to the backend
import { get, post, OFFLINE, convertDateInObject } from "./base_request";
//import the creneaux type
import { Creneaux, Salle, TypeSalle } from "../types";
//import json data from data.json
import data from "../data";

//get data about the salles per id and per week
export async function getSallesSemaine(id : string, semaine: Date): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }

    let results: Creneaux[] = await get<Creneaux[]>("/salle/" + id + "/semaine/" + semaine.toISOString());
    results = results.map((creneau) => {
        creneau = convertDateInObject<Creneaux>(creneau, ["dt_start", "dt_end"]);
        return creneau;
    });
    return results;
}

//get data about the salles per id and per day
export async function getSallesJour(id : string, jour: Date): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }

    let results: Creneaux[] = await get<Creneaux[]>("/salle/" + id + "/jour/" + jour.toISOString());
    results = results.map((creneau) => {
        creneau = convertDateInObject<Creneaux>(creneau, ["dt_start", "dt_end"]);
        return creneau;
    });
    return results;
}

//post a new salle
export async function postSalle(salle : Salle): Promise<Salle> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            capacity: 1,
            type: TypeSalle.TEST,
            creneaux: [],
            reservations: []
        }
    }
    return await post<Salle, Salle>("/salle/", salle);
}

//post a new creneau to a salle
export async function postCreneauSalle(id : string, creneau : Creneaux): Promise<Salle> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            capacity: 1,
            type: TypeSalle.TEST,
            creneaux: [creneau],
            reservations: []
        }
    }
    return await post<Creneaux, Salle>("/salle/" + id + "/addCreneaux/", creneau);
}

//get salles by id
export async function getSallesById(id : string): Promise<Salle> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            capacity: 1,
            type: TypeSalle.TEST,
            creneaux: [],
            reservations: []
        }
    }
    return await get<Salle>("/salle/" + id);
}

//get all salles
export async function getSalles(): Promise<Salle[]> {
    if(OFFLINE){
        return data.salle;
    }
    return await get<Salle[]>("/salle");
}

//get all available salles for a duration
export async function getSallesDispo(date_debut : Date, date_fin : Date): Promise<Salle[]> {
    if(OFFLINE){
        return data.salle;
    }
    return await get<Salle[]>("/salle/date_debut/" + date_debut.toISOString() + "/date_fin/" + date_fin.toISOString());
}