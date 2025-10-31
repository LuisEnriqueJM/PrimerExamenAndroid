package edu.pe.cibertec.gestortareas.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import edu.pe.cibertec.gestortareas.data.repository.FirebaseRepository
import edu.pe.cibertec.gestortareas.model.Usuario
import kotlinx.coroutines.launch

class AuthViewModel(val repository: FirebaseRepository) : ViewModel() {

    // Estados de UI
    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var nombre by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var estaAutenticado by mutableStateOf(false)
        private set

    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    var mostrarRegistro by mutableStateOf(false)
        private set

    init {
        verificarSesion()
    }

    // Verificar si hay sesión activa
    private fun verificarSesion() {
        val user = repository.obtenerUsuarioActual()
        estaAutenticado = user != null

        if (estaAutenticado) {
            viewModelScope.launch {
                user?.uid?.let { uid ->
                    repository.obtenerDatosUsuario(uid)
                        .onSuccess { usuarioActual = it }
                }
            }
        }
    }

    // Actualizar campos
    fun actualizarEmail(nuevoEmail: String) {
        email = nuevoEmail
        errorMessage = null
    }

    fun actualizarPassword(nuevoPassword: String) {
        password = nuevoPassword
        errorMessage = null
    }

    fun actualizarNombre(nuevoNombre: String) {
        nombre = nuevoNombre
    }

    fun toggleMostrarRegistro() {
        mostrarRegistro = !mostrarRegistro
        errorMessage = null
    }

    // Autenticación con Google
    fun autenticarConGoogle(account: GoogleSignInAccount, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            repository.autenticarConGoogle(account)
                .onSuccess { usuario ->
                    usuarioActual = usuario
                    estaAutenticado = true
                    onSuccess()
                }
                .onFailure { exception ->
                    errorMessage = obtenerMensajeError(exception)
                }

            isLoading = false
        }
    }

    // Registrar con Email/Password
    fun registrar(onSuccess: () -> Unit) {
        if (!validarCampos()) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            repository.registrarUsuario(email, password, nombre)
                .onSuccess { usuario ->
                    usuarioActual = usuario
                    estaAutenticado = true
                    limpiarCampos()
                    onSuccess()
                }
                .onFailure { exception ->
                    errorMessage = obtenerMensajeError(exception)
                }

            isLoading = false
        }
    }

    // Iniciar Sesión con Email/Password
    fun iniciarSesion(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Por favor ingresa email y contraseña"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            repository.iniciarSesion(email, password)
                .onSuccess { uid ->
                    repository.obtenerDatosUsuario(uid)
                        .onSuccess { usuario ->
                            usuarioActual = usuario
                            estaAutenticado = true
                            limpiarCampos()
                            onSuccess()
                        }
                }
                .onFailure { exception ->
                    errorMessage = obtenerMensajeError(exception)
                }

            isLoading = false
        }
    }

    // Cerrar Sesión
    fun cerrarSesion() {
        repository.cerrarSesion()
        estaAutenticado = false
        usuarioActual = null
        limpiarCampos()
    }

    // Validar campos para registro
    private fun validarCampos(): Boolean {
        when {
            nombre.isBlank() -> {
                errorMessage = "Por favor ingresa tu nombre"
                return false
            }
            email.isBlank() -> {
                errorMessage = "Por favor ingresa un email"
                return false
            }
            !email.contains("@") -> {
                errorMessage = "Email inválido"
                return false
            }
            password.length < 6 -> {
                errorMessage = "La contraseña debe tener al menos 6 caracteres"
                return false
            }
        }
        return true
    }

    // Limpiar campos
    private fun limpiarCampos() {
        email = ""
        password = ""
        nombre = ""
    }

    // Obtener mensaje de error amigable
    private fun obtenerMensajeError(exception: Throwable): String {
        return when {
            exception.message?.contains("network") == true ->
                "Error de conexión. Verifica tu internet"
            exception.message?.contains("password") == true ->
                "Contraseña incorrecta"
            exception.message?.contains("user-not-found") == true ->
                "Usuario no encontrado"
            exception.message?.contains("email-already-in-use") == true ->
                "Este email ya está registrado"
            exception.message?.contains("invalid-email") == true ->
                "Email inválido"
            exception.message?.contains("weak-password") == true ->
                "Contraseña muy débil"
            else -> exception.message ?: "Error desconocido"
        }
    }

    fun limpiarError() {
        errorMessage = null
    }
}
