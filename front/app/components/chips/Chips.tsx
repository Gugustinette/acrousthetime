import styles from './Chips.module.scss'

/**
 * Chips - Composant d'état, notamment pour les filtres
 * @param props
 * @param props.label - Label du chips
 * @param props.color - Couleur du chips
 * @param props.active - Etat du chips
 * @param props.setFilter - Fonction pour changer l'état du chips
 * @returns {JSX.Element}
 * @example
 * const [active, setActive] = useState(false)
 * <Chips label="Favoris" color="blue" active={active} setFilter={setActive} />
 */
export default function Chips(props: {
    label: string,
    color?: string,
    active: boolean,
    setFilter: Function
}) {
    return (
        <button type="button" className={`${styles.chips} ${styles[
            (props.color && props.active) ? props.color : ''
        ]}`} onClick={() => props.setFilter(!props.active)}>
            <span>{props.label}</span>
        </button>
    )
}
