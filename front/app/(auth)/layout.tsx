export const metadata = {
  title: 'ACrousTheTime',
  description: 'Bienvenue dans ACrousTheTime',
}

import styles from '../page.module.scss'

import { AppProps } from 'next/app'

export default function RootLayout({
  children
}: { children: React.ReactNode}) {
    return (
    <main className={styles.main}>
        {children}
    </main>
    )
}
