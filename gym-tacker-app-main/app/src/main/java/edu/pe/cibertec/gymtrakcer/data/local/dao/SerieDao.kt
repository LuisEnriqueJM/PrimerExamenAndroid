package edu.pe.cibertec.gymtrakcer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.pe.cibertec.gymtrakcer.data.local.entity.SerieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SerieDao{
    @Insert
    suspend fun insert(serie: SerieEntity): Long
    @Update
    suspend fun update (serie: SerieEntity)
    @Delete
    suspend fun delete(serie: SerieEntity)
    @Insert
    suspend fun  insertAll(series: List<SerieEntity>)
    @Query("SELECT * From series WHERE sesionId= :sesionId ORDER BY orden ASC")
    fun getSeriesBySesion(sesionId: Int): Flow<List<SerieEntity>>
    @Query("""
        SELECT * FROM series
        WHERE ejercicioId = :ejercicioId
        ORDER BY pesoKg DESC
        LIMIT 1
    """)
    suspend fun getRecordPersonal(ejercicioId: Int): SerieEntity?
    @Query("DELETE FROM series WHERE sesionId= :sesionId")
    suspend fun deleteSeriesBySesion(sesionId: Int)
}
