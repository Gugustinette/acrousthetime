// Get API URL from .env

import { authenticate } from "./requests_authenticate";

//export const OFFLINE = process.env.NEXT_PUBLIC_OFFLINE === "OFFLINE"
export const OFFLINE = false
export const API_URL = "http://localhost:10000";
export const API_URL2 = "https://api.acrousthetime.gugustinette.com";

//import authentification requests
export {authenticate} from "./requests_authenticate";

//import AuthResponse type
import {AuthResponse} from "../types";

// Base request function
async function http<T>(path: string, config: RequestInit): Promise<T> {
    const request = new Request(API_URL2 + path, config)
    const isAuth = path === "/auth/authenticate" || path === "/auth/register";
    const response = await fetch(request, {
        headers: isAuth ? {
            'Content-Type': 'application/json'
        } : {
            'Content-Type': 'application/json',
            Authorization: "Bearer " + getToken()
        }
    });
    if (!response.ok) {
        throw new Error(response.statusText);
    }
    // If the response doesn't contain JSON, return with an empty object (check content-length header)
    if (response.headers.get("content-length") === "0") {
        return {} as T;
    }
    return await response.json();
}

// Get request
export async function get<T>(
    path: string,
    config?: RequestInit
): Promise<T> {
    const init = { method: "get", ...config };
    return await http<T>(path, init);
}

// Post request
export async function post<T, U>
    (path: string, body: T, config?: RequestInit): Promise<U> {
    const init = { method: "post", body: JSON.stringify(body), ...config };
    return await http<U>(path, init);
}

// Put request
export async function put<T, U>
    (path: string, body: T, config?: RequestInit): Promise<U> {
    const init = { method: "put", body: JSON.stringify(body), ...config };
    return await http<U>(path, init);
}

// Delete request
export async function del<T, U>
    (path: string, body: T, config?: RequestInit): Promise<U> {
    const init = { method: "delete", body: JSON.stringify(body), ...config };
    return await http<U>(path, init);
}


/**
 * Get the token from the local storage or from the authenticate function
 * @returns the token from the local storage or from the authenticate function
 */
export async function tokenRetriever(): Promise<string> {
    var token = localStorage.getItem("token") || ""
    var response = {
        token: ""
    } as AuthResponse
    if(token === null || token === "") {
        response = await authenticate("tanguy@gmail.com", "123")
        localStorage.setItem("token", response.token.toString())
    }
    return response.token.toString();
}

export function setToken(token: String) {
    localStorage.setItem("token", token.toString())
}

export function getToken(): String {
    var result = {
        token: localStorage.getItem("token") || "",
        error: "No token found"
    }
    if(localStorage.getItem("token") === null || localStorage.getItem("token") === "") {
        throw new Error(result.error)
    }
    return result.token
}

/**
 * Fonctions utilitaires pour convertir les dates quand une requêtes renvoie des objets contenant des dates
 */
export function convertDateInObject<T extends object>(object: T, values: string[]): T {
    const convertedObject = { ...object } as any;

    for (const key of Object.keys(convertedObject)) {
      if (values.includes(key)) {
        const value = convertedObject[key];
        if (typeof value === "string") {
          const date = new Date(value);
          if (!isNaN(date.getTime())) {
            convertedObject[key] = date;
          }
        }
      }
    }

    return convertedObject;
}
