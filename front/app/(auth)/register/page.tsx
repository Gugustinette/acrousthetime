"use client"
// Link
import Link from 'next/link'
// Style
import Button from '../../components/button/Button'
import Input from '../../components/inputs/Input'
import styles from './page.module.scss'
import { useRouter } from 'next/navigation'
import { register } from '@/app/api/requests/requests_authenticate'
import { setToken } from '@/app/api/requests/base_request'
import Image from 'next/image'

export default function Register() {
    // router
    const router = useRouter()

    // Function d'inscription
    const submitRegister = async (e: any) => {
        // Evite le rechargement de la page
        e.preventDefault()
        // Récupère les valeurs des inputs
        const nom = e.target.nom.value
        const prenom = e.target.prenom.value
        const email = e.target.email.value
        const password = e.target.password.value
        // Tente de s'inscrire
        try {
            const res = await register(nom, prenom, email, password)
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
        <div className={styles.register_wrapper}>
            <form className={styles.register_form} onSubmit={submitRegister}>
                <Image src="/logo.png" alt="Logo" width={100} height={100} style={{
                    position: 'absolute',
                    top: '50px',
                    left: '50%',
                    transform: 'translateX(-50%)'
                }} />
                <header>
                    <h1>Inscription</h1>
                </header>
                <div className={styles.register_form_inputs}>
                    <div className={styles.register_form_inputs_horizontal}>
                        <Input type="text" name="nom" placeholder="Nom" />
                        <Input type="text" name="prenom" placeholder="Prenom" />
                    </div>
                    <Input type="text" name="email" placeholder="Adresse email" />
                    <Input type="password" name="password" placeholder="Mot de passe" />
                    <Input type="password" name="password-confirmation" placeholder="Confirmation du mot de passe" />
                </div>
                <footer>
                    <Button type="submit" label="S'inscrire" color="blue" />
                    <div className={styles.register_switch_wrapper}>
                        <p>Déjà un compte ?</p>
                        <Link href="/login">Connecte toi</Link>
                    </div>
                </footer>
            </form>
        </div>
    )
}
