import styles from './Subtitle.module.scss'

export default function Subtitle(props: {
    subtitle: string,
}) {
    return (
        <div className={`${styles.subtitle}`}>
            <h2>{props.subtitle}</h2>
        </div>
    )
}