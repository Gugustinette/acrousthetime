package com.example.acrousthetime.ui.components.calendrier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.acrousthetime.model.Creneaux
import com.example.acrousthetime.ui.home.HomeViewModel.Companion.currentEdt

class ComponentCalendrierModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    //use the api to get the a list of creneaux using currentDate and currentEdt
    private val _listCreneaux = MutableLiveData<Creneaux>().apply {
        //faire une verif du type d'emploi du temps (groupe/salle/personnel/etudiants)
    }


    val text: LiveData<String> = _text
    fun changeTextValue(message: String) {
        _text.value = message
    }

    companion object {


        var dataSet: MutableLiveData<List<Creneaux>> = MutableLiveData<List<Creneaux>>(emptyList())
        fun setData(currentCreneaux: List<Creneaux>) {
            dataSet.value = currentCreneaux
        }
    }
}
