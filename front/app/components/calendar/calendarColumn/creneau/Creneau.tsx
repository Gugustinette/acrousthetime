import { Creneaux } from '@/app/api/types';
import styles from './Creneau.module.scss'
import CalendarConst from '../../CalendarConst';
import Modal from '@/app/components/modal/Modal';
import { useState } from 'react';
import CreneauInfos from './CreneauInfos';

export default function Creneau(props: {
    creneau: Creneaux,
    styleOverLapping?: React.CSSProperties
}) {
    const [visible, setVisible] = useState(false)
    let top = (getTime(props.creneau.dt_start) - CalendarConst.startHour) * CalendarConst.creneauHeight + 2;
    let height = (getTime(props.creneau.dt_end) - getTime(props.creneau.dt_start)) * CalendarConst.creneauHeight - 2;
    let theme = '';

    function getTime(date: Date): number {
        return date.getHours() + date.getMinutes() / 60;
    }

    function getTimeFormated(date: Date): string {
        return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
    }

    function getAllPersonnels(): string {
        let personels: String[] = [];
        props.creneau.personnel.forEach(personel => {
            personels.push(personel.name);
        });
        return personels.join(', ');
    }

    function getAllSalles(): string {
        let salles: String[] = [];
        props.creneau.salle.forEach(salle => {
            salles.push(salle.name);
        });
        return salles.join(', ');
    }

    switch (props.creneau.summary.substring(0, 2)) {
        case 'TD':
            theme = 'blue';
            break;
        case 'TP':
            theme = 'orange';
            break;
        case 'CM':
            theme = 'green';
            break;
        default:
            theme = 'red';
            break;
    }


    return (
        <div>
            <div className={`${styles.creneau}`} style={{ top: `${top}px`, height: `${height}px`, ...props.styleOverLapping }} onClick={() => setVisible(true)} >
                <div className={`${styles.creneauTest} ${styles[theme ?? 'red']}`}>
                    <span className={styles.cours}>{props.creneau.summary.split("\\")[0]}</span>
                    <span className={styles.prof}>{getAllPersonnels()}</span>
                    <span className={styles.horaire}>({getTimeFormated(props.creneau.dt_start)} - {getTimeFormated(props.creneau.dt_end)})</span>
                    <span className={styles.salle}>{getAllSalles()}</span>
                </div>
            </div>
            <Modal visible={visible} setVisible={setVisible}>
                <CreneauInfos creneau={props.creneau} />
            </Modal>
        </div>
    )
}
