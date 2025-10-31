package edu.pe.cibertec.gymtrakcer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pe.cibertec.gymtrakcer.data.local.entity.EjercicioEntity
import edu.pe.cibertec.gymtrakcer.data.repository.EjercicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EjericioViewModel(
    private val repository: EjercicioRepository
): ViewModel() {

    private val _ejercicios = MutableStateFlow<List<EjercicioEntity>>(emptyList())
    val ejericios: StateFlow<List<EjercicioEntity>> = _ejercicios.asStateFlow()

    //Estado carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado Error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        caragrEjercicios()
    }

    private fun caragrEjercicios(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllEjercicios().collect { lista ->
                    _ejercicios.value = lista
                }
            } catch (e: Exception){
                _error.value = "Error al cargar ejercicos: ${e.message}"
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun insertartEjercicio(nombre: String, grupoMuscular: String, tipo:String){
        viewModelScope.launch {
            try {
                val ejercicio = EjercicioEntity(
                    nombre = nombre,
                    grupoMuscular = grupoMuscular,
                    tipo = tipo
                )
                repository.insertEjercicio(ejercicio)

            }catch (e: Exception){
                _error.value = "Error al guardar: ${e.message}"
            }
        }
    }

    fun actualizarEjercicio(ejercicio: EjercicioEntity){
        viewModelScope.launch {
            try {
                repository.updateEjercicio(ejercicio)
            }catch (e: Exception){
                _error.value= "Error al actualizar: ${e.message}"
            }
        }
    }

    fun eliminarEjercicio(ejercicio: EjercicioEntity){
        viewModelScope.launch {
            try {
                repository.deleteEjercicio(ejercicio)
            }catch (e: Exception){
                _error.value = "Error al eliminar: ${e.message}"
            }
        }
    }

    fun limpiarError(){
        _error.value = null
    }
}