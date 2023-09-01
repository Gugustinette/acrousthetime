import { useEffect } from 'react'
import Svg from '../../svg/Svg'
import styles from './CommandPalletTop.module.scss'
import { getSearchResults } from '@/app/api/requests/requests_search'

export default function CommandPalletTop(props: {
    visible: boolean,
    setResultGroupe: Function,
    setResultSalle: Function,
    setResultEtudiant: Function,
    setResultPersonnel: Function,
}) {
    useEffect(() => {
        // Requête de recherche (vide)
        getSearchResults("").then(results => {
            props.setResultGroupe(results.groupe)
            props.setResultSalle(results.salle)
            props.setResultEtudiant(results.etudiant)
            props.setResultPersonnel(results.personnel)
        })
    }, [])

    // When visible change, focus on input
    useEffect(() => {
        if (props.visible) {
            const input = document.querySelector(`.${styles.command_pallet_top} input`) as HTMLInputElement
            input?.focus()
        }
    }, [props.visible])

    // Fonction de recherche lors de la saisie
    async function handleChange(event: any) {
        // Récupère la valeur de la saisie
        const value = event.target.value

        // Si la valeur est vide ou a moins de 2 caractères, on vide les résultats
        if (value === '' || value.length < 2) {
            // Requête de recherche (vide)
            const results = await getSearchResults("")
            props.setResultGroupe(results.groupe)
            props.setResultSalle(results.salle)
            props.setResultEtudiant(results.etudiant)
            props.setResultPersonnel(results.personnel)
        }
        // Sinon on fait la recherche
        else {
            // Requête de recherche
            const results = await getSearchResults(value)
            props.setResultGroupe(results.groupe)
            props.setResultSalle(results.salle)
            props.setResultEtudiant(results.etudiant)
            props.setResultPersonnel(results.personnel)
        }
    }

    return (
        <div className={styles.command_pallet_top}>
            <Svg icon="loop" />
            <input type="text" placeholder="Search" onChange={handleChange} />
        </div>
    )
}
