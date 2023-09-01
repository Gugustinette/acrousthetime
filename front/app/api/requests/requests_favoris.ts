//use the base_request to make the requests to the backend
import { get, post, del, OFFLINE } from "./base_request";
//import the creneaux type
import { Favori } from "../types";
//import json data from data.json
import data from "../data";
import { AnyARecord } from "dns";

//get all the favoris
export async function getFavoris(): Promise<Favori> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.favoris);
            }, 1000);
        });
    }
    return await get<Favori>("/favoris");
}

//add a salle to the favoris
export async function addSalleFavori(id: string): Promise<any> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.favoris);
            }, 1000);
        });
    }
    return await post<any, {
        id: string;
    }>("/favoris/salle", {
        id: id
    });
}

//delete a salle from the favoris
export async function deleteSalleFavori(id: string): Promise<any> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.favoris);
            }, 1000);
        });
    }

    return await del<any, {
        id: string;
    }>("/favoris/salle", {
        id: id
    });
}

//add a personnel to the favoris
export async function addPersonnelFavori(id: string): Promise<any> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.favoris);
            }, 1000);
        });
    }
    return await post<any, {
        id: string;
    }>("/favoris/personnel", {
        id: id
    });
}

//delete a personnel from the favoris
export async function deletePersonnelFavori(id: string): Promise<any> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.favoris);
            }, 1000);
        });
    }

    return await del<any, {
        id: string;
    }>("/favoris/personnel", {
        id: id
    });
}

//add a groupe to the favoris
export async function addGroupeFavori(id: string): Promise<any> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.favoris);
            }, 1000);
        });
    }
    return await post<any, {
        id: string;
    }>("/favoris/groupe", {
        id: id
    });
}

//delete a groupe from the favoris
export async function deleteGroupeFavori(id: string): Promise<any> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.favoris);
            }, 1000);
        });
    }

    return await del<any, {
        id: string;
    }>("/favoris/groupe", {
        id: id
    });
}

//add an etudiant to the favoris
export async function addEtudiantFavori(id: string): Promise<any> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.favoris);
            }, 1000);
        });
    }
    return await post<any, {
        id: string;
    }>("/favoris/etudiant", {
        id: id
    });
}

//delete an etudiant from the favoris
export async function deleteEtudiantFavori(id: string): Promise<any> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.favoris);
            }, 1000);
        });
    }

    return await del<any, {
        id: string;
    }>("/favoris/etudiant", {
        id: id
    });
}
