import { useEffect, useState } from 'react';
import CalendarConst from '../CalendarConst';
import styles from './CalenderHeader.module.scss'

export default function CalendarHeader(props: {
    selectedDate: Date
}) {
    // Int to compute which should be highlighted (and 0 if none)
    const [highlighted, setHighlighted] = useState(0);

    useEffect(() => {
        const today = new Date()
        // If the week is the current week, highlight the current day
        if (
            props.selectedDate.getFullYear() === today.getFullYear()
            && props.selectedDate.getMonth() === today.getMonth()
            && props.selectedDate.getDate() === today.getDate()
        ) {
            setHighlighted(today.getDay() - 1);
        } else {
            setHighlighted(-1);
        }
    }, [props.selectedDate]);

    return (
        <div className={styles.days} style={{height: `${CalendarConst.headerHeight}px`}}>
            <span className={highlighted === 0 ? styles.selected : ''}>Lundi</span>
            <span className={highlighted === 1 ? styles.selected : ''}>Mardi</span>
            <span className={highlighted === 2 ? styles.selected : ''}>Mercredi</span>
            <span className={highlighted === 3 ? styles.selected : ''}>Jeudi</span>
            <span className={highlighted === 4 ? styles.selected : ''}>Vendredi</span>
        </div>
    )
}
