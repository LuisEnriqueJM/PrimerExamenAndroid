package edu.pe.cibertec.gestortareas.model

import java.util.*
import java.text.SimpleDateFormat

data class Tarea(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val fechaCreacion: Date = Date(),
    val completada: Boolean = false,
    val prioridad: Prioridad = Prioridad.MEDIA
){
    fun fechaFormateada():String{
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formato.format(fechaCreacion)
    }
} 