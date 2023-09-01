package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.dtos.CreneauxUid
import fr.iutna.lpmiar.td1.model.Creneaux
import fr.iutna.lpmiar.td1.model.Salle
import fr.iutna.lpmiar.td1.services.CreneauxService
import fr.iutna.lpmiar.td1.services.SalleService
import fr.iutna.lpmiar.td1.util.DateUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/salle")
@CrossOrigin(origins = ["*"])
class SalleController (@Autowired val salleService: SalleService,
                       @Autowired val creneauxService: CreneauxService) {
    @GetMapping("/{id}/semaine/{semaine}")
    @Operation(summary = "Récupération des infos d'une salle par semaine")
    @ApiResponse(
        responseCode = "200",
        description = "Infos de la salle par semaine",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Salle::class))
            )
        ]
    )
    fun showSemaine (@PathVariable id: String, @PathVariable semaine: String): ResponseEntity<List<Creneaux>> {
        val creneaux = creneauxService.findCreneauxBySalleAndSemaine(id, DateUtil.stringToDate(semaine))

        return if (creneaux.isNotEmpty()) {
            ResponseEntity.ok(creneaux)
        } else {
            if (salleService.find(id) != null) {
                ResponseEntity.ok(emptyList())
            } else {
                ResponseEntity.notFound().build()
            }
        }
    }

    @GetMapping("/{id}/jour/{jour}")
    @Operation(summary = "Récupération des infos d'une salle par jour")
    @ApiResponse(
        responseCode = "200",
        description = "Infos de la salle par jour",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Salle::class))
            )
        ]
    )
    fun showJour (@PathVariable id: String, @PathVariable jour: String) : ResponseEntity<List<Creneaux>> {
        val creneaux = creneauxService.findCreneauxBySalleAndJour(id, DateUtil.stringToDate(jour))

        return if (creneaux.isNotEmpty()) {
            ResponseEntity.ok(creneaux)
        } else {
            if (salleService.find(id) != null) {
                ResponseEntity.ok(emptyList())
            } else {
                ResponseEntity.notFound().build()
            }
        }
    }


    @PostMapping("/{id}/addCreneaux")
    @Operation(summary = "Ajout d'un créneau à une salle")
    @ApiResponse(
        responseCode = "200",
        description = "Ajout d'un créneau à une salle",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Salle::class))
            )
        ]
    )
    fun addCreneaux(@PathVariable id: String,
                    @RequestBody creneauId: CreneauxUid
    ): ResponseEntity<Salle> {
        val salle = salleService.find(id)

        if (salle == null) {
            return ResponseEntity.notFound().build()
        }

        val creneau = creneauxService.find(creneauId.uid)

        if (creneau == null) {
            return ResponseEntity.notFound().build()
        }

        creneauxService.save(
            creneau.copy(
                salle = creneau.salle.plus(salle)
            )
        )

        //get the groupe with the new creneau
        val salleModifie = salleService.find(id)

        return if (salleModifie != null) {
            ResponseEntity.ok(salleModifie)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    @Operation(summary = "Ajout d'une salle")
    @ApiResponse(
        responseCode = "200",
        description = "Ajout d'une salle",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Salle::class))
            )
        ]
    )
    fun addSalle(@RequestBody salle: Salle): ResponseEntity<Salle> {
        val salleModifie = salleService.save(salle)

        return if (salleModifie != null) {
            ResponseEntity.ok(salleModifie)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    @Operation(summary = "Récupération de toutes les salles")
    @ApiResponse(
        responseCode = "200",
        description = "Récupération de toutes les salles",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Salle::class))
            )
        ]
    )
    fun getAllSalle(): ResponseEntity<List<Salle>> {
        return ResponseEntity.ok(salleService.findAll())
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupération d'une salle")
    @ApiResponse(
        responseCode = "200",
        description = "Récupération d'une salle",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Salle::class))
            )
        ]
    )
    fun getSalle(@PathVariable id: String): ResponseEntity<Salle> {
        val salle = salleService.find(id)

        return if (salle != null) {
            ResponseEntity.ok(salle)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/date_debut/{date_debut}/date_fin/{date_fin}")
    @Operation(summary = "Récupération des salles pour une période")
    @ApiResponse(
        responseCode = "200",
        description = "Récupération des salles disponibles",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Salle::class))
            )
        ]
    )
    fun getSalleByDate(@PathVariable date_debut: String, @PathVariable date_fin: String): ResponseEntity<List<Salle>> {
        val salles = salleService.findSalleByDate(DateUtil.stringToDate(date_debut), DateUtil.stringToDate(date_fin))

        return if (salles.isNotEmpty()) {
            ResponseEntity.ok(salles)
        } else {
            ResponseEntity.ok(emptyList())
        }
    }
}