package com.example.acrousthetime.ui.components.semaine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ComponentSemainesModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is component semaine Fragment"
    }
    val text: LiveData<String> = _text
    fun changeTextValue(message: String) {
        _text.value = message
    }
}
