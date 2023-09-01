"use client"
// React
import { useEffect, useState } from 'react'
// Style
import styles from '../page.module.scss'
// Composants
import Select from '../components/inputs/select/Select'
import Semaine from '../components/semaine/Semaine'
import Calendar from '../components/calendar/Calendar'
import SearchBar from '../components/searchbar/SearchBar'
import { useRouter } from 'next/navigation'
import { getToken } from '../api/requests/base_request'
import { Creneaux, Etudiant, Groupes, Personnel, Salle } from '../api/types'
import { getFavoris } from '../api/requests/requests_favoris'
import { getGroupeSemaine } from '../api/requests/requests_groupes'
import { getSallesSemaine } from '../api/requests/requests_salles'
import { getPersonnelSemaine } from '../api/requests/requests_personnel'
import { getEtudiantSemaine } from '../api/requests/requests_etudiant'

export default function AppHome() {
  // router
  const router = useRouter()
  // Booléen permettent de savoir si un premier chargement a été effectué
  const [firstLoad, setFirstLoad] = useState(true)

  /**
   * Attributs
   */
  // Creneaux
  const [creneaux, setCreneaux] = useState([] as Creneaux[])
  // selectedDate
  const [selectedDate, setSelectedDate] = useState(new Date())
  // FavorisGroupe
  const [favorisGroupe, setFavorisGroupe] = useState([] as Groupes[])
  // FavorisSalle
  const [favorisSalle, setFavorisSalle] = useState([] as Salle[])
  // FavorisPersonnel
  const [favorisPersonnel, setFavorisPersonnel] = useState([] as Personnel[])
  // FavorisEtudiant
  const [favorisEtudiant, setFavorisEtudiant] = useState([] as Etudiant[])
  // Favoris
  const [favoris, setFavoris] = useState([] as { value: string, label: string, type: string }[])
  // SelectedFavoris
  const [selectedFavoris, setSelectedFavoris] = useState({} as { id: string, type: string })

  // Lors du chargement de la page
  useEffect(() => {
    // Vérifie que l'utilisateur est connecté
    try {
      getToken()
    }
    // Il n'est pas connecté
    catch (error) {
      // On le redirige vers la page de connexion
      router.push('/login')
    }

    // Récupère les favoris
    getFavoris().then((res) => {
      // Modification des favoris
      setFavorisGroupe(res.groupe)
      setFavorisSalle(res.salle)
      setFavorisPersonnel(res.personnel)
      setFavorisEtudiant(res.etudiant)
    })
  }, [])

  // Lors d'un update des favoris de groupe, salle, personnel ou étudiant
  useEffect(() => {
    // Fusionne les favoris dans un seul tableau dont les objets sont au format {value: id, label: nom, type: type}
    setFavoris([
      ...favorisGroupe.map((groupe) => ({ value: groupe.id.toString(), label: groupe.name.toString(), type: 'groupe' })),
      ...favorisSalle.map((salle) => ({ value: salle.id.toString(), label: salle.name.toString(), type: 'salle' })),
      ...favorisPersonnel.map((personnel) => ({ value: personnel.id.toString(), label: personnel.name.toString(), type: 'personnel' })),
      ...favorisEtudiant.map((etudiant) => ({ value: etudiant.id.toString(), label: etudiant.name.toString(), type: 'etudiant' }))
    ])
  }, [favorisGroupe, favorisSalle, favorisPersonnel, favorisEtudiant])

  // Lors d'un update des favoris
  useEffect(() => {
    // Si c'est le premier chargement
    if (firstLoad) {
      // Si il y a des favoris
      if (favoris.length > 0) {
        // Récupère l'identifiant et le type du premier favori par défaut dans le Select
        const favori = favoris[0].value
        const type = favoris[0].type
        setSelectedFavoris({ id: favori, type: type })
        setFirstLoad(false)
      }
    }
  }, [favoris])

  // Lors d'un update de la date sélectionnée
  useEffect(() => {
    getCreneauxFavori()
  }, [selectedDate])

  // Lors d'un update du favori sélectionné
  useEffect(() => {
    getCreneauxFavori()
  }, [selectedFavoris])

  // Affiche les créneaux pour le favori sélectionné
  async function getCreneauxFavori() {
    // Récupère les créneaux du favori
    if (selectedFavoris.type === 'salle') {
      const creneaux = await getSallesSemaine(selectedFavoris.id, selectedDate)
      setCreneaux(creneaux)
    } else if (selectedFavoris.type === 'groupe') {
      const creneaux = await getGroupeSemaine(selectedFavoris.id, selectedDate)
      setCreneaux(creneaux)
    } else if (selectedFavoris.type === 'personnel') {
      const creneaux = await getPersonnelSemaine(selectedFavoris.id, selectedDate)
      setCreneaux(creneaux)
    } else if (selectedFavoris.type === 'etudiant') {
      const creneaux = await getEtudiantSemaine(selectedFavoris.id, selectedDate)
      setCreneaux(creneaux)
    }
  }

  // Quand le favori sélectionné change
  function handleFavoriChange(e: any) {
    // Récupère le favori sélectionné
    const favori = e.target.value.split('@@@')[0]
    // Récupère le type du favori sélectionné
    const type = e.target.value.split('@@@')[1]
    setSelectedFavoris({ id: favori, type: type })
  }

  return (
    <div className={styles.main_layout}>
      <header>
        <div className={styles.header_left}>
          <h2>
            <span>Juin</span>
            <span>2023</span>
          </h2>
          <Select name="select" options={favoris} optionValue="value" optionLabel="label" optionType="type" onChange={handleFavoriChange} />
        </div>
        <Semaine selectedDate={selectedDate} setSelectedDate={setSelectedDate} />
        <SearchBar
          favorisGroupes={favorisGroupe}
          setFavorisGroupes={setFavorisGroupe}
          favorisSalle={favorisSalle}
          setFavorisSalle={setFavorisSalle}
          favorisPersonnel={favorisPersonnel}
          setFavorisPersonnel={setFavorisPersonnel}
          favorisEtudiant={favorisEtudiant}
          setFavorisEtudiant={setFavorisEtudiant}
        />
      </header>
      <Calendar creneaux={creneaux} selectedDate={selectedDate} />
    </div>
  )
}
