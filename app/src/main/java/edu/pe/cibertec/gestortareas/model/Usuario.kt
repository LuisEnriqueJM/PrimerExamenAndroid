package edu.pe.cibertec.gestortareas.model

data class Usuario(
    val uid: String = "",
    val email: String = "",
    val nombre: String = "",
    val photoUrl: String = "",
    val fechaRegistro: Long = System.currentTimeMillis()
)
