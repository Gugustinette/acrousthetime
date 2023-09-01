package fr.iutna.lpmiar.td1.dtos

/**
 * Class DTO for the authentication request.
 */
class AuthenticationRequest() {
    var email: String = ""
    var password: String = ""

    constructor(email: String, password: String) : this() {
        this.email = email
        this.password = password
    }
}
