package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.model.Image
import fr.iutna.lpmiar.td1.model.User
import fr.iutna.lpmiar.td1.repositories.ImageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption


@Service
class ImageService(private val imageRepository: ImageRepository) {

    val uploadDirectory = "src/main/resources/image" // Chemin d'accès au dossier de destination

    fun saveImage(file: MultipartFile, user: User) {
        val fileName = file.originalFilename ?: throw IllegalArgumentException("Invalid file name")

        val image = Image(fileName = fileName, user = user)

        try {
            // Save image file to the server
            val destinationPath = Path.of(uploadDirectory, fileName)
            Files.copy(file.inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING)

            // Save image metadata to the database
            imageRepository.save(image)
        } catch (ex: IOException) {
            throw RuntimeException("Failed to save image", ex)
        }
    }

    fun getImage(user: User): ByteArray {
        val image = imageRepository.findByUser(user)
            ?: throw IllegalArgumentException("Image not found for the given user")

        val imagePath = Path.of(uploadDirectory, image.fileName)
        return Files.readAllBytes(imagePath)
    }
}