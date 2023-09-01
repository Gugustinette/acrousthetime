package fr.iutna.lpmiar.td1.controller

import fr.iutna.lpmiar.td1.batch.ImportICS
import fr.iutna.lpmiar.td1.services.ICSService
import fr.iutna.lpmiar.td1.model.Groupe
import fr.iutna.lpmiar.td1.services.GroupeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OulaGangController(
    @Autowired val icsEventService: ICSService,
    @Autowired val groupeService: GroupeService
) {
    @GetMapping("/oula")
    fun oula(): ResponseEntity<Any> {
        println("Import des ICS")
        ImportICS(icsEventService).importIcs()
        println("Import des ICS terminé")

        //Return ok
        return ResponseEntity.ok().build()
    }

    @GetMapping("/gang")
    fun gang(): ResponseEntity<Groupe> {
        val groupe = groupeService.find("g804118")

        if (groupe == null) {
            return ResponseEntity.notFound().build()
        }

        println(groupe.id)

        return ResponseEntity.ok(groupe)
    }
}