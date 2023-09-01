"use client"
// React
import { useEffect, useState } from 'react'
// Style
import styles from '../../../page.module.scss'
// Composants
import Semaine from '../../../components/semaine/Semaine'
import Calendar from '../../../components/calendar/Calendar'
import SearchBar from '../../../components/searchbar/SearchBar'
import { useRouter } from 'next/navigation'
import { getToken } from '../../../api/requests/base_request'
import { Creneaux, Etudiant, Groupes, Personnel, Salle } from '../../../api/types'
import { getFavoris } from '../../../api/requests/requests_favoris'
import { getEtudiantById, getEtudiantSemaine } from '@/app/api/requests/requests_etudiant'

export default function AppGroupe({ params }: { params: { id: string } }) {
  // router
  const router = useRouter()

  /**
   * Attributs
   */
  // Creneaux
  const [creneaux, setCreneaux] = useState([] as Creneaux[])
  // selectedDate
  const [selectedDate, setSelectedDate] = useState(new Date())
  // actualEtudiant
  const [actualEtudiant, setActualEtudiant] = useState({} as Etudiant)
  // FavorisGroupe
  const [favorisGroupe, setFavorisGroupe] = useState([] as Groupes[])
  // FavorisSalle
  const [favorisSalle, setFavorisSalle] = useState([] as Salle[])
  // FavorisPersonnel
  const [favorisPersonnel, setFavorisPersonnel] = useState([] as Personnel[])
  // FavorisEtudiant
  const [favorisEtudiant, setFavorisEtudiant] = useState([] as Etudiant[])

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

    // Récupère le groupe
    getEtudiantById(params.id).then((res) => {
        setActualEtudiant(res)
    })
  }, [])

  // Lors d'un update de la date sélectionnée ou du groupe
  useEffect(() => {
    getCreneauxGroupe()
  }, [selectedDate, actualEtudiant])

  // Affiche les créneaux pour le favori sélectionné
  async function getCreneauxGroupe() {
    // Si le groupe n'est pas sélectionné
    if (!actualEtudiant.id) return
    // Récupère les créneaux du groupe
    const creneaux = await getEtudiantSemaine(actualEtudiant.id.toString(), selectedDate)
    setCreneaux(creneaux)
  }

  return (
    <div className={styles.main_layout}>
      <header>
        <div className={styles.header_left}>
          <h2>
            <span>Juin</span>
            <span>2023</span>
            <span>/</span>
            <span>{actualEtudiant.name}</span>
          </h2>
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
