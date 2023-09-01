'use client';
import { useEffect, useState } from 'react';
import styles from './Calendar.module.scss'
import CalendarColumn from './calendarColumn/CalendarColumn'
import CalendarHeader from './calendarHeader/CalendarHeader'
import CalendarHours from './calendarHours/CalendarHours'
import CalendarConst from './CalendarConst';
import { Creneaux } from '@/app/api/types';

export default function Calendar(props: {
    creneaux: Creneaux[],
    selectedDate: Date
}) {
    let [top, setTop] = useState(0);
    // Show cursor
    const [showCursor, setShowCursor] = useState(false);
    // Splitted creneaux
    const [splittedCreneaux, setSplittedCreneaux] = useState([] as Creneaux[][]);

    top = (getTime(new Date) - CalendarConst.startHour) * CalendarConst.creneauHeight + 2 + CalendarConst.headerHeight;

    useEffect(() => {
        // Update the cursor every 10 seconds
        updateCursor();

        const interval = setInterval(updateCursor, 10000);
        return () => {
            clearInterval(interval);
        };
    }, []);

    useEffect(() => {
        // Split the creneaux by day
        const splittedCreneauxTemp = [...Array(5)].map(() => [] as Creneaux[]);
        props.creneaux.forEach(creneau => {
            const day = new Date(creneau.dt_start).getDay() - 1;
            splittedCreneauxTemp[day]?.push(creneau);
        });
        setSplittedCreneaux(splittedCreneauxTemp);
    }, [props.creneaux]);

    function updateCursor(): void {
        const hour = new Date();
        const time = getTime(hour);
        const newTop = (time - CalendarConst.startHour) * CalendarConst.creneauHeight + 2 + CalendarConst.headerHeight;

        setTop(newTop);

        const currentHour = hour.getHours();
        const shouldShowCursor = currentHour >= CalendarConst.startHour && currentHour <= CalendarConst.endHour;
        
        setShowCursor(shouldShowCursor);
    }

    function getTime(date: Date): number {
        return date.getHours() + date.getMinutes() / 60;
    }

    return (
        <div className={styles.row}>
            <CalendarHours creneaux={props.creneaux} />
            <div className={styles.column}>
                <CalendarHeader selectedDate={props.selectedDate} />
                {showCursor && (
                    <div className={styles.cursor} style={{ top: `${top}px` }}>
                        <div className={styles.circle}></div>
                    </div>
                )}
                <div className={styles.jours}>
                    {splittedCreneaux.map((creneaux, index) => (
                        <CalendarColumn key={index} creneaux={creneaux} />
                    ))}
                </div>
            </div>
        </div>
    )
}
