package edu.pe.cibertec.gestortareas.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pe.cibertec.gestortareas.data.local.entity.ProductoEntity
import edu.pe.cibertec.gestortareas.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    // Estados para el formulario
    var nombre by mutableStateOf("")
        private set
    var descripcion by mutableStateOf("")
        private set
    var precio by mutableStateOf("")
        private set
    var stock by mutableStateOf("")
        private set
    var categoria by mutableStateOf("")
        private set

    // Lista de productos
    private val _productos = MutableStateFlow<List<ProductoEntity>>(emptyList())
    val productos: StateFlow<List<ProductoEntity>> = _productos

    // Producto seleccionado para editar
    var productoSeleccionado by mutableStateOf<ProductoEntity?>(null)
        private set

    // Estado de diálogos
    var mostrarDialogoAgregar by mutableStateOf(false)
        private set
    var mostrarDialogoEditar by mutableStateOf(false)
        private set
    var mostrarDialogoEliminar by mutableStateOf(false)
        private set
    var productoAEliminar by mutableStateOf<ProductoEntity?>(null)
        private set

    // Mensaje de validación
    var mensajeValidacion by mutableStateOf("")
        private set
    var mostrarMensajeValidacion by mutableStateOf(false)
        private set

    init {
        cargarProductos()
    }

    // Cargar productos
    fun cargarProductos() {
        viewModelScope.launch {
            repository.getAllProductos().collect { listaProductos ->
                _productos.value = listaProductos
            }
        }
    }

    // Actualizar campos del formulario
    fun actualizarNombre(nuevoNombre: String) {
        nombre = nuevoNombre
    }

    fun actualizarDescripcion(nuevaDescripcion: String) {
        descripcion = nuevaDescripcion
    }

    fun actualizarPrecio(nuevoPrecio: String) {
        precio = nuevoPrecio
    }

    fun actualizarStock(nuevoStock: String) {
        stock = nuevoStock
    }

    fun actualizarCategoria(nuevaCategoria: String) {
        categoria = nuevaCategoria
    }

    // Validar formulario
    private fun validarFormulario(): Boolean {
        return when {
            nombre.isBlank() -> {
                mensajeValidacion = "El nombre es obligatorio"
                mostrarMensajeValidacion = true
                false
            }
            descripcion.isBlank() -> {
                mensajeValidacion = "La descripción es obligatoria"
                mostrarMensajeValidacion = true
                false
            }
            precio.isBlank() || precio.toDoubleOrNull() == null || precio.toDouble() <= 0 -> {
                mensajeValidacion = "El precio debe ser un número válido mayor a 0"
                mostrarMensajeValidacion = true
                false
            }
            stock.isBlank() || stock.toIntOrNull() == null || stock.toInt() < 0 -> {
                mensajeValidacion = "El stock debe ser un número válido mayor o igual a 0"
                mostrarMensajeValidacion = true
                false
            }
            categoria.isBlank() -> {
                mensajeValidacion = "La categoría es obligatoria"
                mostrarMensajeValidacion = true
                false
            }
            else -> true
        }
    }

    // Agregar producto
    fun agregarProducto() {
        if (!validarFormulario()) return

        viewModelScope.launch {
            val nuevoProducto = ProductoEntity(
                nombre = nombre,
                descripcion = descripcion,
                precio = precio.toDouble(),
                stock = stock.toInt(),
                categoria = categoria
            )
            repository.insertProducto(nuevoProducto)
            limpiarFormulario()
            cerrarDialogoAgregar()
        }
    }

    // Actualizar producto
    fun actualizarProducto() {
        if (!validarFormulario()) return

        productoSeleccionado?.let { producto ->
            viewModelScope.launch {
                val productoActualizado = producto.copy(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio.toDouble(),
                    stock = stock.toInt(),
                    categoria = categoria
                )
                repository.updateProducto(productoActualizado)
                limpiarFormulario()
                cerrarDialogoEditar()
            }
        }
    }

    // Eliminar producto
    fun eliminarProducto() {
        productoAEliminar?.let { producto ->
            viewModelScope.launch {
                repository.deleteProducto(producto)
                cerrarDialogoEliminar()
            }
        }
    }

    // Preparar edición
    fun prepararEdicion(producto: ProductoEntity) {
        productoSeleccionado = producto
        nombre = producto.nombre
        descripcion = producto.descripcion
        precio = producto.precio.toString()
        stock = producto.stock.toString()
        categoria = producto.categoria
        mostrarDialogoEditar = true
    }

    // Preparar eliminación
    fun prepararEliminacion(producto: ProductoEntity) {
        productoAEliminar = producto
        mostrarDialogoEliminar = true
    }

    // Limpiar formulario
    fun limpiarFormulario() {
        nombre = ""
        descripcion = ""
        precio = ""
        stock = ""
        categoria = ""
        productoSeleccionado = null
    }

    // Diálogos
    fun abrirDialogoAgregar() {
        limpiarFormulario()
        mostrarDialogoAgregar = true
    }

    fun cerrarDialogoAgregar() {
        mostrarDialogoAgregar = false
        limpiarFormulario()
    }

    fun cerrarDialogoEditar() {
        mostrarDialogoEditar = false
        limpiarFormulario()
    }

    fun cerrarDialogoEliminar() {
        mostrarDialogoEliminar = false
        productoAEliminar = null
    }

    fun ocultarMensajeValidacion() {
        mostrarMensajeValidacion = false
    }
}
