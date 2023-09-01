package fr.iutna.lpmiar.td1.components

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Classe appelée au démarrage de l'application
 */
@Profile("!test")
@ConditionalOnProperty(
    prefix = "command.line.runner",
    value = arrayOf("enabled"),
    havingValue = "true",
    matchIfMissing = true
)
@Component
class CommandLineTaskExecutor : CommandLineRunner {

    override fun run(vararg args: String?) {
        println("Démarrage de l'API")
    }

}