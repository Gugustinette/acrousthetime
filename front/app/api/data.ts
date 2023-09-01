import { Creneaux, Etudiant, Groupes, Personnel, Reservation, Salle, User, Role, TypeSalle, Favori, StatusReservation } from './types'

const data =
{
    user: [
        {
            nom: "Smith",
            prenom: "John",
            email: "john.smith@test.test",
            password: "123456",
            roles: Role.ADMIN
        },
        {
            nom: "Doe",
            prenom: "Jane",
            email: "jane.doe@test.test",
            password: "123456",
            roles: Role.USER
        }
    ] as User[],
    salle: [
        {
            id: "1",
            name: "Salle 1",
            capacity: 10,
            type: TypeSalle.TD,
            creneaux: [],
            reservations: []
        },
        {
            id: "2",
            name: "Salle 2",
            capacity: 20,
            type: TypeSalle.TP,
            creneaux: [],
            reservations: []
        },
        {
            id: "3",
            name: "Salle 3",
            capacity: 30,
            type: "casse",
            creneaux: [],
            reservations: []
        }
    ] as Salle[],
    reservation: [
        {
            id: "1",
            dt_start: new Date("2020-01-01 10:00:00"),
            dt_end: new Date("2020-01-01 12:00:00"),
            description: "Cours de math",
            salle:
            //create a new salle
            {
                id: "1",
                name: "Salle 1",
                capacity: 10,
                type: TypeSalle.TD,
                creneaux: [],
                reservations: []
            }
            ,
            personnel:
            //create a new personnel
            {
                id: "1",
                name: "John Doe",
                creneaux: []
            },
            status: StatusReservation.PENDING,
        },
        {
            id: "2",
            dt_start: new Date("2023-06-15 5:00:00"),
            dt_end: new Date("2023-06-15 23:00:00"),
            description: "Cours de physique",
            salle:
            //create a new salle
            {
                id: "1",
                name: "Salle 1",
                capacity: 10,
                type: TypeSalle.TD,
                creneaux: [],
                reservations: []
            },
            "personnel": [],
            status: StatusReservation.ACCEPTED,
        },
        {
            id: "3",
            dt_start: new Date("2023-06-15 10:00:00"),
            dt_end: new Date("2023-06-15 12:00:00"),
            description: "Cours de chimie",
            salle:
            //create a new salle
            {
                id: "1",
                name: "Salle 1",
                capacity: 10,
                type: TypeSalle.TD,
                creneaux: [],
                reservations: []
            },
            "personnel": [],
            status: StatusReservation.REFUSED,
        }
    ] as Reservation[],
    personnel: [
        {
            id: "1",
            name: "John Doe",
            creneaux: []
        },
        {
            id: "2",
            name: "Jane Doe",
            creneaux: []
        }
    ] as Personnel[],
    groupes: [
        {
            id: "1",
            name: "Groupe 1",
            creneaux: []
        },
        {
            id: "2",
            name: "Groupe 2",
            creneaux: []
        }
    ] as Groupes[],
    etudiant: [
        {
            id: "1",
            name: "John Doe",
            creneaux: []
        },
        {
            id: "2",
            name: "Jane Doe",
            creneaux: []
        }
    ] as Etudiant[],
    creneaux: [
        {
            uid: "1",
            dt_start: new Date("2020-01-01 08:00:00"),
            dt_end: new Date("2020-01-01 09:00:00"),
            summary: "TD - 1",
            matiere: "Math",
            etudiant: [
                //create a new etudiant
                {
                    id: "1",
                    name: "John Doe",
                    creneaux: []
                },
                {
                    id: "2",
                    name: "Jane Doe",
                    creneaux: []
                }
            ],
            salle: [
                {
                    id: "1",
                    name: "Salle 1",
                    capacity: 10,
                    type: TypeSalle.TD,
                    creneaux: [],
                    reservations: []
                },
                {
                    id: "2",
                    name: "Salle 2",
                    capacity: 20,
                    type: TypeSalle.TD,
                    creneaux: [],
                    reservations: []
                }
            ],
            groupe: [
                //create a new groupe
                {
                    id: "1",
                    name: "Groupe 1",
                    creneaux: []
                },
                {
                    id: "2",
                    name: "Groupe 2",
                    creneaux: []
                }
            ],
            personnel: [
                //create a new personnel
                {
                    id: "1",
                    name: "John Doe",
                    creneaux: []
                },
            ]
        },
        {
            "uid": "2",
            "dt_start": new Date("2020-01-01 10:00:00"),
            "dt_end": new Date("2020-01-01 16:00:00"),
            "summary": "TD - Maths",
            "etudiant": [
                //create a new etudiant
                {
                    "id": "1",
                    "name": "John Doe",
                    "creneaux": []
                },
                {
                    "id": "2",
                    "name": "Jane Doe",
                    "creneaux": []
                }
            ],
            "salle": [
                {
                    "id": "1",
                    "name": "Salle 1",
                    "capacity": 10,
                    "type": TypeSalle.TD,
                    "creneaux": [],
                    "reservations": []
                },
                {
                    "id": "2",
                    "name": "Salle 2",
                    "capacity": 20,
                    "type": TypeSalle.TD,
                    "creneaux": [],
                    "reservations": []
                }
            ],
            "groupe": [
                //create a new groupe
                {
                    "id": "1",
                    "name": "Groupe 1",
                    "creneaux": []
                },
                {
                    "id": "2",
                    "name": "Groupe 2",
                    "creneaux": []
                }
            ],
            "personnel": [
                //create a new personnel
                {
                    "id": "1",
                    "name": "John Doe",
                    "creneaux": []
                },
            ]
        },
        {
            "uid": "3",
            "dt_start": new Date("2020-01-01 14:00:00"),
            "dt_end": new Date("2020-01-01 16:00:00"),
            "summary": "TP - Physique",
            "etudiant": [
                //create a new etudiant
                {
                    "id": "1",
                    "name": "John Doe",
                    "creneaux": []
                },
                {
                    "id": "2",
                    "name": "Jane Doe",
                    "creneaux": []
                }
            ],
            "salle": [
                //create a new salle
                {
                    "id": "1",
                    "name": "Salle 1",
                    "capacity": 10,
                    "type": TypeSalle.TD,
                    "creneaux": [],
                    "reservations": []
                },
            ],
            "groupe": [
                //create a new groupe
                {
                    "id": "1",
                    "name": "Groupe 1",
                    "creneaux": []
                },
            ],
            "personnel": [
                //create a new personnel
                {
                    "id": "1",
                    "name": "John Doe",
                    "creneaux": []
                },
            ]
        },
        {
            "uid": "4",
            "dt_start": new Date("2020-01-01 16:30:00"),
            "dt_end": new Date("2020-01-01 18:00:00"),
            "summary": "CM - Physique",
            "etudiant": [
                //create a new etudiant
                {
                    "id": "1",
                    "name": "John Doe",
                    "creneaux": []
                },
                {
                    "id": "2",
                    "name": "Jane Doe",
                    "creneaux": []
                }
            ],
            "salle": [
                //create a new salle
                {
                    "id": "1",
                    "name": "Salle 1",
                    "capacity": 10,
                    "type": TypeSalle.TD,
                    "creneaux": [],
                    "reservations": []
                },
            ],
            "groupe": [
                //create a new groupe
                {
                    "id": "1",
                    "name": "Groupe 1",
                    "creneaux": []
                },
            ],
            "personnel": [
                //create a new personnel
                {
                    "id": "1",
                    "name": "John Doe",
                    "creneaux": []
                },
            ]
        },
        {
            "uid": "5",
            "dt_start": new Date("2020-01-01 17:00:00"),
            "dt_end": new Date("2020-01-01 20:00:00"),
            "summary": "Créneau spécial",
            "etudiant": [
                //create a new etudiant
                {
                    "id": "1",
                    "name": "John Doe",
                    "creneaux": []
                },
                {
                    "id": "2",
                    "name": "Jane Doe",
                    "creneaux": []
                }
            ],
            "salle": [
                //create a new salle
                {
                    "id": "1",
                    "name": "Salle 1",
                    "capacity": 10,
                    "type": TypeSalle.TD,
                    "creneaux": [],
                    "reservations": []
                },
            ],
            "groupe": [
                //create a new groupe
                {
                    "id": "1",
                    "name": "Groupe 1",
                    "creneaux": []
                },
            ],
            "personnel": [
                //create a new personnel
                {
                    "id": "1",
                    "name": "John Doe",
                    "creneaux": []
                },
            ]
        },
        {
            "uid": "6",
            "dt_start": new Date("2020-01-01 18:00:00"),
            "dt_end": new Date("2020-01-01 20:00:00"),
            "summary": "Créneau spécial",
            "etudiant": [
                //create a new etudiant
                {
                    "id": "1",
                    "name": "John Doe",
                    "creneaux": []
                },
                {
                    "id": "2",
                    "name": "Jane Doe",
                    "creneaux": []
                }
            ],
            "salle": [
                //create a new salle
                {
                    "id": "1",
                    "name": "Salle 1",
                    "capacity": 10,
                    "type": TypeSalle.TD,
                    "creneaux": [],
                    "reservations": []
                },
            ],
            "groupe": [
                //create a new groupe
                {
                    "id": "1",
                    "name": "Groupe 1",
                    "creneaux": []
                },
            ],
            "personnel": [
                //create a new personnel
                {
                    "id": "1",
                    "name": "John Doe",
                    "creneaux": []
                },
            ]
        }
    ] as Creneaux[],
    favoris: {
        etudiant: [],
        salle: [],
        groupe: [],
        personnel: []
    } as Favori,
};

export default data;