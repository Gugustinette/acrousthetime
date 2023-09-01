import { Creneaux } from '@/app/api/types';
import CalendarConst from '../CalendarConst';
import styles from './CalendarHours.module.scss'
import { useEffect, useState } from 'react';

export default function CalendarHours(props: {
    creneaux: Creneaux[]
}) {
    // Total number of hours
    const [total, setTotal] = useState(0);
    // Additional number of minutes
    const [additionalMinutes, setAdditionalMinutes] = useState(0);
    // Hours
    const hours = [];

    for (let hour = CalendarConst.startHour; hour <= CalendarConst.endHour; hour++) {
        const formattedHour = hour.toString().padStart(2, '0') + ':00';
        hours.push(
            <div className={styles.row} style={{ height: `${CalendarConst.creneauHeight}px` }} key={formattedHour}>{formattedHour}</div>
        );
    }

    useEffect(() => {
        // If there is no creneau, set the total to 0
        if (props.creneaux === undefined || props.creneaux.length === 0) {
            setTotal(0);
            return;
        }
        // Compute the total number of hours
        let totalTemp = 0;
        props.creneaux.forEach(creneau => {
            const start = new Date(creneau.dt_start);
            const end = new Date(creneau.dt_end);
            totalTemp += (end.getTime() - start.getTime()) / 1000 / 60 / 60;
        });
        // Convert the total number of hours to a number of hours and minutes
        const hours = Math.floor(totalTemp);
        const minutes = Math.round((totalTemp - hours) * 60);
        // If the number of minutes is greater than 60, add an hour
        if (minutes >= 60) {
            setTotal(hours + 1);
            setAdditionalMinutes(minutes - 60);
        } else {
            setTotal(hours);
            setAdditionalMinutes(minutes);
        }
    }, [props.creneaux]);

    return (
        <div>
            <div className={styles.total} style={{ height: `${CalendarConst.headerHeight}px` }}>
                {total}h{additionalMinutes !== 0 ? additionalMinutes : ''}
            </div>
            <div className={styles.rows} style={{ gridTemplateRows: `repeat(${CalendarConst.startHour - CalendarConst.endHour + 1}, 1fr)` }}>{hours}</div>
        </div>
    );
}