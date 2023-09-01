import { Creneaux } from '@/app/api/types';
import styles from './CreneauInfos.module.scss'
import Title from '@/app/components/title/Title';
import Svg from '@/app/components/svg/Svg';
import Link from 'next/link';

export default function CreneauInfos(props: {
    creneau: Creneaux,
}) {
    function getTimeFormated(date: Date): string {
        return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
    }

    return (
        <div className={`${styles.infos} ${styles.vgroup}`}>
            <Title title={props.creneau.summary.split("\\")[0]} />

            <div className={`${styles.hgroup}`}>
                <Svg icon='person' />
                {props.creneau.personnel.map((personnel, personnelIndex) => (
                    <Link key={personnelIndex} href={`/personnel/${personnel.id}`}>{personnel.name}</Link>
                ))}
            </div>

            <div className={`${styles.hgroup}`}>
                <Svg icon='clock' />
                <span>{getTimeFormated(props.creneau.dt_start)} - {getTimeFormated(props.creneau.dt_end)}</span>
            </div>

            <div className={`${styles.hgroup}`}>
                <Svg icon='door' />
                {props.creneau.salle.map((salle, salleIndex) => (

                    <Link key={salleIndex} href={`/salle/${salle.id}`}>{salle.name}</Link>
                ))}
            </div>

            <div className={`${styles.hgroup}`}>
                <Svg icon='group' />
                {props.creneau.groupe.map((groupe, groupeIndex) => (
                    <Link key={groupeIndex} href={`/groupe/${groupe.id}`}>{groupe.name}</Link>
                ))}
            </div>
        </div>
    )
}
