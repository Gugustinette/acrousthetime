import { Etudiant, Groupes, Personnel, Salle } from '@/app/api/types'
import styles from './CommandPalletResult.module.scss'
import ResultSection from './resultSection/ResultSection'

export default function CommandPalletResult(props: {
    // Result
    resultGroupe: Groupes[],
    resultSalle: Salle[],
    resultEtudiant: Etudiant[],
    resultPersonnel: Personnel[],
    // Filter
    showGroupe: boolean,
    showSalle: boolean,
    showEtudiant: boolean,
    showPersonnel: boolean,
    // Favoris
    favorisGroupes: Groupes[],
    setFavorisGroupes: (favoris: Groupes[]) => void,
    favorisSalle: Salle[],
    setFavorisSalle: (favoris: Salle[]) => void,
    favorisEtudiant: Etudiant[],
    setFavorisEtudiant: (favoris: Etudiant[]) => void,
    favorisPersonnel: Personnel[],
    setFavorisPersonnel: (favoris: Personnel[]) => void
}) {
    return (
        <div className={styles.command_pallet_result}>
            {
                props.showGroupe &&
                <ResultSection
                    title="Groupe"
                    icon="group"
                    url_path="/groupe"
                    results={props.resultGroupe}
                    attributes={{ id: "id", name: "name", type: "type" }}
                    favoris={props.favorisGroupes}
                    setFavoris={props.setFavorisGroupes}
                />
            }
            {
                props.showSalle &&
                <ResultSection
                    title="Salles"
                    icon="door"
                    url_path="/salle"
                    results={props.resultSalle}
                    attributes={{ id: "id", name: "name", type: "type" }}
                    favoris={props.favorisSalle}
                    setFavoris={props.setFavorisSalle}
                />
            }
            {
                props.showEtudiant &&
                <ResultSection
                    title="Etudiant"
                    icon="student"
                    url_path="/etudiant"
                    results={props.resultEtudiant}
                    attributes={{ id: "id", name: "name", type: "type" }}
                    favoris={props.favorisEtudiant}
                    setFavoris={props.setFavorisEtudiant}
                />
            }
            {
                props.showPersonnel &&
                <ResultSection
                    title="Personnel"
                    icon="person"
                    url_path="/personnel"
                    results={props.resultPersonnel}
                    attributes={{ id: "id", name: "name", type: "type" }}
                    favoris={props.favorisPersonnel}
                    setFavoris={props.setFavorisPersonnel}
                />
            }
        </div>
    )
}
