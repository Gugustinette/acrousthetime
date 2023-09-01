import styles from './Button.module.scss'
import Svg from '../svg/Svg'

export default function Button(props: {
    label: string,
    color?: string,
    icon?: string,
    type?: 'button' | 'submit' | 'reset',
    onClick?: Function
}) {

    return (
        <button onClick={() => props.onClick?.()} type={props.type ?? 'button'} className={`${styles.button} ${styles[props.color ?? '']}`}>
            {props.icon && (
                <Svg icon={`${props.icon}`} />
            )}
            <span>{props.label}</span>
        </button>
    )
}
