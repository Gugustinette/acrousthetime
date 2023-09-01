package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.dtos.SearchResponse
import fr.iutna.lpmiar.td1.services.RechercheService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/recherche")
@CrossOrigin(origins = ["*"])
class RechercheController(@Autowired val rechercheService: RechercheService) {

    @GetMapping
    fun search(@RequestParam(name = "search") search: String): ResponseEntity<SearchResponse> {
        return ResponseEntity.ok(rechercheService.search(search))
    }
}