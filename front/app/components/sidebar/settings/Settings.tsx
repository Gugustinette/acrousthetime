import Link from 'next/link'
import styles from './Settings.module.scss'
import Svg from '../../svg/Svg'

/**
 * Settings - Icône de redirection vers le profile et les paramètres
 * @returns {JSX.Element}
 * @example
 * <Settings />
 */
export default function Settings() {
    return (
        <div className={styles.settings}>
            <Link href="/profile">
                <Svg icon="settings" />
            </Link>
        </div>
    )
}
