package com.example.acrousthetime.model.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.acrousthetime.R
import com.example.acrousthetime.model.SearchItem
import com.example.acrousthetime.model.TypeRecherche
import com.example.wezer.http.Api

//Array adapter for the search items

class SearchItemAdapter(context: Context, searchItems: List<SearchItem>)
    : ArrayAdapter<SearchItem>(context, 0, searchItems) {


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.search_item_layout, parent, false)

        val searchItem = getItem(position)!!

        val name = view.findViewById<TextView>(R.id.search_name)
        val icon = view.findViewById<ImageView>(R.id.search_icon)
        val favoris = view.findViewById<ImageView>(R.id.search_favoris)

        name.text = searchItem.name

        //switch case searchItem
        when (searchItem.type) {
            TypeRecherche.GROUPE -> {
                icon.setImageDrawable(context.getDrawable(R.drawable.ic_group_24))
            }
            TypeRecherche.ETUDIANT -> {
                icon.setImageDrawable(context.getDrawable(R.drawable.ic_student_24))
            }
            TypeRecherche.PERSONNEL -> {
                icon.setImageDrawable(context.getDrawable(R.drawable.ic_personnel_24))
            }
            TypeRecherche.SALLE -> {
                icon.setImageDrawable(context.getDrawable(R.drawable.ic_class_24))
            }
        }

        icon.setColorFilter(context.getColor(R.color.darkGrey))

        if (searchItem.isFavoris) {
            favoris.setColorFilter(context.getColor(R.color.red))
        } else {
            favoris.setColorFilter(context.getColor(R.color.darkGrey))
        }

        favoris.setOnClickListener {
            searchItem.isFavoris = !searchItem.isFavoris
            if (searchItem.isFavoris) {
                favoris.setColorFilter(context.getColor(R.color.red))
            } else {
                favoris.setColorFilter(context.getColor(R.color.darkGrey))
            }

            if (searchItem.isFavoris) {
                Api.addFavoris(searchItem.id, searchItem.type)
            } else {
                Api.removeFavoris(searchItem.id, searchItem.type)
            }
        }

        return view
    }
}