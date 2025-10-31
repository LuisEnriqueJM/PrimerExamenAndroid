package edu.pe.cibertec.gestortareas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.pe.cibertec.gestortareas.data.local.database.AppDatabase
import edu.pe.cibertec.gestortareas.data.local.entity.ProductoEntity
import edu.pe.cibertec.gestortareas.data.repository.ProductoRepository
import edu.pe.cibertec.gestortareas.viewmodel.ProductoViewModel
import edu.pe.cibertec.gestortareas.viewmodel.ProductoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { ProductoRepository(database.productoDao()) }
    val viewModelFactory = remember { ProductoViewModelFactory(repository) }
    val viewModel = androidx.lifecycle.viewmodel.compose.viewModel<ProductoViewModel>(
        factory = viewModelFactory
    )

    val productos by viewModel.productos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Gestión de Productos",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.abrirDialogoAgregar() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Producto"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (productos.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Sin productos",
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay productos registrados",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Presiona + para agregar un producto",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productos, key = { it.id }) { producto ->
                        ProductoCard(
                            producto = producto,
                            onEdit = { viewModel.prepararEdicion(producto) },
                            onDelete = { viewModel.prepararEliminacion(producto) }
                        )
                    }
                }
            }
        }

        // Diálogo para agregar producto
        if (viewModel.mostrarDialogoAgregar) {
            ProductoDialog(
                titulo = "Agregar Producto",
                nombre = viewModel.nombre,
                descripcion = viewModel.descripcion,
                precio = viewModel.precio,
                stock = viewModel.stock,
                categoria = viewModel.categoria,
                onNombreChange = { viewModel.actualizarNombre(it) },
                onDescripcionChange = { viewModel.actualizarDescripcion(it) },
                onPrecioChange = { viewModel.actualizarPrecio(it) },
                onStockChange = { viewModel.actualizarStock(it) },
                onCategoriaChange = { viewModel.actualizarCategoria(it) },
                onConfirm = { viewModel.agregarProducto() },
                onDismiss = { viewModel.cerrarDialogoAgregar() }
            )
        }

        // Diálogo para editar producto
        if (viewModel.mostrarDialogoEditar) {
            ProductoDialog(
                titulo = "Editar Producto",
                nombre = viewModel.nombre,
                descripcion = viewModel.descripcion,
                precio = viewModel.precio,
                stock = viewModel.stock,
                categoria = viewModel.categoria,
                onNombreChange = { viewModel.actualizarNombre(it) },
                onDescripcionChange = { viewModel.actualizarDescripcion(it) },
                onPrecioChange = { viewModel.actualizarPrecio(it) },
                onStockChange = { viewModel.actualizarStock(it) },
                onCategoriaChange = { viewModel.actualizarCategoria(it) },
                onConfirm = { viewModel.actualizarProducto() },
                onDismiss = { viewModel.cerrarDialogoEditar() }
            )
        }

        // Diálogo para eliminar producto
        if (viewModel.mostrarDialogoEliminar) {
            AlertDialog(
                onDismissRequest = { viewModel.cerrarDialogoEliminar() },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Advertencia"
                    )
                },
                title = { Text("Confirmar eliminación") },
                text = {
                    Text("¿Estás seguro de que deseas eliminar el producto \"${viewModel.productoAEliminar?.nombre}\"?")
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.eliminarProducto() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.cerrarDialogoEliminar() }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Mensaje de validación
        if (viewModel.mostrarMensajeValidacion) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { viewModel.ocultarMensajeValidacion() }) {
                        Text("OK")
                    }
                }
            ) {
                Text(viewModel.mensajeValidacion)
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: ProductoEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = producto.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Precio:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "S/. %.2f".format(producto.precio),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Column {
                    Text(
                        text = "Stock:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${producto.stock} unidades",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        text = "Categoría:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = producto.categoria,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoDialog(
    titulo: String,
    nombre: String,
    descripcion: String,
    precio: String,
    stock: String,
    categoria: String,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = onNombreChange,
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = onDescripcionChange,
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = onPrecioChange,
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    prefix = { Text("S/. ") }
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = onStockChange,
                    label = { Text("Stock") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    suffix = { Text("unidades") }
                )

                OutlinedTextField(
                    value = categoria,
                    onValueChange = onCategoriaChange,
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
