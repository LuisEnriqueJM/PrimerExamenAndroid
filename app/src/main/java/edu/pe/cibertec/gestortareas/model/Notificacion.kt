package edu.pe.cibertec.gestortareas.model

import androidx.compose.ui.graphics.Color

data class Notificacion(
    val mensaje:String,
    val tipo: TipoNotificacion,
    val visible: Boolean = false,
    val colorFondo: Color? = null
)