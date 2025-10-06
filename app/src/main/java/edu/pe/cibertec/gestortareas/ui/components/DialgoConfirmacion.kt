package edu.pe.cibertec.gestortareas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Color
import edu.pe.cibertec.gestortareas.model.Tarea

@Composable
fun DialogoConfirmacion(
    tarea: Tarea,
    onConfirmar: () -> Unit,
    onDismiss: () -> Unit
){

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Advertencia",
                tint = Color(0xFFFF9800)
            )
        },
        title = {
            Text( text = "Confirmar eliminación")
        },
        text = {
            Text(text = "¿Estás seguro de eliminar la tarea: ${tarea.titulo}?")
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336)
                )
                ) {
                    Text("Eliminar")
                }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}