import styles from './Select.module.scss'

export default function Select(props: {
    disabled?: boolean,
    name: string,
    color?: string,
    optionLabel: string,
    optionValue: string,
    optionType?: string,
    options: Array<any>,
    minWidth?: boolean,
    onChange?: (e: any) => void
}) {
    return (
        <select name={props.name}
            disabled={props.disabled}
            className={`${styles.input} ${styles[props.color ?? '']}`}
            style={{minWidth: props.minWidth ? 'var(--inputs-min-width)' : 'none'}}
            onChange={props.onChange}
        >
            {props.options.map((element, index) => {
                return (
                    <option key={index} value={element[`${props.optionValue}`] + '@@@' + (props.optionType ? element[`${props.optionType}`] : '')}>
                        {element[`${props.optionLabel}`]}
                    </option>
                )
            })}
        </select>
    )
}
