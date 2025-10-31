package edu.pe.cibertec.gestortareas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.pe.cibertec.gestortareas.model.Categoria

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioLibro(
    titulo: String,
    precio: String,
    cantidad: String,
    categoriaSeleccionada: Categoria?,
    onTituloChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onCantidadChange: (String) -> Unit,
    onCategoriaChange: (Categoria) -> Unit,
    onAgregar: () -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Agregar Producto",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = titulo,
                onValueChange = onTituloChange,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = precio,
                    onValueChange = onPrecioChange,
                    label = { Text("Precio (S/.)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )

                OutlinedTextField(
                    value = cantidad,
                    onValueChange = onCantidadChange,
                    label = { Text("Cant.") },
                    modifier = Modifier.weight(0.6f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandido,
                    onExpandedChange = { expandido = !expandido },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = categoriaSeleccionada?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = expandido,
                        onDismissRequest = { expandido = false }
                    ) {
                        Categoria.values().forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria.nombre) },
                                onClick = {
                                    onCategoriaChange(categoria)
                                    expandido = false
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = onAgregar,
                    modifier = Modifier.weight(0.8f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Agregar")
                }
            }
        }
    }
}
