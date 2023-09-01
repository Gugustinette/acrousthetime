import { useState } from 'react';
import styles from './Input.module.scss';
import Svg from '../svg/Svg';

export default function Input(props: {
  type?: string,
  name: string,
  color?: string,
  placeholder?: string,
  defaultValue?: string,
  disabled?: boolean,
  min?: string | number,
  max?: string | number;
  minWidth?: boolean,
  onChange?: (e: any) => void
}) {
  const [showPassword, setShowPassword] = useState(false);

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const inputType = props.type === 'password' && showPassword ? 'text' : props.type;

  return (
    <div className={`${styles.inputContainer}`} style={{minWidth: props.minWidth ? 'var(--inputs-min-width)' : 'none'}}>
      <input
        type={inputType ?? 'text'}
        name={props.name}
        disabled={props.disabled}
        defaultValue={props.defaultValue}
        min={`${[props.min ?? '']}`}
        max={`${[props.max ?? '']}`}
        className={`${styles.input} ${styles[props.color ?? '']}`}
        placeholder={props.placeholder}
        onChange={props.onChange}
      />
      {props.type === 'password' && (
        <div className={`${styles.passwordIcon} ${styles[props.color ?? '']}`} onClick={togglePasswordVisibility}>
          {showPassword ? (
            <Svg icon="eye-crossed" />
          ) : (
            <Svg icon="eye" />
          )}
        </div>
      )}
    </div>
  );
}
