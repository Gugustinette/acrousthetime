package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.model.User
import fr.iutna.lpmiar.td1.services.ImageService
import fr.iutna.lpmiar.td1.util.UtilDev
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = ["*"])
class ImageController @Autowired constructor( @Autowired  val imageService: ImageService) {

    @PostMapping("/upload")
    @Operation(summary = "Upload d'une image")
    @ApiResponse(
        responseCode = "200",
        description = "Image uploadée"
    )
    fun uploadImage(@RequestHeader("Authorization") token: String, @RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val user = SecurityContextHolder.getContext().authentication.principal as User

        try {
            imageService.saveImage(file, user)
            return ResponseEntity.ok().body("Image uploaded successfully.")
        } catch (ex: Exception) {
            return ResponseEntity.status(500).body("Failed to upload image.")
        }
    }

    @GetMapping("/user")
    @Operation(summary = "Récupération d'une image")
    @ApiResponse(
        responseCode = "200",
        description = "Image récupérée"
    )
    fun getImage(@RequestHeader("Authorization") token: String): ResponseEntity<Any> {
        val user = SecurityContextHolder.getContext().authentication.principal as User

        try {
            val image = imageService.getImage(user)
            val headers = HttpHeaders()
            headers.contentType = MediaType.IMAGE_JPEG
            return ResponseEntity.ok().headers(headers).body(ByteArrayResource(image))
        } catch (ex: Exception) {
            UtilDev.printRed(ex.toString())
            return ResponseEntity.status(500).body("Failed to get image")
        }
    }
}