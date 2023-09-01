package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.dtos.UserEdit
import fr.iutna.lpmiar.td1.model.User
import fr.iutna.lpmiar.td1.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = ["*"])
class UserController (
    @Autowired val service: UserService
){
    @GetMapping("/moi")
    @Operation(summary = "Get user")
    @ApiResponse(
        responseCode = "200",
        description = "User",
        content = [
            io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = Schema(implementation = User::class)
            )
        ]
    )
    fun showMoi (): ResponseEntity<Any> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        return ResponseEntity.ok(user)
    }


    @PutMapping
    @Operation(summary = "Update user")
    @ApiResponse(
        responseCode = "200",
        description = "User",
        content = [
            io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = Schema(implementation = User::class)
            )
        ]
    )
    fun update (@RequestBody user: UserEdit): ResponseEntity<Any> {
        val currentUser = SecurityContextHolder.getContext().authentication.principal as User

        if (currentUser.id == null) {
            return ResponseEntity.notFound().build()
        }

        currentUser.nom = user.nom
        currentUser.prenom = user.prenom

        return ResponseEntity.ok(service.save(currentUser))
    }
}