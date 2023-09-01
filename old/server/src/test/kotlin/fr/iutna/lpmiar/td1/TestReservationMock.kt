package fr.iutna.lpmiar.td1

import com.google.gson.GsonBuilder
import fr.iutna.lpmiar.td1.dtos.AuthenticationRequest
import fr.iutna.lpmiar.td1.dtos.AuthenticationResponse
import fr.iutna.lpmiar.td1.dtos.RegisterRequest
import fr.iutna.lpmiar.td1.model.Reservation
import fr.iutna.lpmiar.td1.model.Role
import fr.iutna.lpmiar.td1.model.StatusReservation
import fr.iutna.lpmiar.td1.model.User
import fr.iutna.lpmiar.td1.repositories.UserRepository
import fr.iutna.lpmiar.td1.services.AuthenticationService
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import java.util.*

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestReservationMock {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @Autowired
    private lateinit var reservationService: ReservationService

    @Autowired
    private lateinit var userRepository: UserRepository

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
    @Order(0)
    fun emptyReservation(){
        var token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/reservation/1")
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @Order(1)
    fun ajoutReservation(){
        val token = getToken()
        val reservation = Reservation(
            id = null,
            dt_start = DateUtil.stringToDate("2021-01-01T00:00:00.000Z"),
            dt_end = DateUtil.stringToDate("2021-01-01T00:00:00.000Z"),
            description = "test",
            status = StatusReservation.PENDING,
            salle = null,
            user = null
        )
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/reservation")
                .header("authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(reservation))
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
            .andReturn()
    }

    @Test
    @Order(2)
    fun getAllReservation(){
        var token = getToken()
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/reservation")
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("test"))
            .andReturn()
    }

    @Test
    @Order(3)
    @WithMockUser(username = "user", password = "password", roles = ["ADMIN"])
    fun edit() {
        var token = getToken()

        val liste = reservationService.findAll()

        val id = liste[0].id

        val reservation = Reservation(
            id = id,
            dt_start = DateUtil.stringToDate("2021-01-01T00:00:00.000Z"),
            dt_end = DateUtil.stringToDate("2021-01-01T00:00:00.000Z"),
            description = "test edited",
            status = StatusReservation.PENDING,
            salle = null,
            user = null
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/reservation/$id")
                .header("authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(reservation))
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("test edited"))
            .andReturn()
    }

    @Test
    @Order(4)
    fun getAllAfterEdit() {
        var token = getToken()
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/reservation")
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("test edited"))
            .andReturn()
    }

    @Test
    @Order(5)
    fun acceptReservation() {
        val liste = reservationService.findAll()

        val user = User("test", "test", "test@test.com", BCryptPasswordEncoder().encode("test"), Role.ADMIN)
        user.id = 1
        userRepository.save(user)
        val token = authenticationService.authenticate(
            AuthenticationRequest("test@test.com", "test")
        ).token

        val id = liste[0].id

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/reservation/accept/$id")
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Order(6)
    fun getAllAfterAccept() {
        var token = getToken()
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/reservation")
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("ACCEPTED"))
            .andReturn()
    }

    @Test
    @Order(7)
    @WithMockUser(username = "user", password = "password", roles = ["ADMIN"])
    fun delete() {
        var token = getToken()

        val liste = reservationService.findAll()

        val id = liste[0].id

        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/reservation/$id")
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Order(8)
    fun getAllAfterDelete() {
        var token = getToken()
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/reservation")
                .header("authorization", "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty)
            .andReturn()
    }
}