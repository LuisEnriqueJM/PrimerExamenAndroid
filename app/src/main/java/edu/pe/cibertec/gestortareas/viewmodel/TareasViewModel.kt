package edu.pe.cibertec.gestortareas.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import edu.pe.cibertec.gestortareas.model.Prioridad
import edu.pe.cibertec.gestortareas.model.EstadoDialogo
import edu.pe.cibertec.gestortareas.model.Tarea
import edu.pe.cibertec.gestortareas.model.Notificacion
import edu.pe.cibertec.gestortareas.model.TipoNotificacion
import java.util.*

class TareasViewModel : ViewModel(){
    var tareas by mutableStateOf(obtenerTareasIniciales())
        private set
    var estadoDialogo by mutableStateOf(EstadoDialogo())
        private set
    var notificacion by mutableStateOf<Notificacion?>(null)
        private set
    var tituloNuevaTarea by mutableStateOf("")
        private set
    var descripcionNuevaTarea by mutableStateOf("")
        private set
    var priroridadNuevaTarea by mutableStateOf(Prioridad.MEDIA)
        private set

    // Functiones para manejar estado de datos
    fun actualizarTitulo(nuevoTitulo: String){
        tituloNuevaTarea = nuevoTitulo
    }
    fun actualizarDescripcionTarea(nuevaDescripcion: String){
        descripcionNuevaTarea = nuevaDescripcion
    }
    fun actualizarPrioridad(nuevaPrioridad: Prioridad){
      priroridadNuevaTarea = nuevaPrioridad
    }

    fun mostrarDialogoNuevaTarea(){
        estadoDialogo = estadoDialogo.copy(mostrarDialogoNuevaTarea = true)
        limpiarFormulario()
    }

    fun ocultarDialogoNuevaTarea(){
        estadoDialogo = estadoDialogo.copy(mostrarDialogoNuevaTarea = false)
        limpiarFormulario()
    }
    // Confirmaciones
    fun mostrarDaalogoConfirmacion(tarea: Tarea){
        estadoDialogo = estadoDialogo.copy(mostrarConfirmacion = true, tareaAEliminar = tarea)
    }
    fun ocultarDialogoConfirmacion(){
        estadoDialogo = estadoDialogo.copy(mostrarConfirmacion = false, tareaAEliminar = null)
    }

    // Manejo de acciones crud
    fun agregarTarea(){
        if(tituloNuevaTarea.isBlank()){
            mostrarNotificacion("El título es obligatorio", TipoNotificacion.ERROR)
            return
        }
        val nuevaTarea = Tarea(
            id = tareas.size + 1,
            tituloNuevaTarea.trim(),
            descripcionNuevaTarea.trim(),
            prioridad = priroridadNuevaTarea
        )
        tareas = tareas + nuevaTarea
        ocultarDialogoNuevaTarea()
        mostrarNotificacion("Tarea agregada con éxito", TipoNotificacion.EXITO)
    }
    fun eliminarTarea(){
        estadoDialogo.tareaAEliminar?.let { tarea ->
            tareas = tareas.filter { it.id != tarea.id }
            estadoDialogo = estadoDialogo.copy(mostrarConfirmacion = false)
            mostrarNotificacion("Tarea eliminada con éxito", TipoNotificacion.EXITO)
        }
    }
    fun alternarCompletadaTarea(tarea: Tarea){
        tareas = tareas.map {
            if(it.id == tarea.id){
                it.copy(completada = !it.completada)
            }else{
                it
            }
        }
    }

    // Notificaciones
    fun mostrarNotificacion(mensaje: String, tipoNotificacion: TipoNotificacion){
        notificacion = Notificacion(mensaje, tipoNotificacion, true)
    }

    fun ocultarNotificacion(){
        notificacion = notificacion?.copy(visible = false)
    }

    private fun limpiarFormulario(){
        tituloNuevaTarea = ""
        descripcionNuevaTarea = ""
        priroridadNuevaTarea = Prioridad.MEDIA
    }
    private fun obtenerTareasIniciales(): List<Tarea>{
        return listOf(
            Tarea(1, "Tarea 1", "Descripción de la tarea 1", Date(), false, Prioridad.BAJA),
            Tarea(2, "Tarea 2", "Descripción de la tarea 2", Date(), false, Prioridad.MEDIA),
            Tarea(3, "Tarea 3", "Descripción de la tarea 3", Date(), false, Prioridad.ALTA)
        )
    }
}