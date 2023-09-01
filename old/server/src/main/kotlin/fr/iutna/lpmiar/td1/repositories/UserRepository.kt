package fr.iutna.lpmiar.td1.repositories

import fr.iutna.lpmiar.td1.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Int> {
    /**
     * Find a user by its email.
     */
    fun findUserByEmail(email: String): User

}
