package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.dtos.CreneauxUid
import fr.iutna.lpmiar.td1.model.Creneaux
import fr.iutna.lpmiar.td1.model.Groupe
import fr.iutna.lpmiar.td1.services.CreneauxService
import fr.iutna.lpmiar.td1.services.GroupeService
import fr.iutna.lpmiar.td1.util.DateUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/groupe")
@CrossOrigin(origins = ["*"])
class GroupeController (
    @Autowired val groupeService: GroupeService,
    @Autowired val creneauxService: CreneauxService
) {
    @GetMapping("/{id}/semaine/{semaine}")
    @Operation(summary = "Récupération des infos d'un groupe en fonction de la semaine")
    @ApiResponse(
        responseCode = "200",
        description = "Infos du groupe en fonction de la semaine",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Groupe::class))
            )
        ]
    )
    fun showSemaine(@PathVariable id: String, @PathVariable semaine: String): ResponseEntity<Any> {

        val creneaux = creneauxService.findCreneauxByGroupeAndSemaine(id, DateUtil.stringToDate(semaine))

        return if (creneaux.isNotEmpty()) {
            ResponseEntity.ok(creneaux)
        } else {
            groupeService.find(id)?.let {
                ResponseEntity.ok(emptyList<Creneaux>())
            } ?: ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{id}/jour/{jour}")
    @Operation(summary = "Récupération des infos d'un groupe en fonction du jour")
    @ApiResponse(
        responseCode = "200",
        description = "Infos du groupe en fonction du jour",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Groupe::class))
            )
        ]
    )
    fun showJour (@PathVariable id: String, @PathVariable jour: String) : ResponseEntity<Any> {

        val creneaux = creneauxService.findCreneauxByGroupeAndJour(id, DateUtil.stringToDate(jour))

        return if (creneaux.isNotEmpty()) {
            ResponseEntity.ok(creneaux)
        } else {
            groupeService.find(id)?.let {
                ResponseEntity.ok(emptyList<Creneaux>())
            } ?: ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    @Operation(summary = "Ajout d'un groupe")
    @ApiResponse(
        responseCode = "200",
        description = "Ajout d'un groupe",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Groupe::class))
            )
        ]
    )
    fun addGroupe(@RequestBody groupe: Groupe): ResponseEntity<Groupe> {
        val groupeAdded = groupeService.save(groupe)
        return if (groupeAdded != null) {
            ResponseEntity.ok(groupeAdded)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{id}/addCreneaux")
    @Operation(summary = "Ajout d'un créneau à un groupe")
    @ApiResponse(
        responseCode = "200",
        description = "Ajout d'un créneau à un groupe",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Groupe::class))
            )
        ]
    )
    fun addCreneaux(@PathVariable id: String,
                    @RequestBody creneauId: CreneauxUid): ResponseEntity<Groupe> {
        val groupe = groupeService.find(id)

        if (groupe == null) {
            return ResponseEntity.notFound().build()
        }

        val creneau = creneauxService.find(creneauId.uid)

        if (creneau == null) {
            return ResponseEntity.notFound().build()
        }

        creneauxService.save(
            creneau.copy(
                groupe = creneau.groupe.plus(groupe)
            )
        )

        //get the groupe with the new creneau
        val groupeModifie = groupeService.find(id)

        return if (groupeModifie != null) {
            ResponseEntity.ok(groupeModifie)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupération des infos d'un groupe")
    @ApiResponse(
        responseCode = "200",
        description = "Infos du groupe",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Groupe::class))
            )
        ]
    )
    fun show(@PathVariable id: String): ResponseEntity<Groupe> {
        val groupe = groupeService.find(id)

        return if (groupe != null) {
            ResponseEntity.ok(groupe)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    @Operation(summary = "Récupération de tous les groupes")
    @ApiResponse(
        responseCode = "200",
        description = "Infos de tous les groupes",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Groupe::class))
            )
        ]
    )
    fun showAll(): ResponseEntity<List<Groupe>> {
        val groupes = groupeService.findAll()

        return ResponseEntity.ok(groupes)
    }

}