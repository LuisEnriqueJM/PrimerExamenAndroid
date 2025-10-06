package edu.pe.cibertec.gestortareas.model

import java.util.UUID

data class Libro(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val precioUnitario: Double,
    val cantidad: Int,
    val categoria: String,
    val subtotal: Double = precioUnitario * cantidad
)
