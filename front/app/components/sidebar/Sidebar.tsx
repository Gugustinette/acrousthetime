import Image from 'next/image'
import styles from './Sidebar.module.scss'
import ProfilePicture from './profilepicture/ProfilePicture'
import Routes from './routes/Routes'
import Settings from './settings/Settings'

/**
 * Sidebar - Barre de navigation latérale
 * @returns {JSX.Element}
 * @example
 * <Sidebar />
 */
export default function Sidebar() {
    return (
        <nav className={styles.sidebar}>
            <div className={styles.sidebar_top}>
                <Image src="/logo.png" alt="Logo" width={50} height={50} />
                <Routes />
            </div>
            <div className={styles.sidebar_bottom}>
                <Settings />
            </div>
        </nav>
    )
}
