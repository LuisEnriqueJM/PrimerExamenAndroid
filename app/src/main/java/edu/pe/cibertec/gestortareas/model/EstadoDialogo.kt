package edu.pe.cibertec.gestortareas.model

data class EstadoDialogo(
    val mostrarDialogoNuevaTarea: Boolean = false,
    val mostrarConfirmacion: Boolean = false,
    val tareaAEliminar: Tarea? = null
)