package com.example.acrousthetime.ui.components.semaine

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.acrousthetime.databinding.ComponentSemainesBinding
import com.example.acrousthetime.ui.home.HomeViewModel


class ComponentSemaines : Fragment() {

    private var _binding: ComponentSemainesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val componentSemaineModel =
            ViewModelProvider(this).get(ComponentSemainesModel::class.java)

        _binding = ComponentSemainesBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // create a dataset with numbers from 1 to 52
        val dataset = (1..52).toList()
        val weekList: RecyclerView = binding.recyclerWeek
        //link the adapter to the recycler view with the app context and the dataset
        weekList.adapter = SemainesAdapter(dataset)
        //link the layout manager to the recycler view
        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        weekList.layoutManager = layoutManager

        return root
    }

    fun changeTextValue(message: String) {
        val componentSemaineModel =
            ViewModelProvider(this).get(ComponentSemainesModel::class.java)
        Log.d("GusDebug", message)
        componentSemaineModel.changeTextValue(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}