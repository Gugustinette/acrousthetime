package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.dtos.FavorisRequestId
import fr.iutna.lpmiar.td1.dtos.FavorisResponse
import fr.iutna.lpmiar.td1.model.User
import fr.iutna.lpmiar.td1.services.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/favoris")
@CrossOrigin(origins = ["*"])
class FavorisController(
    @Autowired val userService: UserService,
    @Autowired val etudiantService: EtudiantService,
    @Autowired val salleService: SalleService,
    @Autowired val personnelService: PersonnelService,
    @Autowired val groupeService: GroupeService
) {

    @GetMapping
    @Operation(summary = "Récupérer tous les favoris d'un utilisateur")
    @ApiResponse(
        responseCode = "200",
        description = "Liste des favoris",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = FavorisResponse::class))
            )
        ]
    )
    fun getAllFavorisFromUser(): ResponseEntity<FavorisResponse> {
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User
        val user = userService.findByEmail(userAuthenticate.email)

        if (user == null) {
            return ResponseEntity.badRequest().build()
        }

        val favoris = FavorisResponse()
        favoris.etudiant.addAll(user.etudiantFavoris)
        favoris.groupe.addAll(user.groupeFavoris)
        favoris.salle.addAll(user.salleFavoris)
        favoris.personnel.addAll(user.personnelFavoris)

        return ResponseEntity.ok(favoris)
    }


    @PostMapping("/etudiant")
    @Operation(summary = "Ajouter un étudiant en favoris")
    @ApiResponse(
        responseCode = "200",
        description = "Ajoute un étudiant en favoris",
    )
    fun addEtudiantFavoris(@RequestBody favorisRequest: FavorisRequestId): ResponseEntity<Any> {
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User
        val user = userService.findByEmail(userAuthenticate.email)

        if (user == null) {
            return ResponseEntity.badRequest().build()
        }

        val etudiant = etudiantService.find(favorisRequest.id)

        if (etudiant == null) {
            return ResponseEntity.notFound().build()
        }

        user.etudiantFavoris.add(etudiant)
        userService.save(user)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/groupe")
    @Operation(summary = "Ajouter un groupe en favoris")
    @ApiResponse(
        responseCode = "200",
        description = "Ajoute un groupe en favoris",
    )
    fun addGroupeFavoris(@RequestBody favorisRequest: FavorisRequestId): ResponseEntity<Any> {
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User
        val user = userService.findByEmail(userAuthenticate.email)

        if (user == null) {
            return ResponseEntity.badRequest().build()
        }

        val groupe = groupeService.find(favorisRequest.id)

        if (groupe == null) {
            return ResponseEntity.notFound().build()
        }

        user.groupeFavoris.add(groupe)
        userService.save(user)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/salle")
    @Operation(summary = "Ajouter une salle en favoris")
    @ApiResponse(
        responseCode = "200",
        description = "Ajoute une salle en favoris",
    )
    fun addSalleFavoris(@RequestBody favorisRequest: FavorisRequestId): ResponseEntity<Any> {
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User
        val user = userService.findByEmail(userAuthenticate.email)

        if (user == null) {
            return ResponseEntity.badRequest().build()
        }

        val salle = salleService.find(favorisRequest.id)

        if (salle == null) {
            return ResponseEntity.notFound().build()
        }

        user.salleFavoris.add(salle)
        userService.save(user)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/personnel")
    @Operation(summary = "Ajouter un personnel en favoris")
    @ApiResponse(
        responseCode = "200",
        description = "Ajoute un personnel en favoris",
    )
    fun addPersonnelFavoris(@RequestBody favorisRequest: FavorisRequestId): ResponseEntity<Any> {
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User
        val user = userService.findByEmail(userAuthenticate.email)

        if (user == null) {
            return ResponseEntity.badRequest().build()
        }

        val personnel = personnelService.find(favorisRequest.id)

        if (personnel == null) {
            return ResponseEntity.notFound().build()
        }

        user.personnelFavoris.add(personnel)
        userService.save(user)

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/etudiant")
    @Operation(summary = "Supprimer un étudiant des favoris")
    @ApiResponse(
        responseCode = "200",
        description = "Supprime un étudiant des favoris",
    )
    fun deleteEtudiantFavoris(@RequestBody favorisRequest: FavorisRequestId): ResponseEntity<Any> {
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User
        val user = userService.findByEmail(userAuthenticate.email)

        if (user == null) {
            return ResponseEntity.badRequest().build()
        }

        val etudiant = etudiantService.find(favorisRequest.id)

        if (etudiant == null) {
            return ResponseEntity.notFound().build()
        }

        user.etudiantFavoris.remove(etudiant)
        userService.save(user)

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/groupe")
    @Operation(summary = "Supprimer un groupe des favoris")
    @ApiResponse(
        responseCode = "200",
        description = "Supprime un groupe des favoris",
    )
    fun deleteGroupeFavoris(@RequestBody favorisRequest: FavorisRequestId): ResponseEntity<Any> {
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User
        val user = userService.findByEmail(userAuthenticate.email)

        if (user == null) {
            return ResponseEntity.badRequest().build()
        }

        val groupe = groupeService.find(favorisRequest.id)

        if (groupe == null) {
            return ResponseEntity.notFound().build()
        }

        user.groupeFavoris.remove(groupe)
        userService.save(user)

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/salle")
    @Operation(summary = "Supprimer une salle des favoris")
    @ApiResponse(
        responseCode = "200",
        description = "Supprime une salle des favoris",
    )
    fun deleteSalleFavoris(@RequestBody favorisRequest: FavorisRequestId): ResponseEntity<Any> {
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User
        val user = userService.findByEmail(userAuthenticate.email)

        if (user == null) {
            return ResponseEntity.badRequest().build()
        }

        val salle = salleService.find(favorisRequest.id)

        if (salle == null) {
            return ResponseEntity.notFound().build()
        }

        user.salleFavoris.remove(salle)
        userService.save(user)

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/personnel")
    @Operation(summary = "Supprimer un personnel des favoris")
    @ApiResponse(
        responseCode = "200",
        description = "Supprime un personnel des favoris",
    )
    fun deletePersonnelFavoris(@RequestBody favorisRequest: FavorisRequestId): ResponseEntity<Any> {
        val authenticate = SecurityContextHolder.getContext().authentication
        val userAuthenticate: User = authenticate.principal as User
        val user = userService.findByEmail(userAuthenticate.email)

        if (user == null) {
            return ResponseEntity.badRequest().build()
        }

        val personnel = personnelService.find(favorisRequest.id)

        if (personnel == null) {
            return ResponseEntity.notFound().build()
        }

        user.personnelFavoris.remove(personnel)
        userService.save(user)

        return ResponseEntity.ok().build()
    }
}