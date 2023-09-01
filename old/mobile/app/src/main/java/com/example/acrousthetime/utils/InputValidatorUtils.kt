package com.example.acrousthetime.utils

class InputValidatorUtils {
    companion object {
        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPasswordValid(password: String, confirmPassword: String): Boolean {
            return password == confirmPassword && password.length >= 3
        }

        fun isNomValid(nom: String): Boolean {
            return nom.length >= 2
        }

        fun isPrenomValid(prenom: String): Boolean {
            return prenom.length >= 2
        }
    }
}