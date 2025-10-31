package edu.pe.cibertec.gestortareas.model

import java.util.UUID

data class Libro(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String = "",
    val precioUnitario: Double = 0.0,
    val cantidad: Int = 0,
    val categoria: String = ""
) {
    // Propiedad calculada que no se serializa a Firestore
    val subtotal: Double
        get() = precioUnitario * cantidad
}
