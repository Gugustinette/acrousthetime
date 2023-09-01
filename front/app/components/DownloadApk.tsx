// Style Button
import styles from "./button/Button.module.scss"

// Download APK Button
export default function DownloadApk() {
  return (
    <a href="/gusmetal.apk" download className={`${styles.button} ${styles.blue}`}>
        Télécharger APK
    </a>
  )
}
