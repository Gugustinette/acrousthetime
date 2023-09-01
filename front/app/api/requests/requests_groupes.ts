//use the base_request to make the requests to the backend
import { get, post, OFFLINE, convertDateInObject } from "./base_request";
//import the creneaux type
import { Groupes, Creneaux } from "../types";
//import json data from data.json
import data from "../data";

//get a group data per week and id
export async function getGroupeSemaine(id : string, semaine: Date): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }

    let results: Creneaux[] = await get<Creneaux[]>("/groupe/" + id + "/semaine/" + semaine.toISOString());
    results = results.map((creneau) => {
        creneau = convertDateInObject<Creneaux>(creneau, ["dt_start", "dt_end"]);
        return creneau;
    });
    return results;
}

//get a group data per day and id
export async function getGroupeJour(id : string, jour: Date): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }

    let results: Creneaux[] = await get<Creneaux[]>("/groupe/" + id + "/jour/" + jour.toISOString());
    results = results.map((creneau) => {
        creneau = convertDateInObject<Creneaux>(creneau, ["dt_start", "dt_end"]);
        return creneau;
    });
    return results;
}

//add a new group
export async function postGroupe(groupes : Groupes): Promise<Groupes> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            creneaux: []
        }
    }
    return await post<Groupes, Groupes>("/groupe", groupes);
}

//add a new creneau to a group
export async function postCreneauGroupe(id : string, creneau : Creneaux): Promise<Groupes> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            creneaux: [creneau]
        }
    }
    return await post<Creneaux, Groupes>("/groupe/ + " + id + "/addCreneau", creneau);
}

//get groupes by id
export async function getGroupeById(id : string): Promise<Groupes> {
    if(OFFLINE){
        return {
            id: "1",
            name: "test",
            creneaux: []
        }
    }
    return await get<Groupes>("/groupe/" + id);
}

//get all the groupes
export async function getGroupes(): Promise<Groupes[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.groupes);
            }, 1000);
        });
    }

    return await get<Groupes[]>("/groupe");
}