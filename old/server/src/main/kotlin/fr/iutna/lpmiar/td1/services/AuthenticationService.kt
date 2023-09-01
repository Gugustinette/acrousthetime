package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.dtos.AuthenticationRequest
import fr.iutna.lpmiar.td1.dtos.AuthenticationResponse
import fr.iutna.lpmiar.td1.dtos.RegisterRequest
import fr.iutna.lpmiar.td1.model.Role
import fr.iutna.lpmiar.td1.model.User
import fr.iutna.lpmiar.td1.repositories.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager
) {
    /**
     * Register a new user.
     */
    fun register(request: RegisterRequest): AuthenticationResponse? {
        val utilisateur = User(
            request.nom!!,
            request.prenom!!,
            request.email!!,
            passwordEncoder.encode(request.password!!),
            Role.USER
        )
        utilisateur.id = userRepository.findAll().size + 1
        userRepository.save(utilisateur)
        val jwtToken = jwtService.generateToken(utilisateur)
        return AuthenticationResponse(jwtToken)
    }


    /**
     * Authenticate a user.
     */
    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )
        val utilisateur = userRepository.findUserByEmail(request.email)
        val jwtToken = jwtService.generateToken(utilisateur)
        return AuthenticationResponse(jwtToken)
    }
}
