package fr.iutna.lpmiar.td1

import com.google.gson.GsonBuilder
import fr.iutna.lpmiar.td1.dtos.AuthenticationResponse
import fr.iutna.lpmiar.td1.dtos.RegisterRequest
import fr.iutna.lpmiar.td1.model.Etudiant
import fr.iutna.lpmiar.td1.model.Personnel
import fr.iutna.lpmiar.td1.services.AuthenticationService
import fr.iutna.lpmiar.td1.services.EtudiantService
import fr.iutna.lpmiar.td1.services.PersonnelService
import fr.iutna.lpmiar.td1.services.ReservationService
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
class TestRechercheMock {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @Autowired
    private lateinit var reservationService: ReservationService

    @Autowired
    private lateinit var etudiantService: EtudiantService

    @Autowired
    private lateinit var personnelService: PersonnelService


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
        }
        return ""
    }

    /**
     * testSearch
     * Test the search function
     */
    @Test
    @Order(1)
    fun findEmpty() {
        val token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/recherche?search=gang")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.etudiant").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.personnel").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.groupe").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.salle").isEmpty)
    }


    @Test
    @Order(2)
    fun addStudentAndPersonnel() {
        val token = getToken()

        val etudiant = Etudiant(
            "1",
            "Oula gang"
        )

        val personnel = Personnel(
            "2",
            "Oula gangito"
        )

        etudiantService.save(etudiant)
        personnelService.save(personnel)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/personnel")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/etudiant")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
    }

    @Test
    @Order(3)
    fun testSearch() {
        val token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/recherche?search=gang")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.etudiant.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.personnel.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.groupe").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.salle").isEmpty)


        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/recherche?search=gangito")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.personnel.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.etudiant").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.groupe").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.salle").isEmpty)
    }
}