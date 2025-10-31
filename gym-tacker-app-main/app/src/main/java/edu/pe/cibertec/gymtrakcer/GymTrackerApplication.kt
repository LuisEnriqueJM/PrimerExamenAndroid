package edu.pe.cibertec.gymtrakcer

import android.app.Application
import edu.pe.cibertec.gymtrakcer.data.local.database.AppDatabase
import edu.pe.cibertec.gymtrakcer.data.repository.EjercicioRepository
import edu.pe.cibertec.gymtrakcer.data.repository.SesionRepository

class GymTrackerApplication : Application(){
    private val database by lazy {
        AppDatabase.getDatabase(this)
    }

    val ejercicioRepository by lazy {
        EjercicioRepository(database.ejercicioDao())
    }

    val sesionRepository by lazy {
        SesionRepository(database.serieDao(),database.sesionDao())
    }

    override fun onCreate() {
        super.onCreate()
    }

}