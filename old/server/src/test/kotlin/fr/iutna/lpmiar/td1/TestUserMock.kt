package fr.iutna.lpmiar.td1

import fr.iutna.lpmiar.td1.dtos.AuthenticationResponse
import fr.iutna.lpmiar.td1.dtos.RegisterRequest
import fr.iutna.lpmiar.td1.services.AuthenticationService
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
class TestUserMock {
    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @Autowired
    private lateinit var mockMvc: MockMvc

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
    fun show(){
        val token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/user/moi")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(16))


    }
}