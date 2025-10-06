package edu.pe.cibertec.gestortareas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.pe.cibertec.gestortareas.model.Prioridad


@Composable
fun DialogoNuevaTarea(
    titulo: String,
    descripcion: String,
    prioridad: Prioridad,
    onTituloChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrioridadChange: (Prioridad) -> Unit,
    onConfirmar: () -> Unit,
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Nueva Tarea",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                // Titulo
                OutlinedTextField(
                    value = titulo,
                    onValueChange = onTituloChange,
                    label = { Text("Título * ") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    isError = titulo.isBlank()
                )
                // Descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = onDescripcionChange,
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4
                )
                // Selector de prioridad
                Text(
                    text = "Prioridad",
                    style = MaterialTheme.typography.labelLarge,
                )
                Column (
                    Modifier.fillMaxWidth().fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Prioridad.values().forEach { prioridadOpcion ->
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (prioridadOpcion == prioridad),
                                    onClick = { onPrioridadChange(prioridadOpcion)}
                                )
                                .padding(horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            RadioButton(
                                selected = (prioridadOpcion == prioridad),
                                onClick = { onPrioridadChange(prioridadOpcion)}
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = prioridadOpcion.color.copy(alpha = 0.2f),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.padding(4.dp)
                            ){
                                Text(
                                    text = prioridadOpcion.name,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = prioridadOpcion.color
                                )
                            }
                        }

                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                enabled = titulo.isNotBlank()
            ) { }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )

}