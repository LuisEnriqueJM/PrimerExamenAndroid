package edu.pe.cibertec.gymtrakcer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import edu.pe.cibertec.gymtrakcer.data.local.entity.EjercicioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EjercicioDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ejercicio: EjercicioEntity): Long

    @Update
    suspend fun update(ejercicio: EjercicioEntity)

    @Delete
    suspend fun delete(ejercicio: EjercicioEntity)

    @Query("SELECT * FROM ejercicios ORDER BY nombre ASC")
    fun getAllEjercicios(): Flow<List<EjercicioEntity>>

    @Query("SELECT * FROM ejercicios WHERE id = :id")
    suspend fun getEjercicioById(id: Int): EjercicioEntity?

    @Query("SELECT * FROM ejercicios WHERE grupoMuscular = :grupo ORDER BY nombre ASC")
    fun getEjericiosByGrupo(grupo:String): Flow<List<EjercicioEntity>>

}