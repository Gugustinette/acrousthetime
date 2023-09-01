package fr.iutna.lpmiar.td1

import com.google.gson.GsonBuilder
import fr.iutna.lpmiar.td1.dtos.AuthenticationRequest
import fr.iutna.lpmiar.td1.dtos.FavorisRequestId
import fr.iutna.lpmiar.td1.model.Etudiant
import fr.iutna.lpmiar.td1.model.Personnel
import fr.iutna.lpmiar.td1.model.Role
import fr.iutna.lpmiar.td1.model.User
import fr.iutna.lpmiar.td1.repositories.UserRepository
import fr.iutna.lpmiar.td1.services.AuthenticationService
import fr.iutna.lpmiar.td1.services.EtudiantService
import fr.iutna.lpmiar.td1.services.PersonnelService
import fr.iutna.lpmiar.td1.util.DateUtil
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestFavorisMock {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @Autowired
    private lateinit var etudiantService: EtudiantService

    @Autowired
    private lateinit var personnelService: PersonnelService

    @Autowired
    private lateinit var userRepository: UserRepository


    private var gson = GsonBuilder().setDateFormat(DateUtil.FORMAT).create()

    private lateinit var token: String

    @Test
    @BeforeAll
    fun setupToken() {
        val user = User("test", "test", "test@test.com", BCryptPasswordEncoder().encode("test"), Role.ADMIN)
        user.id = 1
        userRepository.save(user)
        token = authenticationService.authenticate(
            AuthenticationRequest("test@test.com", "test")
        ).token
    }


    /**
     * getToken
     * Register a new user and return the token in body
     */
    fun getToken(): String {
        return token
    }


    @Test
    @Order(1)
    fun getEmptyFavoris() {
        val token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/favoris")
                .header("Authorization", "Bearer $token")
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.etudiant").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.salle").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.personnel").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.groupe").isEmpty)
    }

    @Test
    @Order(2)
    fun addFavoris() {
        val token = getToken()

        val personnel = Personnel(
            "p1010",
            "oula gang",
        )

        val etudiant = Etudiant(
            "e1010",
            "oula gangito",
        )

        val etudiant2 = Etudiant(
            "e1011",
            "oulatito gangito",
        )

        personnelService.save(personnel)
        etudiantService.save(etudiant)
        etudiantService.save(etudiant2)

        var json = gson.toJson(
            FavorisRequestId("e1010")
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/favoris/etudiant")
                .header("Authorization", "Bearer $token")
                .contentType("application/json")
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)

        json = gson.toJson(
            FavorisRequestId("p1010")
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/favoris/personnel")
                .header("Authorization", "Bearer $token")
                .contentType("application/json")
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)

        json = gson.toJson(
            FavorisRequestId("e1011")
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/favoris/etudiant")
                .header("Authorization", "Bearer $token")
                .contentType("application/json")
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Order(3)
    fun getAllFavoris() {
        val token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/favoris")
                .header("Authorization", "Bearer $token")
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.etudiant.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.salle").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.personnel.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.groupe").isEmpty)
    }


    @Test
    @Order(4)
    fun deleteEtudiantPersonnelFavoris() {
        val token = getToken()

        var json = gson.toJson(
            FavorisRequestId("e1010")
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/favoris/etudiant")
                .header("Authorization", "Bearer $token")
                .contentType("application/json")
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)

        json = gson.toJson(
            FavorisRequestId("p1010")
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/favoris/personnel")
                .header("Authorization", "Bearer $token")
                .contentType("application/json")
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Order(5)
    fun getAllFavorisAfterDelete() {
        val token = getToken()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/favoris")
                .header("Authorization", "Bearer $token")
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.etudiant.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.salle").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.personnel").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.groupe").isEmpty)
    }

}