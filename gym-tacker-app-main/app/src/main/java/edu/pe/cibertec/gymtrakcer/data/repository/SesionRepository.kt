package edu.pe.cibertec.gymtrakcer.data.repository

import edu.pe.cibertec.gymtrakcer.data.local.dao.SerieDao
import edu.pe.cibertec.gymtrakcer.data.local.dao.SesionDao
import edu.pe.cibertec.gymtrakcer.data.local.entity.SerieEntity
import edu.pe.cibertec.gymtrakcer.data.local.entity.SesionEntity
import kotlinx.coroutines.flow.Flow

class SesionRepository(
    private val serieDao: SerieDao,
    private val sesionDao: SesionDao
) {

    // SESION
    fun getAllSesiones(): Flow<List<SesionEntity>>{
        return  sesionDao.getAllSessiones()
    }

    suspend fun getSesionById(id: Int): SesionEntity?{
        return sesionDao.getSesionsById(id)
    }

    fun getSesionesByRangeFecha(fechaInicio: Long, fechaFin:Long): Flow<List<SesionEntity>>{
        return  sesionDao.getSesionesByRangeFecha(fechaInicio, fechaFin)
    }

    suspend fun insertSesion(sesion: SesionEntity): Long{
        return  sesionDao.insert(sesion)
    }

    suspend fun updateSesion(sesion: SesionEntity){
       return sesionDao.update(sesion)
    }
    suspend fun  deleteSesion(sesion: SesionEntity){
       return sesionDao.delete(sesion)
    }

    // SERIES
    suspend fun insertSerie(serie: SerieEntity): Long{
        return  serieDao.insert(serie)
    }

    suspend fun update (serie: SerieEntity){
        return  serieDao.update(serie)
    }

    suspend fun delete(serie: SerieEntity){
        return  serieDao.delete(serie)
    }

    suspend fun  insertAll(series: List<SerieEntity>){
        return  serieDao.insertAll(series)
    }

    fun getSeriesBySesion(sesionId: Int): Flow<List<SerieEntity>> {
        return  serieDao.getSeriesBySesion(sesionId)
    }

    suspend fun getRecordPersonal(ejercicioId: Int): SerieEntity?{
        return  serieDao.getRecordPersonal(ejercicioId)
    }

    suspend fun deleteSeriesBySesion(sesionId: Int){
        return  serieDao.deleteSeriesBySesion(sesionId)
    }
    // COMPOSICIONES
    suspend fun guardarSesionCompleta(sesion: SesionEntity, series: List<SerieEntity>): Long{
        val sesionId = sesionDao.insert(sesion)
        val seriesConSesionID = series.map {
            it.copy(sesionId = sesionId.toInt())
        }
        serieDao.insertAll(seriesConSesionID)
        return sesionId
    }
    suspend fun elimianrSerieCompleta(sesionID: Int){
        serieDao.deleteSeriesBySesion(sesionID)
        sesionDao.deleteSesionById(sesionID)
    }
}