package com.tecsup.rincondevallejo.repository

import androidx.lifecycle.LiveData
import com.tecsup.rincondevallejo.data.Sede
import com.tecsup.rincondevallejo.data.SedeDao

class SedeRepository(private val sedeDao: SedeDao) {

    val allSedes: LiveData<List<Sede>> = sedeDao.getAllSedes()

    fun insert(sede: Sede) {
        sedeDao.insert(sede)
    }

    fun update(sede: Sede) {
        sedeDao.update(sede)
    }

    fun delete(sede: Sede) {
        sedeDao.delete(sede)
    }
}
