package com.tecsup.rincondevallejo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SedeDao {

    @Query("SELECT * FROM sedes ORDER BY id DESC")
    fun getAllSedes(): LiveData<List<Sede>>

    @Query("SELECT COUNT(*) FROM sedes")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sede: Sede)

    @Update
    fun update(sede: Sede)

    @Delete
    fun delete(sede: Sede)
}
