//use the base_request to make the requests to the backend
import { post, OFFLINE } from "./base_request";
//import authResponse type
import { AuthResponse } from "../types";

//authenticate a user and get a token
export async function authenticate(email: string, password: string): Promise<AuthResponse> {
    if(OFFLINE){
        return {
            token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YW5ndXlAZ21haWwuY29tIiwiaWF0IjoxNjg2NjQ4OTgwLCJleHAiOjE2ODY2NTA0MjB9.mSb1w2XnUMCU-o-HOSQWYMQYlEkNF-fsVD8EFQSUOwo"
        }
    }

    return await post<{email: string, password: string}, AuthResponse>("/auth/authenticate", {email, password});
}

//register a user
export async function register(nom: string, prenom: string, email: string, password: string): Promise<AuthResponse> {
    if(OFFLINE){
        return {
            token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YW5ndXlAZ21haWwuY29tIiwiaWF0IjoxNjg2NjQ4OTgwLCJleHAiOjE2ODY2NTA0MjB9.mSb1w2XnUMCU-o-HOSQWYMQYlEkNF-fsVD8EFQSUOwo"
        }
    }

    return await post<{nom: string, prenom: string, email: string, password: string}, AuthResponse>("/auth/register", {nom, prenom, email, password});
}