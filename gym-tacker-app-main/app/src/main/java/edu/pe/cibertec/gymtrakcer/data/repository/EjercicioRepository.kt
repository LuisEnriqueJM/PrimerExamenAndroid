package edu.pe.cibertec.gymtrakcer.data.repository

import edu.pe.cibertec.gymtrakcer.data.local.dao.EjercicioDao
import edu.pe.cibertec.gymtrakcer.data.local.entity.EjercicioEntity
import kotlinx.coroutines.flow.Flow

class EjercicioRepository(private val ejercicioDao: EjercicioDao){

    fun getAllEjercicios(): Flow<List<EjercicioEntity>>{
        return  ejercicioDao.getAllEjercicios()
    }

    fun getEjerciciosByGrupo(grupo:String): Flow<List<EjercicioEntity>>{
        return ejercicioDao.getEjericiosByGrupo(grupo)
    }

    suspend fun getEjericicioById(id:Int): EjercicioEntity? {
        return getEjericicioById(id)
    }

    suspend fun insertEjercicio(ejercicio: EjercicioEntity): Long {
        return  ejercicioDao.insert(ejercicio)
    }

    suspend fun updateEjercicio(ejercicio: EjercicioEntity){
        ejercicioDao.update(ejercicio)
    }

    suspend fun deleteEjercicio(ejercicio: EjercicioEntity){
        ejercicioDao.delete(ejercicio)
    }

}