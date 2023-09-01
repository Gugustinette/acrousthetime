package com.example.wezer.http

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.acrousthetime.R
import com.example.acrousthetime.bd.CrudToken
import com.example.acrousthetime.bd.Token
import com.example.acrousthetime.model.*
import com.example.acrousthetime.utils.DateUtils
import com.google.gson.Gson
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// UUID
import java.util.*
import kotlin.math.log

/**
 * API Class
 * Helper class for API calls
 */
class Api {
    companion object {
        /**
         * BASE_URL - The base URL of the API
         */
        val BASE_URL = "https://api.acrousthetime.gugustinette.com"

        /**
         * TOKEN - The token to use the API
         */
        var TOKEN = ""

        /**
         * MODE - "online" or "offline"
         * "online" - Use the API
         * "offline" - Use the local data to simulate the API
         */
        var MODE = "online"

        /**
         * CLIENT - The HTTP client
         */
        val CLIENT = OkHttpClient()

        /**
         * MEDIATYPE_JSON - The media type for JSON
         * Used for POST requests
         */
        val MEDIATYPE_JSON = "application/json; charset=utf-8".toMediaType()

        /**
         * GSON - The Gson object
         * Used to convert JSON to objects
         */
        val GSON = Gson()

        @SuppressLint("StaticFieldLeak")
        var crudToken : CrudToken? = null

        /**
         * init
         */
        fun init(context: Context) {
            crudToken = CrudToken(context)
            val token = crudToken?.getToken
            TOKEN = token?.id ?: ""
        }

        /**
          * register(nom, prenom, email, password) - Register a user to get a token for the API
          * Make a POST request to /auth/register with following data :
          * - nom : string
          * - prenom : string
          * - email : string
          * - password : string
          * @param nom The nom of the user
          * @param prenom The prenom of the user
          * @param email The email of the user
          * @param password The password of the user
          * @return True if the user is authenticated, false otherwise
          * @throws Exception
          */
         fun register(nom: String,
                      prenom: String,
                      email: String,
                      password: String
         ): Boolean {
             /**
              * Handle offline mode
              */
             if (MODE === "offline") {
                 return false
             }

             /**
              * Online mode
              */
             // Create the Map of data
             val data = mapOf(
                 "nom" to nom,
                 "prenom" to prenom,
                 "email" to email,
                 "password" to password
             )

             // Get the response body
             val request = Request.Builder()
                 .url("$BASE_URL/auth/register")
                 .post(RequestBody.create(MEDIATYPE_JSON, Gson().toJson(data)))
                 .build()

             // Execute the request
             val response = CLIENT.newCall(request).execute()

            val body = response.body?.string() ?: ""

             // Result is a JSON Object
             val result = GSON.fromJson(body, Map::class.java)

             // Get the token
             val token = result["token"] as String?
             TOKEN = token ?: ""

             // Save the token in the database
             crudToken?.deleteToken()
             crudToken?.insertToken(Token(TOKEN))

             // Return true if the token is not empty
             return token != null
         }

        /**
         * autenticate(email, password) - Authenticate a user to get a token for the API
         * Make a POST request to /auth/login with following data :
         * - email : string
         * - password : string
         * @param email The email of the user
         * @param password The password of the user
         * @return True if the user is authenticated, false otherwise
         * @throws Exception
         */
        fun authenticate(email: String,
                         password: String
        ): Boolean {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return false
            }

            /**
             * Online mode
             */
            // Create the Map of data
            val data = mapOf(
                "email" to email,
                "password" to password
            )

            println(data)

            // Get the response body
            val request = Request.Builder()
                .url("$BASE_URL/auth/authenticate")
                .post(RequestBody.create(MEDIATYPE_JSON, Gson().toJson(data)))
                .build()

            println(request)

            // Execute the request
            val response = CLIENT.newCall(request).execute()

            println(response)

            val body = response.body?.string() ?: ""

            // Result is a JSON Object
            val result = GSON.fromJson(body, Map::class.java)

            println(result)

            if (result == null) {
                return false
            }

            // Get the token
            val token = result["token"] as String?
            TOKEN = token ?: ""

            // Save the token in the database
            crudToken?.deleteToken()
            crudToken?.insertToken(Token(TOKEN))

            // Return true if the token is not empty
            return token != null
        }

