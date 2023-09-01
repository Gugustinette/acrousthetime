//use the base_request to make the requests to the backend
import { get, post, tokenRetriever, OFFLINE, convertDateInObject } from "./base_request";
//import the creneaux type
import { Etudiant, Creneaux } from "../types";
//import json data from data.json
import data from "../data";

//get all the etudiants
export async function getEtudiants(): Promise<Etudiant[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.etudiant);
            }, 1000);
        });
    }
    return await get<Etudiant[]>("/etudiant");
}

//add a new etudiant
export async function postEtudiant(etudiant : Etudiant): Promise<Etudiant> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            creneaux: []
        }
    }
    return await post<Etudiant, Etudiant>("/etudiant", etudiant);
}

//add creneaux to an etudiant
export async function postCreneauEtudiant(id : string, creneaux : Creneaux[]): Promise<Etudiant> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            creneaux: creneaux
        }
    }
    return await post<Creneaux[], Etudiant>("/etudiant/ + " + id + "/addCreneaux", creneaux);
}

//get etudiant data by id by week
export async function getEtudiantSemaine(id : string, semaine: Date): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }

    let results: Creneaux[] = await get<Creneaux[]>("/etudiant/" + id + "/semaine/" + semaine.toISOString());
    results = results.map((creneau) => {
        creneau = convertDateInObject<Creneaux>(creneau, ["dt_start", "dt_end"]);
        return creneau;
    });
    return results;
}

//get etudiant data by id by day
export async function getEtudiantJour(id : string, jour: Date): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }

    let results: Creneaux[] = await get<Creneaux[]>("/etudiant/" + id + "/jour/" + jour.toISOString());
    results = results.map((creneau) => {
        creneau = convertDateInObject<Creneaux>(creneau, ["dt_start", "dt_end"]);
        return creneau;
    });
    return results;
}

//get etudiant by id
export async function getEtudiantById(id : string): Promise<Etudiant> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            creneaux: []
        }
    }
    return await get<Etudiant>("/etudiant/" + id);
}