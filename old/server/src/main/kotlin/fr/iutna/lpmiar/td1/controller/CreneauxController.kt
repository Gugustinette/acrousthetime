package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.model.Creneaux
import fr.iutna.lpmiar.td1.services.CreneauxService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/creneaux")
@CrossOrigin(origins = ["*"])
class CreneauxController (@Autowired val service: CreneauxService){
    @GetMapping
    @Operation(summary = "Get all creneaux")
    @ApiResponse(
        responseCode = "200",
        description = "Liste des creneaux",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Creneaux::class))
            )
        ]
    )
    fun showAll (): List<Creneaux> = service.findAll()

    @GetMapping("/favoris/semaine/{semaine}")
    @Operation(summary = "Get all creneaux favoris par semaine")
    @ApiResponse(
        responseCode = "200",
        description = "Liste des creneaux favoris par semaine",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Creneaux::class))
            )
        ]
    )
    fun showFavorisSemaine (@PathVariable semaine: Int): List<Creneaux> = listOf() //TODO : return all creneaux favoris par semaine

    @GetMapping("/favoris/jour/{jour}")
    @Operation(summary = "Get all creneaux favoris par jour")
    @ApiResponse(
        responseCode = "200",
        description = "Liste des creneaux favoris par jour",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Creneaux::class))
            )
        ]
    )
    fun showFavorisJour (@PathVariable jour: Int): List<Creneaux> = listOf() //TODO : return all creneaux favoris par jour

    @GetMapping("/semaine/{semaine}")
    @Operation(summary = "Get all creneaux par semaine")
    @ApiResponse(
        responseCode = "200",
        description = "Liste des creneaux par semaine",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Creneaux::class))
            )
        ]
    )
    fun showSemaine (@PathVariable semaine: Int): List<Creneaux> = listOf() //TODO : return all creneaux par semaine

    @GetMapping("/jour/{jour}")
    @Operation(summary = "Get all creneaux par jour")
    @ApiResponse(
        responseCode = "200",
        description = "Liste des creneaux par jour",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Creneaux::class))
            )
        ]
    )
    fun showJour (@PathVariable jour: Int): List<Creneaux> = listOf() //TODO : return all creneaux par jour

    @PostMapping
    @Operation(summary = "Ajout creneau")
    @ApiResponse(
        responseCode = "200",
        description = "Ajout creneau",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Creneaux::class))
            )
        ]
    )
    fun addCreneau (@RequestBody creneau: Creneaux): ResponseEntity<Creneaux>{
        val newCreneau = service.save(creneau)
        return if (newCreneau != null) {
            ResponseEntity.ok(newCreneau)
        } else {
            ResponseEntity.badRequest().build()
        }
    }
}