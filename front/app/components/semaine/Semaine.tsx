import { useEffect, useState } from 'react'
import Svg from '../svg/Svg'
import styles from './Semaine.module.scss'

/**
 * Semaine - Affiche les semaines de l'année
 * @param props
 * @param props.semaine - Semaine actuelle
 * @param props.setSemaine - Fonction pour changer la semaine actuelle
 * @returns {JSX.Element}
 * @example
 * const [semaine, setSemaine] = useState(0)
 * <Semaine semaine={semaine} setSemaine={setSemaine} />
 */
export default function Semaine(props: {
    selectedDate: Date,
    setSelectedDate: (semaine: Date) => void
}) {
    // Numéro de la semaine
    const [semaineNumber, setSemaineNumber] = useState(0)

    useEffect(() => {
        // Calcul de la semaine actuelle
        const firstDayOfYear = new Date(props.selectedDate.getFullYear(), 0, 1)
        const pastDaysOfYear = (props.selectedDate.getTime() - firstDayOfYear.getTime()) / 86400000
        const week = Math.ceil((pastDaysOfYear + firstDayOfYear.getDay() + 1) / 7)
        setSemaineNumber(week)

        // Centre la semaine actuelle
        const semaineContent = document.querySelector("." + styles.semaine_content)
        if (semaineContent) {
            // Là on est pas sûr de la formule hein
            semaineContent.scrollLeft =
                // 35 = largeur d'une semaine
                (week - 1) * 35
                // On ajoute la moitié de la largeur du conteneur
                - semaineContent.clientWidth / 2
                // On enlève une semaine pour centrer la semaine actuelle
                + 35
                // On enlève la moitié de la largeur d'une semaine pour centrer la semaine actuelle
                + 35 / 2
        }
    }, [])

    return (
        <div className={styles.semaine}>
            <span onClick={() => {
                // Calcul de la date de la semaine précédente
                const date = new Date(props.selectedDate)
                date.setDate(date.getDate() - 7)
                props.setSelectedDate(date)
                setSemaineNumber(semaineNumber - 1)
            }}>
                <Svg icon="arrow" />
            </span>
            <div className={styles.semaine_content}>
                {
                    Array.from(Array(52).keys()).map((week) => (
                        <div 
                            className={
                                styles.semaine_object +
                                (week === semaineNumber ? ' ' + styles.semaine_object__active : '') +
                                " semaine-" + week
                            }
                            key={week}
                            onClick={() => {
                                // Calcul de la date de la semaine (date sélectionnée + la différence de semaine)
                                const date = new Date(props.selectedDate)
                                date.setDate(date.getDate() + (week - semaineNumber) * 7)
                                props.setSelectedDate(date)
                                setSemaineNumber(week)
                            }}
                        >
                            {week + 1}
                        </div>
                    ))
                }
            </div>
            <span onClick={() => {
                // Calcul de la date de la semaine suivante
                const date = new Date(props.selectedDate)
                date.setDate(date.getDate() + 7)
                props.setSelectedDate(date)
                setSemaineNumber(semaineNumber + 1)
            }}>
                <Svg icon="arrow" />
            </span>
        </div>
    )
}
