//use the base_request to make the requests to the backend
import { get, post, OFFLINE } from "./base_request";
//import the creneaux type
import { Creneaux } from "../types";
//import json data from data.json
import data from "../data";


//get all the creneaux
export async function getCreneaux(): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }

    return await get<Creneaux[]>("/creneaux");
}

//get the favorite creneaux of the user per week
export async function getCreneauxFavorisSemaine(semaine: number): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }

    return await get<Creneaux[]>("/creneaux/favoris/semaine/" + semaine);
}

//get the favorite creneaux of the user per day
export async function getCreneauxFavorisJour(jour: number): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }
    return await get<Creneaux[]>("/creneaux/favoris/jour/" + jour);
}

//get all the creneaux per week
export async function getCreneauxSemaine(semaine: number): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }
    return await get<Creneaux[]>("/creneaux/semaine/" + semaine);
}

//get all the creneaux per day
export async function getCreneauxJour(jour: number): Promise<Creneaux[]> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.creneaux);
            }, 1000);
        });
    }
    return await get<Creneaux[]>("/creneaux/jour/" + jour);
}

//add a new creneau
export async function postCreneau(creneau : Creneaux): Promise<Creneaux> {
    if(OFFLINE){
        return {
            uid: "1",
            dt_start: new Date("2020-01-01 10:00:00"),
            dt_end: new Date("2020-01-01 12:00:00"),
            matiere: "test",
            summary: "test",
            etudiant: [],
            salle: [],
            groupe: [],
            personnel: []
        }
    }
    return await post<Creneaux, Creneaux>("/creneaux", creneau);
}