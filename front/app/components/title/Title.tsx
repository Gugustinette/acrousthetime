import styles from './Title.module.scss'

export default function Title(props: {
    title: string,
}) {
    return (
        <div className={`${styles.title}`}>
            <h1>{props.title}</h1>
        </div>
    )
}
