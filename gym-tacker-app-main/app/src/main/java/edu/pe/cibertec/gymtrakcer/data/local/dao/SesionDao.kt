package edu.pe.cibertec.gymtrakcer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.pe.cibertec.gymtrakcer.data.local.entity.SesionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SesionDao{
    @Insert
    suspend fun insert (sesion: SesionEntity): Long
    @Update
    suspend fun update(sesion: SesionEntity)
    @Delete
    suspend fun delete(sesion: SesionEntity)
    @Query("SELECT * FROM sesiones ORDER BY fecha DESC")
    fun getAllSessiones(): Flow<List<SesionEntity>>
    @Query("SELECT * FROM sesiones WHERE id= :id")
    suspend fun getSesionsById(id: Int): SesionEntity?
    @Query("SELECT * FROM sesiones WHERE fecha >= :fechaInicio AND fecha<= :fechaFin ORDER BY fecha DESC")
    fun getSesionesByRangeFecha(fechaInicio: Long, fechaFin: Long): Flow<List<SesionEntity>>
    @Query("DELETE FROM sesiones WHERE id = :id")
    suspend fun deleteSesionById(id: Int)
}