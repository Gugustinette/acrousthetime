import { useState } from 'react'
import styles from './Modal.module.scss'

/**
 * Modal - Composant de fenêtre modale
 * @returns {JSX.Element}
 * @example
 * "use client"
 * import { useState } from 'react'
 * import Modal from '../modal/Modal'
 * 
 * export default function TestModal() {
 *      const [visible, setVisible] = useState(false)
 * 
 *      return (
 *          <div>
 *              <button type="button" onClick={() => setVisible(true)}>Open Modal</button>
 *              <Modal visible={visible} setVisible={setVisible}>
 *                  <h1>Hello Modal</h1>
 *              </Modal>
 *          </div>
 *      )
 * }
 */
export default function Modal(props: { visible: boolean, setVisible: Function, children: any }) {
    // Out State
    const [out, setOut] = useState(false)

    // Détecte le clique à l'extérieur de la fenêtre
    function handleClickOutside(event: any) {
        // Si className est un SVGAnimatedString, on ne fait rien
        if (event.target.className instanceof SVGAnimatedString) {
            return
        }
        // Si le clique est en dehors de la fenêtre, on ferme la fenêtre
        if (
            event.target.className.split(' ').indexOf(styles.modal_background) !== -1
        ) {
            setOut(true)
            // Après 300ms (le temps de l'animation) on ferme la fenêtre
            setTimeout(() => {
                props.setVisible(false)
            }, 0)
        }
    }

    return (
        <dialog className={styles.modal_wrapper +
            (out ? ' ' + styles.modal_wrapper_out : '')
        } open={props.visible}>
            <div className={styles.modal_background} onClick={handleClickOutside}>
                <div className={styles.modal}>
                    {props.children}
                </div>
            </div>
        </dialog>
    )
}
