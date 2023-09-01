import { useState } from 'react'
import Chips from '../../chips/Chips'
import styles from './CommandPalletFilter.module.scss'

export default function CommandPalletFilter(props: {
    filterByGroupe: boolean,
    setFilterByGroupe: Function,
    filterBySalle: boolean,
    setFilterBySalle: Function,
    filterByEleve: boolean,
    setFilterByEleve: Function,
    filterByPersonnel: boolean,
    setFilterByPersonnel: Function
}) {
    return (
        <div className={styles.command_pallet_filter}>
            <Chips label="Groupes" color="blue" active={props.filterByGroupe} setFilter={props.setFilterByGroupe} />
            <Chips label="Salles" color="orange" active={props.filterBySalle} setFilter={props.setFilterBySalle} />
            <Chips label="Élèves" color="green" active={props.filterByEleve} setFilter={props.setFilterByEleve} />
            <Chips label="Personnel" color="red" active={props.filterByPersonnel} setFilter={props.setFilterByPersonnel} />
        </div>
    )
}
