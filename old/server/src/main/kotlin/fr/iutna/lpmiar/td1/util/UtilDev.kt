package fr.iutna.lpmiar.td1.util

class UtilDev {
    companion object {
        fun printRed(text: String) {
            println("\u001B[31m$text\u001B[0m")
        }
    }
}