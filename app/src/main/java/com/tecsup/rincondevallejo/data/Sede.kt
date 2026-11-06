package com.tecsup.rincondevallejo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "sedes")
data class Sede(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val capacidad: Int,
    val mesas: Int,
    val especialidad: String,
    val gerente: String,
    val jefePersonal: String,
    val latitud: Double,
    val longitud: Double
) : Serializable
