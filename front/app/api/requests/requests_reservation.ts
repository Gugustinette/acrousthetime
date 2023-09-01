//use the base_request to make the requests to the backend
import { get, post, put, del, OFFLINE, convertDateInObject } from "./base_request";
//import the creneaux type
import { Reservation, Salle, Personnel, TypeSalle, StatusReservation } from "../types";

type RequestBody = {
    dt_start: Date,
    dt_end: Date,
    description: string,
    salle: Salle,
    personnel: Personnel
}

type ResponseBody = RequestBody & {
    id: string
}


//get a reservation by id
export async function getReservation(id : string): Promise<Reservation> {
    if(OFFLINE){
        return {
            id: "1",
            dt_start: new Date(),
            dt_end: new Date(),
            description: "test",
            status: StatusReservation.PENDING,
            salle: {
                id: "1",
                name: "test",
                capacity: 1,
                type: TypeSalle.TEST,
                creneaux: [],
                reservations: []
            },
            personnel: {
                id: "1",
                name: "test",
                creneaux: []
            }
        }
    }

    let reservation: Reservation = await get<Reservation>("/reservation/" + id);
    reservation = convertDateInObject<Reservation>(reservation, ["dt_start", "dt_end"]);
    return reservation;
}

//post a new reservation
export async function postReservation(reservation : Reservation): Promise<Reservation> {
    if(OFFLINE){
        return {
            id: "1",
            dt_start: new Date(),
            dt_end: new Date(),
            description: "test",
            status: StatusReservation.PENDING,
            salle: {
                id: "1",
                name: "test",
                capacity: 1,
                type: TypeSalle.TEST,
                creneaux: [],
                reservations: []
            },
            personnel: {
                id: "1",
                name: "test",
                creneaux: []
            }
        }
    }

    let result: Reservation = await post<Reservation, Reservation>("/reservation", reservation);
    result = convertDateInObject<Reservation>(result, ["dt_start", "dt_end"]);
    return result;
}

//get all reservations
export async function getReservations(): Promise<Reservation[]> {
    if(OFFLINE){
        return [{
            id: "1",
            dt_start: new Date(),
            dt_end: new Date(),
            description: "test",
            status: StatusReservation.PENDING,
            salle: {
                id: "1",
                name: "test",
                capacity: 1,
                type: TypeSalle.TEST,
                creneaux: [],
                reservations: []
            },
            personnel: {
                id: "1",
                name: "test",
                creneaux: []
            }
        }]
    }

    let results: Reservation[] = await get<Reservation[]>("/reservation");
    results = results.map((reservation) => {
        reservation = convertDateInObject<Reservation>(reservation, ["dt_start", "dt_end"]);
        return reservation;
    });
    return results;
}

//delete a reservation
export async function deleteReservation(id : string): Promise<any> {
    if(OFFLINE){
        return {
            id: "1",
            dt_start: new Date(),
            dt_end: new Date(),
            description: "test",
            status: StatusReservation.PENDING,
            salle: {
                id: "1",
                name: "test",
                capacity: 1,
                type: TypeSalle.TEST,
                creneaux: [],
                reservations: []
            },
            personnel: {
                id: "1",
                name: "test",
                creneaux: []
            }
        }
    }

    return await del<string, any>("/reservation/" + id, {} as any);
}

//update a reservation
export async function updateReservation(id : string, reservation : Reservation): Promise<Reservation> {
    if(OFFLINE){
        return {
            id: "1",
            dt_start: new Date(),
            dt_end: new Date(),
            description: "test",
            status: StatusReservation.PENDING,
            salle: {
                id: "1",
                name: "test",
                capacity: 1,
                type: TypeSalle.TEST,
                creneaux: [],
                reservations: []
            },
            personnel: {
                id: "1",
                name: "test",
                creneaux: []
            }
        }
    }
    return await put<Reservation, Reservation>("/reservation/" + id, reservation);
}

//get all reservations by user
export async function getReservationsByUser(): Promise<Reservation[]> {
    if(OFFLINE){
        return [{
            id: "1",
            dt_start: new Date(),
            dt_end: new Date(),
            description: "test",
            status: StatusReservation.PENDING,
            salle: {
                id: "1",
                name: "test",
                capacity: 1,
                type: TypeSalle.TEST,
                creneaux: [],
                reservations: []
            },
            personnel: {
                id: "1",
                name: "test",
                creneaux: []
            }
        }]
    }

    let results: Reservation[] = await get<Reservation[]>("/reservation/user");
    results = results.map((reservation) => {
        reservation = convertDateInObject<Reservation>(reservation, ["dt_start", "dt_end"]);
        return reservation;
    });
    return results;
}

// decline a reservation
export async function declineReservation(id : string): Promise<any> {
    if(OFFLINE){
        return id
    }
    return await put<any, any>("/reservation/decline/" + id, id);
}

// accept a reservation
export async function acceptReservation(id : string): Promise<any> {
    if(OFFLINE){
        return id
    }
    return await post<string, string>("/reservation/accept/" + id, id);
}