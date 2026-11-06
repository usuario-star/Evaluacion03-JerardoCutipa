package com.tecsup.rincondevallejo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tecsup.rincondevallejo.data.Sede
import com.tecsup.rincondevallejo.data.SedeDatabase
import com.tecsup.rincondevallejo.repository.SedeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SedeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SedeRepository
    val allSedes: LiveData<List<Sede>>

    init {
        val sedeDao = SedeDatabase.getDatabase(application).sedeDao()
        repository = SedeRepository(sedeDao)
        allSedes = repository.allSedes
    }

    fun insert(sede: Sede) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(sede)
    }

    fun update(sede: Sede) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(sede)
    }

    fun delete(sede: Sede) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(sede)
    }
}
