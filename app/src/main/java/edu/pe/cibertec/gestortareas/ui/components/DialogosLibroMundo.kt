package edu.pe.cibertec.gestortareas.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import edu.pe.cibertec.gestortareas.model.Libro

@Composable
fun DialogoValidacion(
    mensaje: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Validación")
        },
        text = {
            Text(mensaje)
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}

@Composable
fun DialogoConfirmacionLimpiar(
    onConfirmar: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Confirmar")
        },
        text = {
            Text("¿Está seguro de limpiar el carrito?")
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Sí")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@Composable
fun DialogoConfirmacionEliminarLibro(
    libro: Libro,
    onConfirmar: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Confirmar")
        },
        text = {
            Text("¿Eliminar '${libro.titulo}' del carrito?")
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Sí")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}
