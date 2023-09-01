//use the base_request to make the requests to the backend
import { get, post, OFFLINE, getToken } from "./base_request";
//import json data from data.json
import data from "../data";

//get image 
export async function getImage() {
    if(OFFLINE){
        return "data.image";
    }

    return await get("/image/user?" + getToken());
}

//post image
export async function postImage(image: string) {
    if(OFFLINE){
        return image;
    }

    return await post<{image: string}, string>("/image/upload", {image});
}