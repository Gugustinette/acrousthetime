package fr.iutna.lpmiar.td1.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object{

        const val FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        // Conversion string à date
        fun stringToDate(date: String): Date {
            // vérification du format de la date de java avec les heures
            val format = SimpleDateFormat(FORMAT)
            if (format.parse(date) != null)
                return format.parse(date)
            println("Erreur de conversion de la date avec " + date)
            return Date()
        }

    }
}