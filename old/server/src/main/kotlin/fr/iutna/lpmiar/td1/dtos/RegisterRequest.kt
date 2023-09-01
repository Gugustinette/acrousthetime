package fr.iutna.lpmiar.td1.dtos

/**
 * Class DTO for the register request.
 */
class RegisterRequest {
    var nom: String? = null
    var prenom: String? = null
    var email: String? = null
    var password: String? = null

    constructor(nom: String?, prenom: String?, email: String?, password: String?) {
        this.nom = nom
        this.prenom = prenom
        this.email = email
        this.password = password
    }
}
