package com.example.acrousthetime.ui.components.semaine

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.acrousthetime.R
import com.example.acrousthetime.ui.components.calendrier.ComponentCalendrierModel
import com.example.acrousthetime.ui.home.HomeViewModel
import com.example.acrousthetime.ui.home.HomeViewModel.Companion.getWeekNumberFromDate
import com.example.acrousthetime.utils.DateUtils
import com.example.wezer.http.Api


class SemainesAdapter(private val dataSet: List<Int>) :
    RecyclerView.Adapter<SemainesAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weekButton: Button = view.findViewById(R.id.weekButton)

        /*
        init {
            // Define click listener for the ViewHolder's View.
            weekButton.setOnClickListener {

            }
        }
        */
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.semaines_layout, viewGroup, false)

        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val weekNumber = dataSet[position]
        viewHolder.weekButton.text = weekNumber.toString()

        val currentWeekNumber = getWeekNumberFromDate(HomeViewModel.currentDate)
        val context = viewHolder.itemView.context

        if (weekNumber == currentWeekNumber) {
            viewHolder.weekButton.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            viewHolder.weekButton.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            viewHolder.weekButton.setBackgroundColor(ContextCompat.getColor(context, R.color.lessLightGrey))
            viewHolder.weekButton.setTextColor(ContextCompat.getColor(context, R.color.lightGrey))
        }

        viewHolder.weekButton.setOnClickListener {
            HomeViewModel.setWeekNumber(weekNumber)
            notifyDataSetChanged()
            val date = DateUtils.localDateTimeToDate(HomeViewModel.currentDate)

            val currentCreneaux = Api.getCreneauxFromFavorisItem(HomeViewModel.currentFavorisItem.value!!, date)

            ComponentCalendrierModel.setData(currentCreneaux)
        }

        HomeViewModel.currentWeekOfYear.observeForever {
            if (it == weekNumber) {
                viewHolder.weekButton.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                viewHolder.weekButton.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                viewHolder.weekButton.setBackgroundColor(ContextCompat.getColor(context, R.color.lessLightGrey))
                viewHolder.weekButton.setTextColor(ContextCompat.getColor(context, R.color.lightGrey))
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
