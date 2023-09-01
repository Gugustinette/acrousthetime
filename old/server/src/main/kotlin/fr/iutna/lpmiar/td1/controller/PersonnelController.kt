package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.dtos.CreneauxUid
import fr.iutna.lpmiar.td1.model.Creneaux
import fr.iutna.lpmiar.td1.model.Personnel
import fr.iutna.lpmiar.td1.services.CreneauxService
import fr.iutna.lpmiar.td1.services.PersonnelService
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
@RequestMapping("/personnel")
@CrossOrigin(origins = ["*"])
class PersonnelController (@Autowired val personnelService: PersonnelService,
                           @Autowired val creneauxService: CreneauxService) {
    @GetMapping("/{id}/semaine/{semaine}")
    @Operation(summary = "Récupération des infos d'un personnel par semaine")
    @ApiResponse(
        responseCode = "200",
        description = "Infos du personnel par semaine",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Personnel::class))
            )
        ]
    )
    fun showSemaine (@PathVariable id: String, @PathVariable semaine: String) : ResponseEntity<List<Creneaux>>
    {
        val creneaux = creneauxService.findCreneauxByPersonnelAndSemaine(id, DateUtil.stringToDate(semaine))

        return if (creneaux.isNotEmpty()) {
            ResponseEntity.ok(creneaux)
        } else {
            if (personnelService.find(id) != null) {
                ResponseEntity.ok(emptyList())
            } else {
                ResponseEntity.notFound().build()
            }
        }
    }

    @GetMapping("/{id}/jour/{jour}")
    @Operation(summary = "Récupération des infos d'un personnel par jour")
    @ApiResponse(
        responseCode = "200",
        description = "Infos du personnel par jour",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Personnel::class))
            )
        ]
    )
    fun showJour (@PathVariable id: String, @PathVariable jour: String) : ResponseEntity<List<Creneaux>>{
        val creneaux = creneauxService.findCreneauxByPersonnelAndJour( id, DateUtil.stringToDate(jour))
        return if (creneaux.isNotEmpty()) {
            ResponseEntity.ok(creneaux)
        } else {
            if (personnelService.find(id) != null) {
                ResponseEntity.ok(emptyList())
            } else {
                ResponseEntity.notFound().build()
            }
        }
    }

    @PostMapping("/{id}/addCreneaux")
    @Operation(summary = "Ajout d'un créneau à un personnel")
    @ApiResponse(
        responseCode = "200",
        description = "Ajout d'un créneau à un personnel",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Personnel::class))
            )
        ]
    )
    fun addCreneaux(@PathVariable id: String,
                    @RequestBody creneauId: CreneauxUid
    ): ResponseEntity<Personnel> {
        val personnel = personnelService.find(id)

        if (personnel == null) {
            return ResponseEntity.notFound().build()
        }

        val creneau = creneauxService.find(creneauId.uid)

        if (creneau == null) {
            return ResponseEntity.notFound().build()
        }

        creneauxService.save(
            creneau.copy(
                personnel = creneau.personnel.plus(personnel)
            )
        )

        //get the groupe with the new creneau
        val personnelModifie = personnelService.find(id)

        return if (personnelModifie != null) {
            ResponseEntity.ok(personnelModifie)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    @Operation(summary = "Ajout d'un personnel")
    @ApiResponse(
        responseCode = "200",
        description = "Ajout d'un personnel",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Personnel::class))
            )
        ]
    )
    fun addPersonnel(@RequestBody personnel: Personnel): ResponseEntity<Personnel> {
        personnelService.save(personnel)

        return ResponseEntity.ok(personnel)
    }

    @GetMapping
    @Operation(summary = "Récupération de tous les personnels")
    @ApiResponse(
        responseCode = "200",
        description = "Récupération de tous les personnels",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Personnel::class))
            )
        ]
    )
    fun showAllPersonnel() = personnelService.findAll()

    @GetMapping("/{id}")
    @Operation(summary = "Récupération d'un personnel")
    @ApiResponse(
        responseCode = "200",
        description = "Récupération d'un personnel",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Personnel::class))
            )
        ]
    )
    fun showPersonnel(@PathVariable id: String): ResponseEntity<Personnel> {
        val personnel = personnelService.find(id)

        return if (personnel != null) {
            ResponseEntity.ok(personnel)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}