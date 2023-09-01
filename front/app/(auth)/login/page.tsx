"use client"
// Link
import Link from 'next/link'
// Style
import Button from '../../components/button/Button'
import Input from '../../components/inputs/Input'
import styles from './page.module.scss'
// API
import { authenticate } from '../../api/requests/requests_authenticate'
import { useRouter } from 'next/navigation'
import { setToken } from '../../api/requests/base_request'
import Image from 'next/image'

export default function Login() {
    // router
    const router = useRouter()

    // Function de connexion
    const submitLogin = async (e: any) => {
        // Evite le rechargement de la page
        e.preventDefault()
        // Récupère les valeurs des inputs
        const email = e.target.email.value
        const password = e.target.password.value
        // Tente de se connecter
        try {
            const res = await authenticate(email, password)
            // Si la connexion est réussi
            if (res) {
                // On enregistre le token
                setToken(res.token)
                // On redirige vers la page d'accueil
                router.push('/')
            }
        } catch (err) {
            // Si la connexion est échoué
            alert('Erreur de connexion')
            console.log(err)
        }
    }

    return (
        <div className={styles.login_wrapper}>
            <Image src="/logo.png" alt="Logo" width={100} height={100} style={{
                position: 'absolute',
                top: '50px',
                left: '50%',
                transform: 'translateX(-50%)'
            }} />
            <form className={styles.login_form} onSubmit={submitLogin}>
                <header>
                    <h1>Connexion</h1>
                </header>
                <div className={styles.login_form_inputs}>
                    <Input type="text" name="email" placeholder="Adresse email" minWidth={true} />
                    <Input type="password" name="password" placeholder="Mot de passe" />
                </div>
                <footer>
                    <Button type="submit" label="Se connecter" color="blue" />
                    <div className={styles.login_switch_wrapper}>
                        <p>Pas encore de compte ?</p>
                        <Link href="/register">Crée le ici</Link>
                    </div>
                </footer>
            </form>
        </div>
    )
}
