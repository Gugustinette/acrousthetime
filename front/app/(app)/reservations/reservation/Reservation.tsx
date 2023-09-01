import Button from '@/app/components/button/Button'
import styles from './Reservation.module.scss'
import Svg from '@/app/components/svg/Svg'
import { Reservation, StatusReservation } from '@/app/api/types'

export default function ReservationComponent(props: {
    reservation: Reservation,
    onDelete: (reservationId: string) => void
}) {

    function deleteReserv() {
        if (props.reservation.id) {
            props.onDelete(props.reservation.id.toString());
          }
      }

    function getTimeFormated(date: Date): string {
        return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
    }

    function getDayFormated(date: Date): string {
        const days = ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'];
        const months = ['janvier', 'février', 'mars', 'avril', 'mai', 'juin', 'juillet',
            'août', 'septembre', 'octobre', 'novembre', 'décembre'];
        return `${days[date.getDay()]} ${date.getDate()} ${months[date.getMonth()]}`;
    }

    function getStatus(status: StatusReservation) {
        switch (status) {
            case StatusReservation.ACCEPTED:
                return 'Acceptée';
            case StatusReservation.PENDING:
                return 'En attente';
            case StatusReservation.REFUSED:
                return 'Refusée';
            default:
                return 'Inconnu';
        }
    }

    return (
        <div className={`${styles.reservation}`}>
            <div className={`${styles.vgroup} ${styles.info} ${styles.date}`}>
                <span className={`${styles.bold}`}>{getDayFormated(props.reservation.dt_start)}</span>
                <span className={`${styles.grey}`}>{props.reservation.dt_start.getFullYear()}</span>
            </div>
            <div className={`${styles.hgroup} ${styles.info} ${styles.heure}`}>
                <Svg icon='clock' />
                <span>{getTimeFormated(props.reservation.dt_start)} - {getTimeFormated(props.reservation.dt_end)}</span>
            </div>
            <div className={`${styles.hgroup} ${styles.info} ${styles.salle}`}>
                <Svg icon='door' />
                <span>{props.reservation.salle.name}</span>
            </div>
            <div className={`${styles.hgroup} ${styles.status}`}>
                <span className={`${styles[props.reservation.status.toLocaleLowerCase()] ?? ''}`}>{getStatus(props.reservation.status)}</span>
            </div>
            <div className={`${styles.annuler}`}>
                <Button onClick={() => deleteReserv()} label='Annuler' />
            </div>
        </div>
    )
}
