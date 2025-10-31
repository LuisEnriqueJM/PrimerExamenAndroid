package edu.pe.cibertec.gymtrakcer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.pe.cibertec.gymtrakcer.data.repository.EjercicioRepository
import edu.pe.cibertec.gymtrakcer.data.repository.SesionRepository

class ViewModelFactory (
    private val ejercicioRepository: EjercicioRepository,
    private val sesionRepository: SesionRepository
): ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(EjericioViewModel::class.java) -> {
                EjericioViewModel(ejercicioRepository) as T
            }
            modelClass.isAssignableFrom(SesionViewModel::class.java)->{
                SesionViewModel(sesionRepository) as T
            }
            else -> throw IllegalArgumentException("View Model Class no reconocido: ${modelClass.name}")
        }
    }
}