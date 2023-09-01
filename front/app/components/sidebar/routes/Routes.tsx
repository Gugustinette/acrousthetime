import Link from 'next/link'
import styles from './Routes.module.scss'
import Svg from '../../svg/Svg'

/**
 * Routes - Icônes de navigation dans la barre latérale
 * @returns {JSX.Element}
 * @example
 * <Routes />
 */
export default function Routes() {
    return (
        <ul className={styles.routes}>
            <li>
                <Link href="/">
                    <Svg icon="calendar" />
                </Link>
            </li>
            <li>
                <Link href="/reservations">
                    <Svg icon="collection" />
                </Link>
            </li>
        </ul>
    )
}
