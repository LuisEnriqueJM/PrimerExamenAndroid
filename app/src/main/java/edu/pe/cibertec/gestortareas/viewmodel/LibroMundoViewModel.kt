package edu.pe.cibertec.gestortareas.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import edu.pe.cibertec.gestortareas.model.Categoria
import edu.pe.cibertec.gestortareas.model.InfoDescuento
import edu.pe.cibertec.gestortareas.model.Libro
import edu.pe.cibertec.gestortareas.model.Notificacion
import edu.pe.cibertec.gestortareas.model.TipoNotificacion

class LibroMundoViewModel : ViewModel() {

    // Estado para los campos del formulario
    var tituloLibro by mutableStateOf("")
        private set

    var precioUnitario by mutableStateOf("")
        private set

    var cantidad by mutableStateOf("")
        private set

    var categoriaSeleccionada by mutableStateOf<Categoria?>(null)
        private set

    // Lista de libros en el carrito
    val librosCarrito = mutableStateListOf<Libro>()

    // Cálculos
    var subtotal by mutableStateOf(0.0)
        private set

    var montoDescuento by mutableStateOf(0.0)
        private set

    var total by mutableStateOf(0.0)
        private set

    var porcentajeDescuento by mutableStateOf(0)
        private set

    // Estado de diálogos
    var mostrarDialogoValidacion by mutableStateOf(false)
        private set

    var mensajeValidacion by mutableStateOf("")
        private set

    var mostrarDialogoConfirmacionLimpiar by mutableStateOf(false)
        private set

    var mostrarDialogoConfirmacionEliminar by mutableStateOf(false)
        private set

    var libroAEliminar by mutableStateOf<Libro?>(null)
        private set

    // Notificación (Snackbar)
    var notificacion by mutableStateOf<Notificacion?>(null)
        private set

    var calculoRealizado by mutableStateOf(false)
        private set

    // Funciones para actualizar campos
    fun actualizarTitulo(nuevoTitulo: String) {
        tituloLibro = nuevoTitulo
    }

    fun actualizarPrecio(nuevoPrecio: String) {
        precioUnitario = nuevoPrecio
    }

    fun actualizarCantidad(nuevaCantidad: String) {
        cantidad = nuevaCantidad
    }

    fun actualizarCategoria(nuevaCategoria: Categoria) {
        categoriaSeleccionada = nuevaCategoria
    }

    // Función para validar y agregar libro
    fun agregarLibro() {
        // Validar título
        if (tituloLibro.isBlank()) {
            mensajeValidacion = "Debe ingresar el título del libro"
            mostrarDialogoValidacion = true
            return
        }

        // Validar precio
        val precio = precioUnitario.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            mensajeValidacion = "El precio debe ser mayor a 0"
            mostrarDialogoValidacion = true
            return
        }

        // Validar cantidad
        val cant = cantidad.toIntOrNull()
        if (cant == null || cant <= 0) {
            mensajeValidacion = "La cantidad debe ser mayor a 0"
            mostrarDialogoValidacion = true
            return
        }

        // Validar categoría
        if (categoriaSeleccionada == null) {
            mensajeValidacion = "Debe seleccionar una categoría"
            mostrarDialogoValidacion = true
            return
        }

        // Crear el libro
        val nuevoLibro = Libro(
            titulo = tituloLibro,
            precioUnitario = precio,
            cantidad = cant,
            categoria = categoriaSeleccionada!!.nombre
        )

        // Agregar a la lista
        librosCarrito.add(nuevoLibro)

        // Log para depuración
        println("📚 Libro agregado: ${nuevoLibro.titulo} - Total en carrito: ${librosCarrito.size}")

        // Mostrar Snackbar de éxito
        notificacion = Notificacion(
            mensaje = "Libro '${nuevoLibro.titulo}' agregado (Total: ${librosCarrito.size})",
            tipo = TipoNotificacion.EXITO
        )

