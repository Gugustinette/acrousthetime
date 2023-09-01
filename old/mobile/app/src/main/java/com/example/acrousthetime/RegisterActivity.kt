package com.example.acrousthetime

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.acrousthetime.utils.InputValidatorUtils.Companion.isEmailValid
import com.example.acrousthetime.utils.InputValidatorUtils.Companion.isNomValid
import com.example.acrousthetime.utils.InputValidatorUtils.Companion.isPasswordValid
import com.example.acrousthetime.utils.InputValidatorUtils.Companion.isPrenomValid
import com.example.wezer.http.Api


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setContentView(R.layout.activity_register)

        // views

        //inputs
        val email = findViewById<EditText>(R.id.register_email_input)
        val password = findViewById<EditText>(R.id.register_password_input)
        val nom = findViewById<EditText>(R.id.register_nom_input)
        val prenom = findViewById<EditText>(R.id.register_prenom_input)
        val confirmPassword = findViewById<EditText>(R.id.register_confirm_password_input)

        //title
        val emailTitle = findViewById<TextView>(R.id.register_email_title)
        val passwordTitle = findViewById<TextView>(R.id.register_password_title)
        val nomTitle = findViewById<TextView>(R.id.register_nom_title)
        val prenomTitle = findViewById<TextView>(R.id.register_prenom_title)
        val confirmPasswordTitle = findViewById<TextView>(R.id.register_confirm_password_title)

        //buttons
        val registerButton = findViewById<Button>(R.id.register_button)
        val loginRedirect = findViewById<TextView>(R.id.register_login_redirect)

        // listeners
        registerButton.setOnClickListener {
            // Reset title colors
            emailTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            passwordTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            nomTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            prenomTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            confirmPasswordTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))

            // check if each input is valid
            if (!isEmailValid(email.text.toString())) {
                emailTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                return@setOnClickListener
            }

            if (!isPasswordValid(password.text.toString(), confirmPassword.text.toString())) {
                passwordTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                confirmPasswordTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                return@setOnClickListener
            }

            if (!isNomValid(nom.text.toString())) {
                nomTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                return@setOnClickListener
            }

            if (!isPrenomValid(prenom.text.toString())) {
                prenomTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                return@setOnClickListener
            }

            // register user
            val isRegistered = Api.register(
                nom.text.toString(),
                prenom.text.toString(),
                email.text.toString(),
                password.text.toString()
            )

            if (isRegistered) {
                // redirect to login activity
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                // set title color to red
                emailTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                passwordTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                nomTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                prenomTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                confirmPasswordTitle.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
            }
        }

        loginRedirect.setOnClickListener {
            // redirect to login activity
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}