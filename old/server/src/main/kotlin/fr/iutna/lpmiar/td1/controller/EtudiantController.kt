package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.dtos.CreneauxUid
import fr.iutna.lpmiar.td1.model.Creneaux
import fr.iutna.lpmiar.td1.model.Etudiant
import fr.iutna.lpmiar.td1.services.CreneauxService
import fr.iutna.lpmiar.td1.services.EtudiantService
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
@RequestMapping("/etudiant")
@CrossOrigin(origins = ["*"])
class EtudiantController (@Autowired val etudiantService: EtudiantService,
                          @Autowired val creneauxService: CreneauxService){

    @GetMapping
    @Operation(summary = "Récupère tous les étudiants")
    @ApiResponse(
        responseCode = "200",
        description = "Liste des étudiants",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Etudiant::class))
            )
        ]
    )
    fun showAll() = etudiantService.findAll()

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un étudiant")
    @ApiResponse(
        responseCode = "200",
        description = "Récupère un étudiant",
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = Etudiant::class)
            )
        ]
    )
    fun showOne(@PathVariable id: String): ResponseEntity<Etudiant> {
        val etudiant = etudiantService.find(id)

        return if (etudiant == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(etudiant)
        }
    }

    @PostMapping
    @Operation(summary = "Ajoute un étudiant")
    @ApiResponse(
        responseCode = "200",
        description = "Etudiant ajouté",
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = Etudiant::class)
            )
        ]
    )
    fun addEtudiant(@RequestBody etudiant: Etudiant): ResponseEntity<Etudiant> {
        val etudiantModifie = etudiantService.save(etudiant)

        return if (etudiantModifie != null) {
            ResponseEntity.ok(etudiantModifie)
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{id}/semaine/{semaine}")
    @Operation(summary = "Récupération des creneaux d'un étudiant en fonction de la semaine choisie")
    @ApiResponse(
        responseCode = "200",
        description = "Creneaux de l'étudiant en fonction de la semaine",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Creneaux::class))
            )
        ]
    )
    fun showSemaine(@PathVariable id: String, @PathVariable semaine: String): ResponseEntity<List<Creneaux>> {
        val creneaux = creneauxService.findCreneauxByEtudiantAndSemaine(id, DateUtil.stringToDate(semaine))

        return if (creneaux.isNotEmpty()) {
            ResponseEntity.ok(creneaux)
        } else {
            if (etudiantService.find(id) != null) {
                ResponseEntity.ok(emptyList())
            } else {
                ResponseEntity.notFound().build()
            }
        }
    }

    @GetMapping("/{id}/jour/{jour}")
    @Operation(summary = "Récupération des creneaux d'un étudiant en fonction du jour choisi")
    @ApiResponse(
        responseCode = "200",
        description = "Creneaux de l'étudiant en fonction du jour",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = Creneaux::class))
            )
        ]
    )
    fun showJour(@PathVariable id: String, @PathVariable jour: String): ResponseEntity<List<Creneaux>> {
        val creneaux = creneauxService.findCreneauxByEtudiantAndJour(id, DateUtil.stringToDate(jour))

        return if (creneaux.isNotEmpty()) {
            ResponseEntity.ok(creneaux)
        } else {
            if (etudiantService.find(id) != null) {
                ResponseEntity.ok(emptyList())
            } else {
                ResponseEntity.notFound().build()
            }
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
                array = ArraySchema(schema = Schema(implementation = Etudiant::class))
            )
        ]
    )
    fun addCreneaux(@PathVariable id: String,
                    @RequestBody creneauId: CreneauxUid
    ): ResponseEntity<Etudiant> {
        val etudiant = etudiantService.find(id)

        if (etudiant == null) {
            return ResponseEntity.notFound().build()
        }

        val creneau = creneauxService.find(creneauId.uid)

        if (creneau == null) {
            return ResponseEntity.notFound().build()
        }

        creneauxService.save(
            creneau.copy(
                etudiant = creneau.etudiant.plus(etudiant)
            )
        )

        val etudiantModifie = etudiantService.find(id)

        return if (etudiantModifie != null) {
            ResponseEntity.ok(etudiantModifie)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}