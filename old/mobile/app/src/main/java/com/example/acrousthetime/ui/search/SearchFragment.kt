package com.example.acrousthetime.ui.search

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import androidx.core.widget.addTextChangedListener
import com.example.acrousthetime.R
import com.example.acrousthetime.model.Favoris
import com.example.acrousthetime.model.SearchItem
import com.example.acrousthetime.model.TypeRecherche
import com.example.acrousthetime.model.adapter.SearchItemAdapter
import com.example.wezer.http.Api

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (container == null) {
            return null
        }

        val viewLayout = inflater.inflate(R.layout.fragment_search, container, false)

        val searchList = viewLayout.findViewById<ListView>(R.id.search_list)
        val searchInput = viewLayout.findViewById<EditText>(R.id.search_bar)
        val searchItemList: MutableList<SearchItem> = mutableListOf()

        val searchItemAdapter = context?.let { SearchItemAdapter(it, searchItemList) }

        var favoris = Favoris()

        try {
           favoris = Api.getFavoris()
        } catch (e: Exception) {
            if (e.message == "Unauthorized") {
                Api.TOKEN = ""
            }
        }

        searchInput.addTextChangedListener {text: Editable? ->
            val currentSearch = text.toString()

            if (currentSearch.isBlank() || currentSearch.isEmpty() || currentSearch.length < 2) {
                // Affichage de rien
                searchItemList.clear()
                searchItemAdapter?.notifyDataSetChanged()
                return@addTextChangedListener
            }

            if (currentSearch.length > 2) {
                // Appel API
                try {
                    val liste = Api.search(currentSearch)

                    liste.forEach {
                        if (favorisContains(favoris, it)) {
                            it.isFavoris = true
                        }
                    }

                    searchItemList.clear()
                    searchItemList.addAll(liste)
                    searchItemAdapter?.notifyDataSetChanged()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return@addTextChangedListener
            }
        }

        searchList?.adapter = searchItemAdapter

        // Inflate the layout for this fragment
        return viewLayout
    }

    private fun favorisContains(favoris: Favoris, it: SearchItem): Boolean {
        if (it.type == TypeRecherche.ETUDIANT) {
            return favoris.etudiant.any { etudiant -> etudiant.id == it.id }
        }

        if (it.type == TypeRecherche.PERSONNEL) {
            return favoris.personnel.any { personnel -> personnel.id == it.id }
        }

        if (it.type == TypeRecherche.SALLE) {
            return favoris.salle.any { salle -> salle.id == it.id }
        }

        if (it.type == TypeRecherche.GROUPE) {
            return favoris.groupe.any { groupe -> groupe.id == it.id }
        }

        return false
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() {
            SearchFragment().apply {

            }
        }

    }
}