package com.example.acrousthetime

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wezer.http.Api


class LoginActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setContentView(R.layout.activity_login)


        // get views
        val email = findViewById<EditText>(R.id.login_email_input)
        val password = findViewById<EditText>(R.id.login_password_input)
        val errorMessage = findViewById<TextView>(R.id.login_error_message)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<TextView>(R.id.login_create_account)

        // set listeners
        loginButton.setOnClickListener {
            val isConnected = Api.authenticate(email.text.toString(), password.text.toString())

            if (isConnected) {
                //Lancer la main activity
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                // Toast error message
                Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
            }
        }

        errorMessage.setOnClickListener {
            // Clear error message
            errorMessage.text = ""
        }

        registerButton.setOnClickListener {
            //Lancer la register activity
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}