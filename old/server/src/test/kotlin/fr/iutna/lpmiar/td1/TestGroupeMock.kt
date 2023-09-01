package fr.iutna.lpmiar.td1

import com.google.gson.GsonBuilder
import fr.iutna.lpmiar.td1.dtos.AuthenticationResponse
import fr.iutna.lpmiar.td1.dtos.CreneauxUid
import fr.iutna.lpmiar.td1.dtos.RegisterRequest
import fr.iutna.lpmiar.td1.model.Creneaux
import fr.iutna.lpmiar.td1.model.Groupe
import fr.iutna.lpmiar.td1.services.AuthenticationService
import fr.iutna.lpmiar.td1.util.DateUtil
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.util.*

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestGroupeMock {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    private var gson = GsonBuilder().setDateFormat(DateUtil.FORMAT).create()

    /**
     * getToken
     * Register a new user and return the token in body
     */
    fun getToken(): String {
        // Create new RegisterRequest
        var registerRequest = RegisterRequest(
            "Test",
            "Test",
            UUID.randomUUID().toString() + "@test.com",
            "test"
        )

        // Register the user
        var response = authenticationService.register(registerRequest)
        // Return the token
        if (response is AuthenticationResponse) {
            return response.token
        } else {
            return ""
        }
    }
    @Test
    @Order(0)
    /**
     * testGetGroupeSemaineVide
     * Test the GET request on /groupe/{id}/semaine/{semaine}
     */
    fun testEmptyGroupe() {
    // Get the token
        var token = getToken()


        // Test the request
        mockMvc.perform(
            MockMvcRequestBuilders.get("/groupe/1/semaine/2023-06-30T10:30:00.000Z")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @Order(1)
    fun ajoutGroupeSemaine(){
        var token = getToken()

        val listCreneaux = listOf(
            Creneaux(
                "1",
                DateUtil.stringToDate("2023-06-30T10:30:00.000Z"),
                DateUtil.stringToDate("2023-06-30T12:30:00.000Z"),
                "Summary 1",
                etudiant = emptyList(),
                salle = emptyList(),
                groupe = emptyList(),
                personnel = emptyList(),
                matiere = "Matiere 1",
                last_update = DateUtil.stringToDate("2023-06-30T12:00:00.000Z")
            ),
            Creneaux(
                "2",
                DateUtil.stringToDate("2023-06-30T14:30:00.000Z"),
                DateUtil.stringToDate("2023-06-30T16:30:00.000Z"),
                "Summary 2",
                etudiant = emptyList(),
                salle = emptyList(),
                groupe = emptyList(),
                personnel = emptyList(),
                matiere = "Matiere 2",
                last_update = DateUtil.stringToDate("2023-06-30T16:00:00.000Z")
            )
        )

        val groupeAdd = Groupe(
            "1",
            "test",
        )

        var json = gson.toJson(groupeAdd)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/groupe")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + token)
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("test"))



        val groupeAdd2 = Groupe(
            "2",
            "test2",
        )

        json = gson.toJson(groupeAdd2)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/groupe")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + token)
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("test2"))

        var compteur = 1

        listCreneaux.forEach {
            json = gson.toJson(it)

            mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/creneaux")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("authorization", "Bearer " + token)
                    .content(json)
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.uid").exists())

            json = gson.toJson(CreneauxUid(it.uid))

            mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/groupe/${groupeAdd.id}/addCreneaux")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("authorization", "Bearer " + token)
                    .content(json)
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("test"))

            mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/groupe/${groupeAdd2.id}/addCreneaux")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("authorization", "Bearer " + token)
                    .content(json)
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("test2"))

            compteur++
        }
    }


    @Test
    @Order(2)
    fun find() {
        var token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/creneaux")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.length()").value(2))

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/groupe/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("test"))

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/groupe/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("test2"))
    }

    @Test
    @Order(3)
    fun findByDate() {
        var token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/groupe/1/semaine/2023-06-30T10:30:00.000Z")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.length()").value(2))
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/groupe/1/semaine/2022-06-30T10:30:00.000Z")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/groupe/1/jour/2023-06-30T10:30:00.000Z")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer $token")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.length()").value(2))
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/groupe/1/jour/2022-06-29T10:30:00.000Z")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
    }

}

