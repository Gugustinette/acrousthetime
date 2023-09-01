package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.model.User
import fr.iutna.lpmiar.td1.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService (val userRepository: UserRepository)
{
    fun findByEmail(email: String): User? {
        return userRepository.findUserByEmail(email)
    }

    fun save(user: User) {
        userRepository.save(user)
    }
}