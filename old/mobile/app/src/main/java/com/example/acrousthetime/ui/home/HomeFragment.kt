package com.example.acrousthetime.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.acrousthetime.R
import com.example.acrousthetime.databinding.FragmentHomeBinding
import com.example.acrousthetime.model.FavorisItem
import com.example.acrousthetime.model.TypeRecherche
import com.example.acrousthetime.ui.components.calendrier.ComponentCalendrierModel
import com.example.acrousthetime.utils.DateUtils
import com.example.wezer.http.Api
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textMonth: TextView = binding.month
        val textYear: TextView = binding.year
        val textDay: TextView = binding.day
        //get the spinner from the xml with id dropdown_edts
        val spinnerEdts: Spinner = binding.dropdownEdts

        HomeViewModel.currentMonthText.observe(viewLifecycleOwner) {
            textMonth.text = it
        }

        HomeViewModel.currentYearText.observe(viewLifecycleOwner) {
            textYear.text = it
        }

        HomeViewModel.currentDayText.observe(viewLifecycleOwner) {
            textDay.text = it
        }

        // initialise les données des dates
        HomeViewModel.onUpdateDate()

        val previousDay = root.findViewById<TextView>(R.id.home_previous_day)
        val nextDay = root.findViewById<TextView>(R.id.home_next_day)

        previousDay.setOnClickListener {
            HomeViewModel.previousDay()
            val date = DateUtils.localDateTimeToDate(HomeViewModel.currentDate)

            val currentCreneaux = Api.getCreneauxFromFavorisItem(HomeViewModel.currentFavorisItem.value!!, date)

            ComponentCalendrierModel.setData(currentCreneaux)
        }

        nextDay.setOnClickListener {
            HomeViewModel.nextDay()
            val date = DateUtils.localDateTimeToDate(HomeViewModel.currentDate)

            val currentCreneaux = Api.getCreneauxFromFavorisItem(HomeViewModel.currentFavorisItem.value!!, date)

            ComponentCalendrierModel.setData(currentCreneaux)
        }

        //create a list of items for the spinner.
        try {
            val favorisTest = Api.getFavoris()

            val favItems = favorisTest.getListItem()

            // Si aucun favoris on ajoute les LP MiAR
            if (favItems.size == 0) {
                favItems.add(
                    FavorisItem(
                        "g3545",
                        "LP MiAR Groupe 2",
                        TypeRecherche.GROUPE
                    )
                )

                favItems.add(
                    FavorisItem(
                        "g3546",
                        "LP MiAR Groupe 1",
                        TypeRecherche.GROUPE
                    )
                )
            }

            //create an adapter to describe how the items are displayed, adapters are used in several places in android.
            //There are multiple variations of this, but this is the basic variant.
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                favItems
            )
            //set the spinners adapter to the previously created one.
            spinnerEdts.adapter = adapter

            spinnerEdts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val favorisItem = parent.getItemAtPosition(position) as FavorisItem

                    HomeViewModel.currentFavorisItem.value = favorisItem

                    val date = DateUtils.localDateTimeToDate(HomeViewModel.currentDate)

                    val currentCreneaux = Api.getCreneauxFromFavorisItem(favorisItem, date)

                    ComponentCalendrierModel.setData(currentCreneaux)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }

            // On récupère le favoris actuel
            val currentFavoris = HomeViewModel.currentFavorisItem.value!!

            //localDateTime en Date

            if (currentFavoris.id != "") {
                val date = DateUtils.localDateTimeToDate(HomeViewModel.currentDate)

                val currentCreneaux = Api.getCreneauxFromFavorisItem(currentFavoris, date)

                ComponentCalendrierModel.setData(currentCreneaux)
            }

        } catch (e: Exception) {
            if (e.message == "Unauthorized") {
                Api.TOKEN = ""
            }

            e.printStackTrace()
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}