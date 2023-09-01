package fr.iutna.lpmiar.td1

import com.google.gson.GsonBuilder
import fr.iutna.lpmiar.td1.dtos.AuthenticationResponse
import fr.iutna.lpmiar.td1.dtos.CreneauxUid
import fr.iutna.lpmiar.td1.dtos.RegisterRequest
import fr.iutna.lpmiar.td1.model.Creneaux
import fr.iutna.lpmiar.td1.model.Salle
import fr.iutna.lpmiar.td1.model.TypeSalle
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
import java.util.*

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestSalleMock {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    val gson = GsonBuilder().setDateFormat(DateUtil.FORMAT).create()

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
    @Order(1)
    fun getSalleEmpty() {
        val token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/salle")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))
    }

    @Test
    @Order(2)
    fun addSalle() {
        val token = getToken()

        val salle = Salle(
            "KC",
            "KCORP",
            30,
            TypeSalle.AVEC_PC
        )

        val json = gson.toJson(salle)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/salle")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("KC"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("KCORP"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.capacity").value(30))
            .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("AVEC_PC"))
    }

    @Test
    @Order(3)
    fun addCreneauxToPersonnel() {
        val token = getToken()

        val creneau = Creneaux(
            "6",
            DateUtil.stringToDate("2023-06-30T10:30:00.000Z"),
            DateUtil.stringToDate("2023-06-30T12:30:00.000Z"),
            "Summary 1",
            etudiant = emptyList(),
            salle = emptyList(),
            groupe = emptyList(),
            personnel = emptyList(),
            matiere = "Matiere 1",
            last_update = DateUtil.stringToDate("2023-06-30T12:00:00.000Z")
        )

        var json = gson.toJson(creneau)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/creneaux")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.uid").value("6"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.matiere").value("Matiere 1"))

        json = gson.toJson(CreneauxUid(creneau.uid))

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/salle/KC/addCreneaux")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("KC"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("KCORP"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.capacity").value(30))
            .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("AVEC_PC"))
    }

    @Test
    @Order(4)
    fun findTest() {
        val token = getToken()
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/salle")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/salle/KC/semaine/2023-06-30T10:30:00.000Z")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/salle/KC/jour/2023-06-30T10:30:00.000Z")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/salle/KC/semaine/2023-08-30T10:30:00.000Z")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/salle/KC/jour/2023-06-26T10:30:00.000Z")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))
    }
    @Test
    @Order(5)
    fun findSalle(){
        val token = getToken()
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/salle/date_debut/2023-06-30T10:30:00.000Z/date_fin/2023-06-30T12:30:00.000Z")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))

    }


}