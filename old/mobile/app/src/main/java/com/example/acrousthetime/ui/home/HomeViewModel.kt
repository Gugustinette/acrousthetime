package com.example.acrousthetime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.acrousthetime.model.FavorisItem
import com.example.acrousthetime.model.TypeRecherche
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import java.util.*

class HomeViewModel : ViewModel() {

    //get the current year, month, day
    val current = LocalDateTime.now()
    val currentYear = current.year
    val currentMonth = current.month
    val currentDay = current.dayOfMonth
    val currentDayOfWeek = current.dayOfWeek

    //array adapter for the spinner
    private val _currentEdts = MutableLiveData<Int>().apply {
        value = 0
    }

    val edts: LiveData<Int> = _currentEdts

    private val _currentMonth = MutableLiveData<String>().apply {
        value = "$currentMonth"
    }

    val month: LiveData<String> = _currentMonth

    private val _currentDay = MutableLiveData<String>().apply {
        value = "$currentDayOfWeek $currentDay"
    }

    val day: LiveData<String> = _currentDay

    private val _currentYear = MutableLiveData<String>().apply {
        value = "$currentYear"
    }

    val year: LiveData<String> = _currentYear

    companion object {
        var currentDate = LocalDateTime.now() //remplacer par la date du jour sélectionné
        var currentEdt = 1 //remplacer par l'edt sélectionné (au moins l'id et le type (groupe/salle/personnel/etudiants))

        var currentWeekOfYear: MutableLiveData<Int> = MutableLiveData(getWeekNumberFromDate(currentDate))
        var currentDayText: MutableLiveData<String> = MutableLiveData("")
        var currentMonthText: MutableLiveData<String> = MutableLiveData("")
        var currentYearText: MutableLiveData<String> = MutableLiveData("")

        var currentFavorisItem: MutableLiveData<FavorisItem> = MutableLiveData(FavorisItem("", "", TypeRecherche.PERSONNEL))

        fun getWeekNumberFromDate(dateTime: LocalDateTime): Int {
            val weekFields = WeekFields.of(Locale.getDefault())
            val weekNumber = dateTime.get(weekFields.weekOfWeekBasedYear())
            return weekNumber
        }

        fun getDateFromWeekNumber(weekNumber: Int): LocalDateTime? {
            val weekFields = WeekFields.of(Locale.getDefault())
            val firstDayOfWeek = LocalDateTime.now().with(weekFields.weekOfYear(), weekNumber.toLong()).with(weekFields.dayOfWeek(), 1)
            return firstDayOfWeek
        }

        fun previousDay() {
            currentDate = currentDate.minusDays(1)
            onUpdateDate()
        }

        fun nextDay() {
            currentDate = currentDate.plusDays(1)
            onUpdateDate()
        }

        fun onUpdateDate() {
            currentWeekOfYear.value = getWeekNumberFromDate(currentDate)
            //get current day of week with translation of local
            val day = currentDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
            //set the first letter to uppercase
            currentDayText.value = day[0].uppercaseChar() + day.substring(1) + " " + currentDate.dayOfMonth
            currentMonthText.value = currentDate.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
            currentYearText.value = currentDate.year.toString()
        }

        fun setWeekNumber(weekNumber: Int) {
            currentWeekOfYear.value = weekNumber
            currentDate = getDateFromWeekNumber(weekNumber)!!
            onUpdateDate()
        }

        fun setFavorisItem(favorisItem: FavorisItem) {
            currentFavorisItem.value = favorisItem
        }
    }
}