package com.example.acrousthetime.ui.components.calendrier

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.acrousthetime.databinding.ComponentCalendrierBinding
import com.example.acrousthetime.databinding.ComponentSemainesBinding
import com.example.acrousthetime.model.Creneaux


class ComponentCalendrier : Fragment() {

    private var _binding: ComponentCalendrierBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val componentCalendrierModel =
            ViewModelProvider(this)[ComponentCalendrierModel::class.java]

        _binding = ComponentCalendrierBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val calendrier: RecyclerView  = binding.edt

        calendrier.adapter = CalendrierAdapter(ComponentCalendrierModel.dataSet.value!!)
        calendrier.layoutManager = LinearLayoutManager(requireContext())

        ComponentCalendrierModel.dataSet.observeForever {
            calendrier.adapter = CalendrierAdapter(it)
        }


        return root
    }


    fun changeTextValue(message: String) {
        val componentSemaineModel =
            ViewModelProvider(this).get(ComponentCalendrierModel::class.java)
        Log.d("GusDebug", message)
        componentSemaineModel.changeTextValue(message)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}