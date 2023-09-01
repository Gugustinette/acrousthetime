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
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestImageMock {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var authenticationService: AuthenticationService


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
    fun emptyImage() {
        val token = getToken()
        mockMvc.perform(
            get("/image/user")
                .header("Authorization", "Bearer $token")
        ).andExpect(status().isInternalServerError)
            .andExpect(content().string("Failed to get image"))
            .andReturn()
    }

    @Test
    @Order(1)
    fun getImage(){
        val token = getToken()

        val imageFile = Paths.get("src/test/kotlin/fr/iutna/lpmiar/td1/ressources/testImage.png")
        val image = MockMultipartFile(
            "file",
            "image.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            Files.readAllBytes(imageFile)
        )

        mockMvc.perform(
            multipart("/image/upload")
                .file(image)
                .header("Authorization", "Bearer $token")
        ).andExpect(status().isOk)
            .andExpect(content().string("Image uploaded successfully."));

        mockMvc.perform(
                get("/image/user")
                .header("Authorization", "Bearer $token")
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.IMAGE_JPEG))
            .andReturn()
    }

}
