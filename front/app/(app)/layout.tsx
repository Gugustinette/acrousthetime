import styles from '../page.module.scss'
import Sidebar from '../components/sidebar/Sidebar'

export default function RootLayout({
  children
}: { children: React.ReactNode}) {
  const showSidebar = true

  return (
    <main className={styles.main}>
        {showSidebar && <Sidebar />}
        {children}
    </main>
  )
}
