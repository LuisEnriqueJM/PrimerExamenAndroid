package edu.pe.cibertec.gymtrakcer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pe.cibertec.gymtrakcer.data.local.entity.SerieEntity
import edu.pe.cibertec.gymtrakcer.data.local.entity.SesionEntity
import edu.pe.cibertec.gymtrakcer.data.repository.SesionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: SesionRepository
): ViewModel(){
    private val _sesiones = MutableStateFlow<List<SesionEntity>>(emptyList())
    val sesion: StateFlow<List<SesionEntity>> = _sesiones.asStateFlow()

    private val _seriesActuales = MutableStateFlow<List<SerieEntity>>(emptyList())
    val seriesActuales: StateFlow<List<SerieEntity>> = _seriesActuales.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado Error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarSesiones()
    }

    private fun cargarSesiones() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllSesiones().collect {
                    lista -> _sesiones.value = lista
                }
            }catch (e: Exception){
                _error.value = "Error al cargar sesiones: ${e.message}"

            }finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarSeiresDeSesion(sesionId: Int){
        viewModelScope.launch {
            try {
                repository.getSeriesBySesion(sesionId).collect {
                    lista -> _seriesActuales.value = lista
                }

            }catch (e: Exception){
                _error.value = "Error al cargar series: ${e.message}"
            }
        }
    }

    fun guardarSesionCompleta(
        fecha: Long,
        duracionMinutos: Int,
        notas: String,
        series: List<SerieEntity>
    ){
        viewModelScope.launch {
            try {
                val sesion = SesionEntity(
                    fecha = fecha,
                    duracionMinutos = duracionMinutos,
                    notas = notas
                )
                repository.guardarSesionCompleta(sesion, series)
            }catch (e: Exception){
                _error.value = "Error al guardar sesion: ${e.message}"
            }
        }
    }

    fun insertarSeire(
        sesionId: Int,
        ejericioId: Int,
        pesoKg: Double,
        repeticiones: Int,
        orden: Int
    ){
        viewModelScope.launch {
            try {
                val serie = SerieEntity(
                    sesionId = sesionId,
                    ejercicioId = ejericioId,
                    pesoKg =  pesoKg,
                    repeticiones= repeticiones,
                    orden = orden
                )
                repository.insertSerie(serie)

            }catch (e: Exception){
                _error.value = "Error al guardar serie: ${e.message}"
            }
        }
    }

    fun eliminarSesion(sesionId: Int){
        viewModelScope.launch {
            try {
                repository.elimianrSerieCompleta(sesionId)
            }catch (e: Exception){
                _error.value = "Error al eliminar: ${e.message}"
            }
        }
    }

    fun otenerRecordPersona(ejercicioId: Int, onResult: (SerieEntity?) -> Unit){
        viewModelScope.launch {
            try {
                val record = repository.getRecordPersonal(ejercicioId)
                onResult(record)
            }catch (e: Exception){
                _error.value = "Error al obtener record: ${e.message}"
                onResult(null)
            }
        }
    }

    fun limpiarError(){
        _error.value  = null
    }

}