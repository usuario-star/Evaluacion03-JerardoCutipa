package com.tecsup.rincondevallejo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [Sede::class], version = 2, exportSchema = false)
abstract class SedeDatabase : RoomDatabase() {

    abstract fun sedeDao(): SedeDao

    companion object {
        @Volatile
        private var INSTANCE: SedeDatabase? = null

        fun getDatabase(context: Context): SedeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SedeDatabase::class.java,
                    "rincon_de_vallejo_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                prepopulateIfEmpty(instance)
                instance
            }
        }

        private fun prepopulateIfEmpty(db: SedeDatabase) {
            Executors.newSingleThreadExecutor().execute {
                val dao = db.sedeDao()
                if (dao.getCount() == 0) {
                    val sedesIniciales = listOf(
                        Sede(
                            nombre = "Sede Orbegoso - Trujillo Centro",
                            capacidad = 140,
                            mesas = 35,
                            especialidad = "Comida Norteña Tradicional",
                            gerente = "Jose Lopez Garcia",
                            jefePersonal = "Maria Vargas Soto",
                            latitud = -8.1102,
                            longitud = -79.0275
                        ),
                        Sede(
                            nombre = "Sede Plaza Mayor - Pizarro",
                            capacidad = 120,
                            mesas = 30,
                            especialidad = "Cabrito y Pato Guisado",
                            gerente = "Carlos Mendoza Ruiz",
                            jefePersonal = "Ana Torres Diaz",
                            latitud = -8.1128,
                            longitud = -79.0298
                        ),
                        Sede(
                            nombre = "Sede El Recreo - Av. España",
                            capacidad = 160,
                            mesas = 40,
                            especialidad = "Pescados y Mariscos",
                            gerente = "Luis Paredes Silva",
                            jefePersonal = "Rosa Chavez Leon",
                            latitud = -8.1085,
                            longitud = -79.0235
                        ),
                        Sede(
                            nombre = "Sede San Andrés - Av. Larco",
                            capacidad = 100,
                            mesas = 25,
                            especialidad = "Criolla Tradicional",
                            gerente = "Elena Castro Vega",
                            jefePersonal = "Jorge Rios Rojas",
                            latitud = -8.1162,
                            longitud = -79.0318
                        )
                    )
                    sedesIniciales.forEach { dao.insert(it) }
                }
            }
        }
    }
}
