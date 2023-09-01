import Svg from '@/app/components/svg/Svg'
import styles from './ResultSection.module.scss'
import { addEtudiantFavori, addGroupeFavori, addPersonnelFavori, addSalleFavori, deleteGroupeFavori, deleteEtudiantFavori, deletePersonnelFavori, deleteSalleFavori } from '@/app/api/requests/requests_favoris'

/**
 * ResultSection - Display a section of results
 * @param props
 * @param props.results - Array of objects to be displayed in the result section
 * @param props.title - Title to be displayed above the results
 * @param props.icon - Icon to be displayed next to each object
 * @param props.url_path - url_path to be used to create the link
 * @param props.attributes - Corresponding attributes to be displayed for each object
 * @param props.attributes.id - id of the object
 * @param props.attributes.name - name of the object
 * @param props.attributes.type - type of the object
 * @param props.favoris - Array of objects that are in the favorites
 * @param props.setFavoris - Function to set the favorites
 * @example
 * <ResultSection
 *    title="Groupe"
 *    icon="group"
 *    url_path="/groupe"
 *    results={props.resultGroupe}
 *    attributes={{ id: "id", name: "name", type: "type" }}
 * />
 * @returns 
 */
export default function ResultSection<T extends {
    [key: string]: any
}>(props: {
    // Array of objects to be displayed in the result section
    results: T[],
    // Title to be displayed above the results
    title: string,
    // Icon to be displayed next to each object
    icon: string,
    // url_path to be used to create the link
    url_path: string,
    // Corresponding attributes to be displayed for each object
    // Name will be displayed at the left, type at the right
    // url_path and id are used to create the link
    attributes: {
        id: string,
        name: string,
        type: string,
    }
    // favoris
    favoris: T[],
    setFavoris: (favoris: T[]) => void
}) {
    // Function to add an object to the favorites (calling the appropriate API route)
    function addFavoris(result: T) {
        // Check the url_path to call the appropriate API route
        switch (props.url_path) {
            case "/groupe":
                // Call the API route to add the group to the favorites
                addGroupeFavori(result[props.attributes.id])
                break
            case "/salle":
                // Call the API route to add the room to the favorites
                addSalleFavori(result[props.attributes.id])
                break
            case "/etudiant":
                // Call the API route to add the student to the favorites
                addEtudiantFavori(result[props.attributes.id])
                break
            case "/personnel":
                // Call the API route to add the person to the favorites
                addPersonnelFavori(result[props.attributes.id])
                break
        }
    }

    // Function to delete an object from the favorites (calling the appropriate API route)
    function deleteFavoris(result: T) {
        // Check the url_path to call the appropriate API route
        switch (props.url_path) {
            case "/groupe":
                // Call the API route to delete the group from the favorites
                deleteGroupeFavori(result[props.attributes.id])
                break
            case "/salle":
                // Call the API route to delete the room from the favorites
                deleteSalleFavori(result[props.attributes.id])
                break
            case "/etudiant":
                // Call the API route to delete the student from the favorites
                deleteEtudiantFavori(result[props.attributes.id])
                break
            case "/personnel":
                // Call the API route to delete the person from the favorites
                deletePersonnelFavori(result[props.attributes.id])
                break
        }
    }

    // When clicking on the SVG
    function handleFavoriteClick(event: React.MouseEvent<HTMLSpanElement, MouseEvent>, result: T) {
        event.preventDefault()
        // Check if the object is already in the favorites
        const isAlreadyFavorite = props.favoris.find(favori => favori[props.attributes.id] === result[props.attributes.id])
        // If it is, we remove it
        if (isAlreadyFavorite) {
            props.setFavoris(props.favoris.filter(favori => favori[props.attributes.id] !== result[props.attributes.id]))
            deleteFavoris(result)
        } else {
            // If it isn't, we add it
            props.setFavoris([...props.favoris, result])
            addFavoris(result)
        }
    }

    return (
        <div className={styles.result_section}>
            <span>{props.title}</span>
            <ul>
                {props.results.map((result, index) => {
                    return (
                        <li key={index}>
                            <a href={`${props.url_path}/${result[props.attributes.id]}`}>
                                <div className={styles.result_section__result_name}>
                                    <Svg icon={props.icon} />
                                    <span>{result[props.attributes.name]}</span>
                                </div>
                                <div className={styles.result_section__result_type + " " +
                                    // If the object is in the favorites, we add the class "result_favoris"
                                    (props.favoris.find(favori => favori[props.attributes.id] === result[props.attributes.id]) ? styles.result_favoris : "")}>
                                    <span>{result[props.attributes.type]}</span>
                                    <span onClick={(event) => handleFavoriteClick(event, result)}>
                                        <Svg icon="heart" />
                                    </span>
                                </div>
                            </a>
                        </li>
                    )
                })}
            </ul>
        </div>
    )
}
