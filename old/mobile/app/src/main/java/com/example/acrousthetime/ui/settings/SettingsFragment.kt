package com.example.acrousthetime.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.acrousthetime.R
import com.example.acrousthetime.utils.InputValidatorUtils
import com.example.wezer.http.Api
import java.io.File


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //layout
        val layout = inflater.inflate(R.layout.fragment_settings, container, false)

        //views
        val profilePicture = layout.findViewById<ImageView>(R.id.settings_pdp)
        val editPicture = layout.findViewById<Button>(R.id.settings_edit_pdp)
        val deletePicture = layout.findViewById<Button>(R.id.settings_delete_pdp)
        val saveProfileEdit = layout.findViewById<Button>(R.id.settings_edit_account_button)
        val nomInput = layout.findViewById<EditText>(R.id.settings_nom_input)
        val prenomInput = layout.findViewById<EditText>(R.id.settings_prenom_input)

        val nomTitle = layout.findViewById<TextView>(R.id.settings_nom_title)
        val prenomTitle = layout.findViewById<TextView>(R.id.settings_prenom_title)


        try {
            Api.loadProfilePicture(profilePicture, requireContext())
            Api.getCurrentUser().let {
                nomInput.setText(it.nom)
                prenomInput.setText(it.prenom)
            }
        }catch (e: Exception) {
            if (e.message == "Unauthorized") {
                Api.TOKEN = ""
            }
        }


        //listeners
        saveProfileEdit.setOnClickListener {
            if (!InputValidatorUtils.isNomValid(nomInput.toString())) {
                nomTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            if (!InputValidatorUtils.isPrenomValid(prenomInput.toString())) {
                prenomTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
        }

        editPicture.setOnClickListener {
            // ouvrir la galerie et récupère la photo
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                startActivityForResult(it, 0)
            }
        }

        // Inflate the layout for this fragment
        return layout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // récupérer l'image
        val uri = data?.data

        println(uri)

        if (uri != null) {
            val profilePicture = view?.findViewById<ImageView>(R.id.settings_pdp)

            profilePicture?.setImageURI(uri)

            // envoyer l'image au serveur
            Api.uploadProfilePicture(uri, requireContext())

            Toast.makeText(requireContext(), "Photo de profil mise à jour", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {

            }
    }
}