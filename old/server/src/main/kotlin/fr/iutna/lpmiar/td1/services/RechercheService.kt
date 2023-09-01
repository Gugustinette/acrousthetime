package fr.iutna.lpmiar.td1.services

import fr.iutna.lpmiar.td1.dtos.SearchResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RechercheService(
    @Autowired val etudiantService: EtudiantService,
    @Autowired val groupeService: GroupeService,
    @Autowired val personnelService: PersonnelService,
    @Autowired val salleService: SalleService
) {
    fun search(search: String): SearchResponse {
        val response = SearchResponse()

        etudiantService.findAllContains(search).forEach { response.etudiant.add(it) }
        groupeService.findAllContains(search).forEach { response.groupe.add(it) }
        personnelService.findAllContains(search).forEach { response.personnel.add(it) }
        salleService.findAllContains(search).forEach { response.salle.add(it) }

        return response
    }

}
