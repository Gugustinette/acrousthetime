package fr.iutna.lpmiar.td1.repositories

import fr.iutna.lpmiar.td1.model.Image
import fr.iutna.lpmiar.td1.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long> {
    fun findByUser(user: User): Image?
}