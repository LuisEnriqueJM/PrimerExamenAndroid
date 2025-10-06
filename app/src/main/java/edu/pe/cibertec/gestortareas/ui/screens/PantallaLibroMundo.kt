package edu.pe.cibertec.gestortareas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.pe.cibertec.gestortareas.ui.components.*
import edu.pe.cibertec.gestortareas.viewmodel.LibroMundoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLibroMundo(
    viewModel: LibroMundoViewModel = viewModel()
) {
    // Forzar recomposición cuando cambia la lista
    val libros = viewModel.librosCarrito.toList()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "LibroMundo - Carrito de Compras",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            if (viewModel.calculoRealizado && libros.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { /* Aquí se podría mostrar un resumen detallado */ },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Mostrar Resumen"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Formulario de agregar libro
                FormularioLibro(
                    titulo = viewModel.tituloLibro,
                    precio = viewModel.precioUnitario,
                    cantidad = viewModel.cantidad,
                    categoriaSeleccionada = viewModel.categoriaSeleccionada,
                    onTituloChange = { viewModel.actualizarTitulo(it) },
                    onPrecioChange = { viewModel.actualizarPrecio(it) },
                    onCantidadChange = { viewModel.actualizarCantidad(it) },
                    onCategoriaChange = { viewModel.actualizarCategoria(it) },
                    onAgregar = { viewModel.agregarLibro() }
                )

                // Sección de listado de libros
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Listado de libros en el Carrito (${libros.size}):",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Lista de libros
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (libros.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hay libros en el carrito",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(libros, key = { it.id }) { libro ->
                            TarjetaLibro(
                                libro = libro,
                                onEliminar = { viewModel.mostrarDialogoEliminarLibro(libro) }
                            )
                        }
                    }
                }

                // Sección de totales
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Subtotal:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "S/. %.2f".format(viewModel.subtotal),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Descuento (${viewModel.porcentajeDescuento}%):",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "- S/. %.2f".format(viewModel.montoDescuento),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 2.dp),
                            thickness = 1.dp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "S/. %.2f".format(viewModel.total),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.confirmarLimpiarCarrito() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                ),
                                enabled = libros.isNotEmpty()
                            ) {
                                Text("Limpiar")
                            }

                            Button(
                                onClick = { viewModel.calcularTotal() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Calcular")
                            }
                        }
                    }
                }
            }

            // Snackbar en la parte inferior
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                BarraNotificacion(
                    notificacion = viewModel.notificacion,
                    onDismiss = { viewModel.ocultarNotificacion() }
                )
            }
        }

        // Diálogos
        if (viewModel.mostrarDialogoValidacion) {
            DialogoValidacion(
                mensaje = viewModel.mensajeValidacion,
                onDismiss = { viewModel.ocultarDialogoValidacion() }
            )
        }

        if (viewModel.mostrarDialogoConfirmacionLimpiar) {
            DialogoConfirmacionLimpiar(
                onConfirmar = { viewModel.limpiarCarrito() },
                onDismiss = { viewModel.ocultarDialogoConfirmacionLimpiar() }
            )
        }

        if (viewModel.mostrarDialogoConfirmacionEliminar) {
            viewModel.libroAEliminar?.let { libro ->
                DialogoConfirmacionEliminarLibro(
                    libro = libro,
                    onConfirmar = { viewModel.eliminarLibro() },
                    onDismiss = { viewModel.ocultarDialogoConfirmacionEliminar() }
                )
            }
        }
    }
}