        // Limpiar campos (excepto categoría)
        limpiarCampos()
    }

    private fun limpiarCampos() {
        tituloLibro = ""
        precioUnitario = ""
        cantidad = ""
        // Mantener categoría seleccionada
    }

    // Función para calcular total
    fun calcularTotal() {
        // Validar que haya libros
        if (librosCarrito.isEmpty()) {
            mensajeValidacion = "Debe agregar al menos un libro al carrito"
            mostrarDialogoValidacion = true
            return
        }

        // Calcular subtotal
        subtotal = librosCarrito.sumOf { it.subtotal }

        // Calcular cantidad total de libros
        val cantidadTotal = librosCarrito.sumOf { it.cantidad }

        // Obtener información del descuento
        val infoDescuento = obtenerInfoDescuento(cantidadTotal)
        porcentajeDescuento = infoDescuento.porcentaje

        // Calcular descuento
        montoDescuento = subtotal * (porcentajeDescuento / 100.0)

        // Calcular total
        total = subtotal - montoDescuento

        // Mostrar notificación según descuento
        notificacion = Notificacion(
            mensaje = infoDescuento.mensaje,
            tipo = when (porcentajeDescuento) {
                0 -> TipoNotificacion.INFO
                10 -> TipoNotificacion.EXITO
                15 -> TipoNotificacion.INFO
                20 -> TipoNotificacion.EXITO
                else -> TipoNotificacion.INFO
            },
            colorFondo = infoDescuento.color
        )

        calculoRealizado = true
    }

    private fun obtenerInfoDescuento(cantidadTotal: Int): InfoDescuento {
        return when {
            cantidadTotal in 1..2 -> InfoDescuento(
                porcentaje = 0,
                color = Color.Gray,
                mensaje = "No hay descuento aplicado"
            )
            cantidadTotal in 3..5 -> InfoDescuento(
                porcentaje = 10,
                color = Color(0xFF4CAF50), // Verde
                mensaje = "¡Genial! Ahorraste S/. %.2f".format(subtotal * 0.10)
            )
            cantidadTotal in 6..10 -> InfoDescuento(
                porcentaje = 15,
                color = Color(0xFF2196F3), // Azul
                mensaje = "¡Excelente! Ahorraste S/. %.2f".format(subtotal * 0.15)
            )
            else -> InfoDescuento(
                porcentaje = 20,
                color = Color(0xFFFFD700), // Dorado
                mensaje = "¡Increíble! Ahorraste S/. %.2f".format(subtotal * 0.20)
            )
        }
    }

    // Función para limpiar carrito
    fun confirmarLimpiarCarrito() {
        mostrarDialogoConfirmacionLimpiar = true
    }

    fun limpiarCarrito() {
        librosCarrito.clear()
        limpiarCampos()
        categoriaSeleccionada = null
        subtotal = 0.0
        montoDescuento = 0.0
        total = 0.0
        porcentajeDescuento = 0
        calculoRealizado = false

        notificacion = Notificacion(
            mensaje = "Carrito limpiado",
            tipo = TipoNotificacion.INFO
        )

        ocultarDialogoConfirmacionLimpiar()
    }

    // Función para eliminar libro individual
    fun mostrarDialogoEliminarLibro(libro: Libro) {
        libroAEliminar = libro
        mostrarDialogoConfirmacionEliminar = true
    }

    fun eliminarLibro() {
        libroAEliminar?.let { libro ->
            librosCarrito.removeAll { it.id == libro.id }

            // Recalcular automáticamente si ya se había calculado
            if (calculoRealizado && librosCarrito.isNotEmpty()) {
                calcularTotal()
            } else if (librosCarrito.isEmpty()) {
                subtotal = 0.0
                montoDescuento = 0.0
                total = 0.0
                porcentajeDescuento = 0
                calculoRealizado = false
            }

            notificacion = Notificacion(
                mensaje = "Libro eliminado del carrito",
                tipo = TipoNotificacion.ADVERTENCIA
            )
        }

        ocultarDialogoConfirmacionEliminar()
    }

    // Funciones para ocultar diálogos
    fun ocultarDialogoValidacion() {
        mostrarDialogoValidacion = false
    }

    fun ocultarDialogoConfirmacionLimpiar() {
        mostrarDialogoConfirmacionLimpiar = false
    }

    fun ocultarDialogoConfirmacionEliminar() {
        mostrarDialogoConfirmacionEliminar = false
        libroAEliminar = null
    }

    // Función para ocultar notificación
    fun ocultarNotificacion() {
        notificacion = null
    }
}
 