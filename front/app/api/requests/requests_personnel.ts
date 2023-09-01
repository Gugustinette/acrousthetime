//use the base_request to make the requests to the backend
import { get, post, OFFLINE, convertDateInObject } from "./base_request";
//import the creneaux type
import { Personnel, Creneaux } from "../types";
//import json data from data.json
import data from "../data";

//get a personnel data per week and id
export async function getPersonnelSemaine(id : string, semaine: Date): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }

    let results: Creneaux[] = await get<Creneaux[]>("/personnel/" + id + "/semaine/" + semaine.toISOString());
    results = results.map((creneau) => {
        creneau = convertDateInObject<Creneaux>(creneau, ["dt_start", "dt_end"]);
        return creneau;
    });
    return results;
}

//get a personnel data per day and id
export async function getPersonnelJour(id : string, jour: Date): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }

    let results: Creneaux[] = await get<Creneaux[]>("/personnel/" + id + "/jour/" + jour.toISOString());
    results = results.map((creneau) => {
        creneau = convertDateInObject<Creneaux>(creneau, ["dt_start", "dt_end"]);
        return creneau;
    });
    return results;
}

//add creneaux to a personnel
export async function postCreneauPersonnel(id : string, creneaux : Creneaux[]): Promise<Personnel> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            creneaux: creneaux
        }
    }
    return await post<Creneaux[], Personnel>("/personnel/ + " + id + "/addCreneau", creneaux);
}

//add a new personnel
export async function postPersonnel(personnel : Personnel): Promise<Personnel> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            creneaux: []
        }
    }
    return await post<Personnel, Personnel>("/personnel", personnel);
}

//get personnel by id
export async function getPersonnelById(id : string): Promise<Personnel> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            creneaux: []
        }
    }
    return await get<Personnel>("/personnel/" + id);
}

//get all the personnel
export async function getPersonnel(): Promise<Personnel[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.personnel);
            }, 1000);
        });
    }

    return await get<Personnel[]>("/personnel");
}