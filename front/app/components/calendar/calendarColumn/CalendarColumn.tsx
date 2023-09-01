import { Creneaux } from '@/app/api/types';
import styles from './CalendarColumn.module.scss'
import Creneau from './creneau/Creneau'
import CalendarConst from '../CalendarConst';
import React from 'react';

export default function CalendarColumn(props: { creneaux: Creneaux[] }) {

    const getTimeDifference = (start: Date, end: Date): string => {
        const diffInMinutes = Math.abs(end.getTime() - start.getTime()) / (1000 * 60);
        const hours = Math.floor(diffInMinutes / 60);
        const minutes = Math.floor(diffInMinutes % 60);

        return `${hours}h${minutes.toString().padStart(2, '0')}`;
    };

    function getTime(date: Date): number {
        return date.getHours() + date.getMinutes() / 60;
    }

    function getTop(date: Date): number {
        return (getTime(date) - CalendarConst.startHour) * CalendarConst.creneauHeight + 2;
    }

    function getHeight(dateStart: Date, dateEnd: Date): number {
        return (getTime(dateEnd) - getTime(dateStart)) * CalendarConst.creneauHeight - 2;
    }

    function checkOverlap(creneauA: Creneaux, creneauB: Creneaux): boolean {
        return creneauA.dt_start < creneauB.dt_end && creneauB.dt_start < creneauA.dt_end;
    }

    let overlappingCreneaux: Creneaux[][] = [];

    props.creneaux.forEach((creneau) => {
        let foundGroup = false;

        // Parcourir les groupes existants pour vérifier si le créneau s'y chevauche
        overlappingCreneaux.forEach((group) => {
            if (group.some((groupCreneau) => checkOverlap(groupCreneau, creneau))) {
                group.push(creneau);
                foundGroup = true;
            }
        });

        // Si le créneau ne se chevauche avec aucun groupe existant, créer un nouveau groupe
        if (!foundGroup) {
            overlappingCreneaux.push([creneau]);
        }
    });

    return (
        <div className={styles.colonne}>

            {overlappingCreneaux.map((group, groupIndex) => (
                <div key={groupIndex}>
                    {group.map((creneau, creneauIndex) => (
                        <React.Fragment key={creneauIndex}>
                            <Creneau
                                creneau={creneau}
                                styleOverLapping={{
                                    width: `${100 / group.length}%`,
                                    left: `${(100 / (group.length - 1)) * creneauIndex}%`,
                                    transform: `translateX(-${(100 / (group.length - 1)) * creneauIndex}%)`
                                }}
                            />
                        </React.Fragment>
                    ))}
                </div>
            ))}

            <div className={styles.rows}>
                {[...Array(CalendarConst.endHour - CalendarConst.startHour + 1)].map((x, i) =>
                    <div key={i} style={{ height: `${CalendarConst.creneauHeight}px` }}></div>
                )}
            </div>
        </div>
    )
}
