import { useState } from 'react'
import CommandPalletBottom from './commandPalletBottom/CommandPalletBottom'
import CommandPalletFilter from './commandPalletFilter/CommandPalletFilter'
import CommandPalletResult from './commandPalletResult/CommandPalletResult'
import CommandPalletTop from './commandPalletTop/CommandPalletTop'
import Modal from '../modal/Modal'
import { Etudiant, Groupes, Personnel, Salle } from '@/app/api/types'

export default function CommandPallet(props: {
    visible: boolean,
    setVisible: Function,
    favorisGroupes: Groupes[],
    setFavorisGroupes: (favoris: Groupes[]) => void,
    favorisSalle: Salle[],
    setFavorisSalle: (favoris: Salle[]) => void,
    favorisEtudiant: Etudiant[],
    setFavorisEtudiant: (favoris: Etudiant[]) => void,
    favorisPersonnel: Personnel[],
    setFavorisPersonnel: (favoris: Personnel[]) => void
}) {
    // Résultat Personnel
    const [resultPersonnel, setResultPersonnel]: [Personnel[], Function] = useState([])
    // Résultat Salle
    const [resultSalle, setResultSalle]: [Salle[], Function] = useState([])
    // Résultat Etudiant
    const [resultEtudiant, setResultEtudiant]: [Etudiant[], Function] = useState([])
    // Résultat Groupe
    const [resultGroupe, setResultGroupe]: [Groupes[], Function] = useState([])

    // Booléen pour chaque filtre
    const [filterByGroupe, setFilterByGroupe] = useState(false)
    const [filterBySalle, setFilterBySalle] = useState(false)
    const [filterByEleve, setFilterByEleve] = useState(false)
    const [filterByPersonnel, setFilterByPersonnel] = useState(false)
    const filterOn = filterByGroupe || filterBySalle || filterByEleve || filterByPersonnel

    return (
        <Modal visible={props.visible} setVisible={props.setVisible}>
            <CommandPalletTop
                visible={props.visible}
                setResultGroupe={setResultGroupe}
                setResultSalle={setResultSalle}
                setResultEtudiant={setResultEtudiant}
                setResultPersonnel={setResultPersonnel}
            />
            <CommandPalletFilter
                filterByGroupe={filterByGroupe}
                setFilterByGroupe={setFilterByGroupe}
                filterBySalle={filterBySalle}
                setFilterBySalle={setFilterBySalle}
                filterByEleve={filterByEleve}
                setFilterByEleve={setFilterByEleve}
                filterByPersonnel={filterByPersonnel}
                setFilterByPersonnel={setFilterByPersonnel}
            />
            <CommandPalletResult
                resultPersonnel={resultPersonnel}
                resultSalle={resultSalle}
                resultEtudiant={resultEtudiant}
                resultGroupe={resultGroupe}
                showGroupe={filterOn ? filterByGroupe : true}
                showSalle={filterOn ? filterBySalle : true}
                showEtudiant={filterOn ? filterByEleve : true}
                showPersonnel={filterOn ? filterByPersonnel : true}
                favorisGroupes={props.favorisGroupes} setFavorisGroupes={props.setFavorisGroupes}
                favorisSalle={props.favorisSalle} setFavorisSalle={props.setFavorisSalle}
                favorisEtudiant={props.favorisEtudiant} setFavorisEtudiant={props.setFavorisEtudiant}
                favorisPersonnel={props.favorisPersonnel} setFavorisPersonnel={props.setFavorisPersonnel}
            />
            <CommandPalletBottom />
        </Modal>
    )
}
