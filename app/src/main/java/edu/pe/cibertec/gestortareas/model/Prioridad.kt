package edu.pe.cibertec.gestortareas.model

import androidx.compose.ui.graphics.Color

enum class Prioridad(val texto: String, val color: Color){
    BAJA("Baja", Color.Green),
    MEDIA("Media", Color.Yellow),
    ALTA("Alta", Color.Red)

}