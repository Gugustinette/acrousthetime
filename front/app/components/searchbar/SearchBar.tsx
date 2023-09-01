import { useEffect, useState } from 'react'
import styles from './SearchBar.module.scss'

// Import command pallet component
import CommandPallet from './CommandPallet'
import Svg from '../svg/Svg'
import { Etudiant, Groupes, Personnel, Salle } from '@/app/api/types'

/**
 * SearchBar - Affiche le bouton pour ouvrir la command pallet.
 * La command pallet est intégré au bouton
 * @returns {JSX.Element}
 * @example
 * <SearchBar />
 */
export default function SearchBar(props: {
    favorisGroupes: Groupes[],
    setFavorisGroupes: (favoris: Groupes[]) => void,
    favorisSalle: Salle[],
    setFavorisSalle: (favoris: Salle[]) => void,
    favorisEtudiant: Etudiant[],
    setFavorisEtudiant: (favoris: Etudiant[]) => void,
    favorisPersonnel: Personnel[],
    setFavorisPersonnel: (favoris: Personnel[]) => void
}) {
    // Visible state
    const [visible, setVisible] = useState(false)

    // Listen to Command+K for focus on search bar
    useEffect(() => {
        const handleKeyDown = (event: KeyboardEvent) => {
            if (event.metaKey && event.key === 'k') {
                event.preventDefault()
                setVisible(true)
            }
        }
        window.addEventListener('keydown', handleKeyDown)
        return () => {
            window.removeEventListener('keydown', handleKeyDown)
        }
    }, [])

    return (
        <div className={styles.search_bar_button}>
            <button onClick={() => setVisible(!visible)}>
                <Svg icon="loop" />
                <span>Rechercher</span>
            </button>
            <CommandPallet
                visible={visible} setVisible={setVisible}
                favorisGroupes={props.favorisGroupes} setFavorisGroupes={props.setFavorisGroupes}
                favorisSalle={props.favorisSalle} setFavorisSalle={props.setFavorisSalle}
                favorisEtudiant={props.favorisEtudiant} setFavorisEtudiant={props.setFavorisEtudiant}
                favorisPersonnel={props.favorisPersonnel} setFavorisPersonnel={props.setFavorisPersonnel}
            />
        </div>
    )
}