        /**
         * getCreneaux() - Get the list of creneaux
         * @return List of creneaux
         * @throws Exception
         */
        fun getGroupes(): List<Groupe> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Groupe.exampleList()
            }

            /**
             * Online mode
             */
            // Make the request
            val body = getRequestFromApi("/groupe")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)

            // Create the list of groupe
            val groupes = mutableListOf<Groupe>()

            // Loop through the results
            for (groupe in result) {
                // Convert the groupe to a Map
                val groupeMap = groupe as Map<*, *>

                // Create the groupe object
                val groupeObject = Groupe(
                    groupeMap["id"] as String,
                    groupeMap["name"] as String,
                )
                groupes.add(groupeObject)
            }


            // Return the list of groupes
            return groupes
        }

        /**
         *  getGroupeInfos(id) - Get the informations of a groupe with his id
         *  @param id The id of the groupe
         *  @return The groupe object
         *  @throws Exception
         */
        fun getGroupeInfos(id: String): Groupe {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Groupe.exampleList()[0]
            }

            /**
             * Online mode
             */
            // Make the request
            val body = getRequestFromApi("/groupe/$id")

            // Parse the JSON
            val result = GSON.fromJson(body, Map::class.java)

            // Create the groupe object
            val groupe = Groupe(
                result["id"] as String,
                result["name"] as String,
            )

            // Return the groupe
            return groupe
        }

        /**
         *  getGroupeCreneauxParSemaine(id, week) - Get the list of creneaux for a groupe  by Semaine with his id
         *  @param id The id of the groupe
         *  @param week The week the creneaux are in
         *  @return The list of creneaux
         *  @throws Exception
         */
        fun getGroupeCreneauxParSemaine(id: String, week: Date): List<Creneaux> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Creneaux.exampleList()
            }

            /**
             * Online mode
             */
            week.toString()
            // Make the request
            val body = getRequestFromApi("/groupe/$id/semaine/$week")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)

            // Create the list of creneaux
            val creneaux = mutableListOf<Creneaux>()

            // Loop through the results
            for (creneau in result) {
                // Convert the creneau to a Map
                val creneauMap = creneau as Map<*, *>

                // Create the creneau object
                val creneauObject = Creneaux(
                    creneauMap["uid"] as String,
                    DateUtils.stringFormattedToDate((creneauMap["dt_start"] as String))!!,
                    DateUtils.stringFormattedToDate((creneauMap["dt_end"] as String))!!,
                    creneauMap["summary"] as String,
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    creneauMap["matiere"] as String,
                    Date()
                )
                creneaux.add(creneauObject)
            }

            // Return the list of creneaux
            return creneaux
        }

        /**
         * getGroupeCreneauxParJour(id, day) - Get the list of creneaux for a groupe  by Jour with his id
         * @param id The id of the groupe
         * @param day The day the creneaux are in
         * @return The list of creneaux
         * @throws Exception
         */
        fun getGroupeCreneauxParJour(id: String, day: Date): List<Creneaux> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Creneaux.exampleList()
            }

            /**
             * Online mode
             */
            val dateString = DateUtils.dateToStringFormatted(day)
            // Make the request
            val body = getRequestFromApi("/groupe/$id/jour/$dateString")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)
            // Create the list of creneaux
            val creneaux = mutableListOf<Creneaux>()

            // Loop through the results
            for (creneau in result) {
                // Convert the creneau to a Map
                val creneauMap = creneau as Map<*, *>

                // Create the creneau object
                val creneauObject = Creneaux(
                    creneauMap["uid"] as String,
                    DateUtils.stringFormattedToDate((creneauMap["dt_start"] as String))!!,
                    DateUtils.stringFormattedToDate((creneauMap["dt_end"] as String))!!,
                    creneauMap["summary"] as String,
                    (creneauMap["etudiant"] as List<*>).map {
                        Etudiant.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["salle"] as List<*>).map {
                        Salle.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["groupe"] as List<*>).map {
                        Groupe.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["personnel"] as List<*>).map {
                        Personnel.readFromJson(it as Map<String, *>)
                    },
                    creneauMap["matiere"] as String,
                    Date()
                )
                creneaux.add(creneauObject)
            }

            // Return the list of creneaux
            return creneaux
        }


        /**
         * getEtudiantList() - Get the list of all the etudiants
         * @return The list of etudiants
         * @throws Exception
         */
        fun getEtudiantList(): List<Etudiant> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Etudiant.exampleList()
            }

            /**
             * Online mode
             */
            // Make the request
            val body = getRequestFromApi("/etudiant")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)

            // Create the list of etudiants
            val etudiants = mutableListOf<Etudiant>()

            // Loop through the results
            for (etudiant in result) {
                // Convert the etudiant to a Map
                val etudiantMap = etudiant as Map<*, *>

                // Create the etudiant object
                val etudiantObject = Etudiant(
                    etudiantMap["id"] as String,
                    etudiantMap["name"] as String,
                       etudiantMap["last_update"] as Date,
                )
                etudiants.add(etudiantObject)
            }

            // Return the list of etudiants
            return etudiants
        }

        /**
         * getEtudiantById(id) - Get the etudiant with his id
         * @param id The id of the etudiant
         * @return The etudiant
         * @throws Exception
         */
        fun getEtudiantById(id: String): Etudiant {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Etudiant.exampleList()[0]
            }

            /**
             * Online mode
             */
            // Make the request
            val body = getRequestFromApi("/etudiant/$id")

            // Parse the JSON
            val result = GSON.fromJson(body, Map::class.java)

            // Create the etudiant object
            val etudiant = Etudiant(
                result["id"] as String,
                result["name"] as String,
                result["last_update"] as Date,
            )

            // Return the etudiant
            return etudiant
        }

        /**
         * getEtudiantCreneauxParSemaine(id, week) - Get the list of creneaux for a etudiant with his id
         * @param id The id of the etudiant
         * @param week The week the creneaux are in
         * @return The list of creneaux
         * @throws Exception
         */
        fun getEtudiantCreneauxParSemaine(id: String, week: Date): List<Creneaux> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Creneaux.exampleList()
            }

            /**
             * Online mode
             */
            week.toString()
            // Make the request
            val body = getRequestFromApi("/etudiant/$id/semaine/$week")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)

            // Create the list of creneaux
            val creneaux = mutableListOf<Creneaux>()

            // Loop through the results
            for (creneau in result) {
                // Convert the creneau to a Map
                val creneauMap = creneau as Map<*, *>

                // Create the creneau object
                val creneauObject = Creneaux(
                    creneauMap["uid"] as String,
                    DateUtils.stringFormattedToDate((creneauMap["dt_start"] as String))!!,
                    DateUtils.stringFormattedToDate((creneauMap["dt_end"] as String))!!,
                    creneauMap["summary"] as String,
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    creneauMap["matiere"] as String,
                    Date()
                )
                creneaux.add(creneauObject)
            }

            // Return the list of creneaux
            return creneaux
        }

        /**
         * getEtudiantCreneauxParJour(id, day) - Get the list of creneaux for a etudiant with his id
         * @param id The id of the etudiant
         * @param day The day the creneaux are in
         * @return The list of creneaux
         * @throws Exception
         */
        fun getEtudiantCreneauxParJour(id: String, day: Date): List<Creneaux> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Creneaux.exampleList()
            }

            /**
             * Online mode
             */
            val dateString = DateUtils.dateToStringFormatted(day)
            // Make the request
            val body = getRequestFromApi("/etudiant/$id/jour/$dateString")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)

            // Create the list of creneaux
            val creneaux = mutableListOf<Creneaux>()

            // Loop through the results
            for (creneau in result) {
                // Convert the creneau to a Map
                val creneauMap = creneau as Map<*, *>

                // Create the creneau object
                val creneauObject = Creneaux(
                    creneauMap["uid"] as String,
                    DateUtils.stringFormattedToDate((creneauMap["dt_start"] as String))!!,
                    DateUtils.stringFormattedToDate((creneauMap["dt_end"] as String))!!,
                    creneauMap["summary"] as String,
                    (creneauMap["etudiant"] as List<*>).map {
                        Etudiant.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["salle"] as List<*>).map {
                        Salle.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["groupe"] as List<*>).map {
                        Groupe.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["personnel"] as List<*>).map {
                        Personnel.readFromJson(it as Map<String, *>)
                    },
                    creneauMap["matiere"] as String,
                    Date()
                )
                creneaux.add(creneauObject)
            }

            // Return the list of creneaux
            return creneaux
        }

        /**
         * getPersonnel() - Get the list of personnel
         * @return The list of personnel
         * @throws Exception
         */
        fun getPersonnel(): List<Personnel> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Personnel.exampleList()
            }

            /**
             * Online mode
             */
            // Make the request
            val body = getRequestFromApi("/personnel")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)

            // Create the list of personnel
            val personnels = mutableListOf<Personnel>()

            // Loop through the results
            for (personnel in result) {
                // Convert the personnel to a Map
                val personnelMap = personnel as Map<*, *>

                // Create the personnel object
                val personnelObject = Personnel(
                    personnelMap["id"] as String,
                    personnelMap["name"] as String,
                    personnelMap["last_update"] as Date,
                )
                personnels.add(personnelObject)
            }

            // Return the list of personnel
            return personnels
        }

        /**
         * getPersonnelById(id) - Get the personnel with his id
         * @param id The id of the personnel
         * @return The personnel
         * @throws Exception
         */
        fun getPersonnelById(id: String): Personnel {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Personnel.exampleList()[0]
            }

            /**
             * Online mode
             */
            // Make the request
            val body = getRequestFromApi("/personnel/$id")

            // Parse the JSON
            val result = GSON.fromJson(body, Map::class.java)

            // Create the personnel object
            val personnel = Personnel(
                result["id"] as String,
                result["name"] as String,
                result["last_update"] as Date,
            )

            // Return the personnel
            return personnel
        }

        /**
         * getPersonnelCreneaux(id) - Get the list of creneaux for a personnel with his id
         * @param id The id of the personnel
         * @return The list of creneaux
         * @throws Exception
         */
        fun getPersonnelCreneaux(id: String): List<Creneaux> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Creneaux.exampleList()
            }

            /**
             * Online mode
             */
            // Make the request
            val body = getRequestFromApi("/personnel/$id/creneaux")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)

            // Create the list of creneaux
            val creneaux = mutableListOf<Creneaux>()

            // Loop through the results
            for (creneau in result) {
                // Convert the creneau to a Map
                val creneauMap = creneau as Map<*, *>

                // Create the creneau object
                val creneauObject = Creneaux(
                    creneauMap["uid"] as String,
                    DateUtils.stringFormattedToDate((creneauMap["dt_start"] as String))!!,
                    DateUtils.stringFormattedToDate((creneauMap["dt_end"] as String))!!,
                    creneauMap["summary"] as String,
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    creneauMap["matiere"] as String,
                    Date()
                )
                creneaux.add(creneauObject)
            }

            // Return the list of creneaux
            return creneaux
        }

        /**
         * getPersonnelCreneauxParJour(id, day) - Get the list of creneaux for a personnel with his id and a day
         * @param id The id of the personnel
         * @param day The day
         * @return The list of creneaux
         * @throws Exception
         */
        fun getPersonnelCreneauxParJour(id: String, day: Date): List<Creneaux> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Creneaux.exampleList()
            }

            /**
             * Online mode
             */
            val dateString = DateUtils.dateToStringFormatted(day)
            // Make the request
            val body = getRequestFromApi("/personnel/$id/jour/$dateString")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)

            // Create the list of creneaux
            val creneaux = mutableListOf<Creneaux>()

            // Loop through the results
            for (creneau in result) {
                // Convert the creneau to a Map
                val creneauMap = creneau as Map<*, *>

                // Create the creneau object
                val creneauObject = Creneaux(
                    creneauMap["uid"] as String,
                    DateUtils.stringFormattedToDate((creneauMap["dt_start"] as String))!!,
                    DateUtils.stringFormattedToDate((creneauMap["dt_end"] as String))!!,
                    creneauMap["summary"] as String,
                    (creneauMap["etudiant"] as List<*>).map {
                        Etudiant.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["salle"] as List<*>).map {
                        Salle.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["groupe"] as List<*>).map {
                        Groupe.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["personnel"] as List<*>).map {
                        Personnel.readFromJson(it as Map<String, *>)
                    },
                    creneauMap["matiere"] as String,
                    Date()
                )
                creneaux.add(creneauObject)
            }

            // Return the list of creneaux
            return creneaux
        }

        /**
         * getPersonnelCreneauxParSemaine(id, week) - Get the list of creneaux for a personnel with his id and a week
         * @param id The id of the personnel
         * @param week The week
         * @return The list of creneaux
         * @throws Exception
         */
        fun getPersonnelCreneauxParSemaine(id: String, week: Date): List<Creneaux> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Creneaux.exampleList()
            }

            /**
             * Online mode
             */
            week.toString()
            // Make the request
            val body = getRequestFromApi("/personnel/$id/semaine/$week")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)

            // Create the list of creneaux
            val creneaux = mutableListOf<Creneaux>()

            // Loop through the results
            for (creneau in result) {
                // Convert the creneau to a Map
                val creneauMap = creneau as Map<*, *>

                // Create the creneau object
                val creneauObject = Creneaux(
                    creneauMap["uid"] as String,
                    DateUtils.stringFormattedToDate((creneauMap["dt_start"] as String))!!,
                    DateUtils.stringFormattedToDate((creneauMap["dt_end"] as String))!!,
                    creneauMap["summary"] as String,
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    creneauMap["matiere"] as String,
                    Date()
                )
                creneaux.add(creneauObject)
            }

            // Return the list of creneaux
            return creneaux
        }

        fun getSalleCreneauxParJour(id: String, day: Date): List<Creneaux> {
            /**
             * Handle offline mode
             */
            if (MODE === "offline") {
                return Creneaux.exampleList()
            }

            /**
             * Online mode
             */
            val dateString = DateUtils.dateToStringFormatted(day)
            // Make the request
            val body = getRequestFromApi("/salle/$id/jour/$dateString")

            // Parse the JSON
            val result = GSON.fromJson(body, List::class.java)

            // Create the list of creneaux
            val creneaux = mutableListOf<Creneaux>()

            // Loop through the results
            for (creneau in result) {
                // Convert the creneau to a Map
                val creneauMap = creneau as Map<*, *>

                // Create the creneau object
                val creneauObject = Creneaux(
                    creneauMap["uid"] as String,
                    DateUtils.stringFormattedToDate((creneauMap["dt_start"] as String))!!,
                    DateUtils.stringFormattedToDate((creneauMap["dt_end"] as String))!!,
                    creneauMap["summary"] as String,
                    (creneauMap["etudiant"] as List<*>).map {
                        Etudiant.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["salle"] as List<*>).map {
                        Salle.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["groupe"] as List<*>).map {
                        Groupe.readFromJson(it as Map<String, *>)
                    },
                    (creneauMap["personnel"] as List<*>).map {
                        Personnel.readFromJson(it as Map<String, *>)
                    },
                    creneauMap["matiere"] as String,
                    Date()
                )
                creneaux.add(creneauObject)
            }

            // Return the list of creneaux
            return creneaux
        }



        /**
         * Search
         */
        fun search(search: String): List<SearchItem> {
            val body = getRequestFromApi("/recherche?search=$search")

            val result = GSON.fromJson(body, Map::class.java)

            val searchItems = mutableListOf<SearchItem>()

            result["etudiant"]?.let { etudiants: Any ->
                if (etudiants is List<*>) {
                    for (etudiant in etudiants) {
                        val etudiantMap = etudiant as Map<*, *>
                        val etudiantObject = SearchItem(
                            etudiantMap["id"] as String,
                            etudiantMap["name"] as String,
                            TypeRecherche.ETUDIANT,
                        )
                        searchItems.add(etudiantObject)
                    }
                }
            }

            result["personnel"]?.let { personnels: Any ->
                if (personnels is List<*>) {
                    for (personnel in personnels) {
                        val personnelMap = personnel as Map<*, *>
                        val personnelObject = SearchItem(
                            personnelMap["id"] as String,
                            personnelMap["name"] as String,
                            TypeRecherche.PERSONNEL,
                        )
                        searchItems.add(personnelObject)
                    }
                }
            }

            result["groupe"]?.let { groupes: Any ->
                if (groupes is List<*>) {
                    for (groupe in groupes) {
                        val groupeMap = groupe as Map<*, *>
                        val groupeObject = SearchItem(
                            groupeMap["id"] as String,
                            groupeMap["name"] as String,
                            TypeRecherche.GROUPE,
                        )
                        searchItems.add(groupeObject)
                    }
                }
            }

            result["salle"]?.let { salles: Any ->
                if (salles is List<*>) {
                    for (salle in salles) {
                        val salleMap = salle as Map<*, *>
                        val salleObject = SearchItem(
                            salleMap["id"] as String,
                            salleMap["name"] as String,
                            TypeRecherche.SALLE,
                        )
                        searchItems.add(salleObject)
                    }
                }
            }


            return searchItems

        }

        fun loadProfilePicture(profilePicture: ImageView?, c: Context) {
            // Ajoute le token à la requête de Picasso
            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor {
                    val newRequest = it.request().newBuilder()
                        .addHeader("Authorization", "Bearer $TOKEN")
                        .build()
                    it.proceed(newRequest)
                })
                .build()

            val picasso = Picasso.Builder(c)
                .downloader(OkHttp3Downloader(client))
                .build()

            picasso.load("$BASE_URL/image/user")
                .placeholder(R.drawable.avatar_100)
                .error(R.drawable.avatar_100)
                .into(profilePicture)
        }


        /**
         * getRequestFromApi() - Create the request object and execute it for a get request
         * @param url_path The path of the URL
         * @return The response body
         */
        fun getRequestFromApi(url_path: String): String {
            // Create the request
            val request = Request.Builder()
                .url(BASE_URL + url_path)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .build()

            // Execute the request
            val response = CLIENT.newCall(request).execute()

            println(response)

            //if error 401
            if (response.code == 401) {
                // Throw exception
                this.TOKEN = ""
                this.crudToken?.deleteToken()
                throw Exception("Unauthorized")
            }

            // Get the response body
            val body = response.body?.string()

            // Return the body
            return body ?: ""
        }

        /**
         * postRequestFromApi() - Create the request object and execute it for a post request
         * @param url_path The path of the URL
         * @param data The data to send (as a Map)
         * @return The response body
         */
        fun postRequestFromApi(url_path: String, data: Map<String, String>): String {
            // Create the request
            val request = Request.Builder()
                .url(BASE_URL + url_path)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .post(RequestBody.create(MEDIATYPE_JSON, Gson().toJson(data)))
                .build()

            // Execute the request
            val response = CLIENT.newCall(request).execute()

            println(response)

            //if error 401
            if (response.code == 401) {
                // Throw exception
                this.TOKEN = ""
                this.crudToken?.deleteToken()
                throw Exception("Unauthorized")
            }

            // Get the response body
            val body = response.body?.string()

            // Return the body
            return body ?: ""
        }

        fun deleteRequestFromApi(url_path: String, data: Map<String, String>): String {
            // Create the request
            val request = Request.Builder()
                .url(BASE_URL + url_path)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .delete(RequestBody.create(MEDIATYPE_JSON, Gson().toJson(data)))
                .build()

            // Execute the request
            val response = CLIENT.newCall(request).execute()

            println(response)

            //if error 401
            if (response.code == 401) {
                // Throw exception
                this.TOKEN = ""
                this.crudToken?.deleteToken()
                throw Exception("Unauthorized")
            }

            // Get the response body
            val body = response.body?.string()

            // Return the body
            return body ?: ""
        }

        fun getCurrentUser(): User {
            try {
                val body = getRequestFromApi("/user/moi")

                val result = GSON.fromJson(body, Map::class.java)

                println(result)

                val role = result["roles"] as String

                val user = User(
                    (result["id"] as Double).toInt(),
                    result["nom"] as String,
                    result["prenom"] as String,
                    Role.valueOf(role),
                )

                return user

            } catch (e: Exception) {
                throw Exception("Unauthorized")
            }
        }

        fun uploadProfilePicture(image: Uri,
                                 context: Context): Boolean {
            try {
                val inputStream = context.contentResolver.openInputStream(image)
                val file = File(context.cacheDir, "temp_image.jpg")
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        file.name,
                        RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    )
                    .build()

                val request = Request.Builder()
                    .url("$BASE_URL/image/upload")
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .post(requestBody)
                    .build()

                val response = CLIENT.newCall(request).execute()
                println(response)

                return response.isSuccessful
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return false
        }


        fun getFavoris(): Favoris {
            try {
                val response = getRequestFromApi("/favoris")

                if (response != "") {
                    val result = GSON.fromJson(response, Favoris::class.java)
                    println(result)

                    return result
                }

                return Favoris()
            } catch (e: Exception) {
                throw Exception("Unauthorized")
            }
        }

        fun addFavoris(id: String, type: TypeRecherche) {
            try {
                val request = postRequestFromApi("/favoris/${type.toString().lowercase(Locale.ROOT)}", mapOf("id" to id))
                println(request)
            } catch (e: Exception) {
                throw Exception("Unauthorized")
            }
        }

        fun removeFavoris(id: String, type: TypeRecherche) {
            try {
                val request = deleteRequestFromApi("/favoris/${type.toString().lowercase(Locale.ROOT)}", mapOf("id" to id))
                println(request)
            } catch (e: Exception) {
                throw Exception("Unauthorized")
            }
        }

        fun getCreneauxFromFavorisItem(currentFavoris: FavorisItem, day: Date): List<Creneaux> {

            if (currentFavoris.id == "") {
                return listOf()
            }

            if (currentFavoris.type == TypeRecherche.ETUDIANT) {
                return getEtudiantCreneauxParJour(currentFavoris.id, day)
            }

            if (currentFavoris.type == TypeRecherche.PERSONNEL) {
                return getPersonnelCreneauxParJour(currentFavoris.id, day)
            }

            if (currentFavoris.type == TypeRecherche.SALLE) {
                return getSalleCreneauxParJour(currentFavoris.id, day)
            }

            if (currentFavoris.type == TypeRecherche.GROUPE) {
                return getGroupeCreneauxParJour(currentFavoris.id, day)
            }

            return listOf()
        }
    }
}
