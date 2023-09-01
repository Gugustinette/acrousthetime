//use the base_request to make the requests to the backend
import { get, post, OFFLINE } from "./base_request";
//import json data from data.json
import data from "../data";
import { Favori } from "../types";

// get results from the search
export async function getSearchResults(search: string): Promise<Favori> {
    if(OFFLINE){
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(data.favoris);
            }, 1000);
        });
    }
    return await get<Favori>("/recherche?search=" + search);
}