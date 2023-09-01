package com.example.acrousthetime

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.acrousthetime.databinding.ActivityMainBinding
import com.example.wezer.http.Api
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)

        Api.init(this.applicationContext)


        if (Api.TOKEN == "") {
            //Lancer la login activity
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        // Hide action bar
        val actionBar = supportActionBar
        actionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_notifications,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // check if user is connected on resume
    override fun onResume() {
        super.onResume()
        if (Api.TOKEN == "") {
            //Lancer la login activity
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        println(Api.TOKEN)
        if (Api.TOKEN == "") {
            //Lancer la login activity
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }

}