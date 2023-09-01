package fr.iutna.lpmiar.td1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.boot.runApplication
// Swagger
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
	info = Info(
		title = "TD1",
		version = "1.0.0",
		description = "TD1",
		contact = Contact(
			name = "Augustin Mercier",
			email = "augustin.mercier@etu.univ-nantes.fr"
		)
	),
)
class Td1Application

fun main(args: Array<String>) {
	runApplication<Td1Application>(*args)
}
