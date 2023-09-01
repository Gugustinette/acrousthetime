package fr.iutna.lpmiar.td1

import com.google.gson.GsonBuilder
import fr.iutna.lpmiar.td1.dtos.AuthenticationResponse
import fr.iutna.lpmiar.td1.dtos.CreneauxUid
import fr.iutna.lpmiar.td1.dtos.RegisterRequest
import fr.iutna.lpmiar.td1.model.Creneaux
import fr.iutna.lpmiar.td1.model.Etudiant
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
class TestEtudiantMock {
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
    @Order(1)
    fun getStudentEmpty() {
        val token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/etudiant")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    @Order(2)
    fun addStudent() {
        val token = getToken()

        val student = Etudiant(
            "1",
            "GLOBUTRON"
        )

        val json = gson.toJson(student)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/etudiant")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.name").value("GLOBUTRON"))
    }

    @Test
    @Order(3)
    fun showAll() {
        val token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/etudiant")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
    }

    @Test
    @Order(4)
    fun addCreneauToStudent() {
        val token = getToken()

        val creneau = Creneaux(
                "3",
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

        // Ajout du creneaux
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/creneaux")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.uid").value("3"))
            .andExpect(jsonPath("$.matiere").value("Matiere 1"))

        // Ajout du creneaux à l'étudiant
        json = gson.toJson(CreneauxUid(creneau.uid))

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/etudiant/1/addCreneaux")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.name").value("GLOBUTRON"))
    }

    @Test
    @Order(5)
    fun findTest() {
        val token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/etudiant")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.length()").value(1))

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/etudiant/1/semaine/2023-06-30T10:30:00.000Z")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/etudiant/1/semaine/2023-07-30T10:30:00.000Z")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/etudiant/1/jour/2023-06-30T10:30:00.000Z")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/etudiant/1/jour/2023-06-29T10:30:00.000Z")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
    }
}