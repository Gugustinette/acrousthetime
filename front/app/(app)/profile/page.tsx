"use client"

import Title from "@/app/components/title/Title"
import Subtitle from "@/app/components/subtitle/Subtitle"
import Button from "@/app/components/button/Button"
import Input from "@/app/components/inputs/Input"
import styles from "./Profile.module.scss"
import DownloadApk from "@/app/components/DownloadApk"

import { getImage, postImage } from "@/app/api/requests/requests_image"
import { useState } from "react"

export default function Profile() {
    const img = "https://www.woolha.com/media/2020/03/eevee.png"
    const [form, setForm] = useState({
        nom: "",
        prenom: ""
    })
    const onChange = (target : string, e : any) => {
        setForm({...form, [target]: e.target.value})
    }

    const onSubmit = () => {
        console.log(form)
    }

    return (
        <div className={`${styles.main}`}>
            <div>
                <Title title="Mon profil"/>
                <Subtitle subtitle="Gérer les paramètres de votre profil"/>
            </div>
            <div className={`${styles.profile_content}`}>
                <div>
                    <h4 className="labelInput">Photo de profil</h4>
                    <div className={`${styles.profile_img_content}`}>
                        <div>
                            <svg className={`${styles.profile_img}`} width="100" height="100" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
                            </svg>
                        </div>
                        <div className={`${styles.profile_img_buttons}`}>
                            <Button label="Modifier la photo" icon="camera" onClick={getImage}></Button>
                            <Button label="Supprimer la photo" color="red" icon="trash" onClick={() => postImage(img)}></Button>
                        </div>
                    </div>
                </div>
                <div>
                    <h4 className="labelInput">Nom</h4>
                    <Input name="nom" type="text" color="blue" onChange={(e) => onChange("nom", e) }></Input>
                </div>
                <div>
                    <h4 className="labelInput">Prénom</h4>
                    <Input name="prenom" type="text" color="blue" onChange={(e) => onChange("prenom", e)}></Input>
                </div>
                <div>
                    <Button type="submit" label="Valider les changements" onClick={onSubmit}></Button>
                </div>
                <div>
                    <DownloadApk ></DownloadApk>
                </div>
            </div>
        </div>
    )
}
