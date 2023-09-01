package com.example.acrousthetime.ui.components.calendrier

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.acrousthetime.R
import com.example.acrousthetime.model.Creneaux
import java.util.Calendar.HOUR


class CalendrierAdapter(private val dataSet: List<Creneaux>) :
    RecyclerView.Adapter<CalendrierAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val intituleCours: TextView = view.findViewById(R.id.intituleCours)
        val nomSalle: TextView = view.findViewById(R.id.nomSalle)
        val nomProf: TextView = view.findViewById(R.id.nomProf)
        val horaire: TextView = view.findViewById(R.id.horaire)
        val creneauHolder = view.findViewById<ConstraintLayout>(R.id.creneau)

        init {


        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cours_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.intituleCours.text = dataSet[position].summary.replaceAfter("-", "")+"-"+dataSet[position].matiere
        // Add 2 hours to dates
        dataSet[position].dt_start.hours += 2
        dataSet[position].dt_end.hours += 2

        var nomSalle = ""
        dataSet[position].salle.forEach {
            nomSalle += it.name+" "
        }
        viewHolder.nomSalle.text = nomSalle
        var nomProf =""
        dataSet[position].personnel.forEach {
            nomProf += it.name+" "
        }
        viewHolder.nomProf.text = nomProf
        // split the date to get HH:MM format
        var horaire = dataSet[position].dt_start.hours.toString()+":"
        if (dataSet[position].dt_start.minutes.toString().length == 1) {
            horaire += "0"+dataSet[position].dt_start.minutes.toString()
        } else {
            horaire += dataSet[position].dt_start.minutes.toString()
        }
        horaire += " - "+dataSet[position].dt_end.hours.toString()+":"
        if (dataSet[position].dt_end.minutes.toString().length == 1) {
            horaire += "0"+dataSet[position].dt_end.minutes.toString()
        } else {
            horaire += dataSet[position].dt_end.minutes.toString()
        }
        viewHolder.horaire.text = horaire

        // Set height of the creneau
        val params = viewHolder.creneauHolder.layoutParams
        params.height = (dataSet[position].dt_end.hours - dataSet[position].dt_start.hours)*200
        viewHolder.creneauHolder.layoutParams = params
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
