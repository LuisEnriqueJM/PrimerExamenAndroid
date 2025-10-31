package edu.pe.cibertec.gymtrakcer.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import  androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.pe.cibertec.gymtrakcer.data.local.entity.EjercicioEntity

import edu.pe.cibertec.gymtrakcer.ui.viewmodel.EjericioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun EjerciciosScreen(
    viewModel: EjericioViewModel
){
    val ejercicios by viewModel.ejericios.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var mostrarDialogo by remember{ mutableStateOf(false) }

    Scaffold (
        topBar = {
            TopAppBar(
                title =  { Text("Ejercicios") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogo = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Ejercicio")
            }
        }
    ){  paddingValues ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: $error")
                        Button(
                            onClick = { viewModel.limpiarError( )}
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
                ejercicios.isEmpty() ->{
                    Text("NO HAY EJERCICIOS. PRESIONA + PARA AGREGAR.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(ejercicios) {
                            ejercicio -> EjercicioItem(
                                ejercicio = ejercicio,
                                onDelete = {viewModel.eliminarEjercicio(ejercicio)}
                            )
                        }
                    }
                }
            }
        }

    }
    if (mostrarDialogo){
        AgregarEjercicioDialog(
            onDimiss = {mostrarDialogo = false},
            onConfirm = { nombre, grupo, tipo ->
                viewModel.insertartEjercicio(nombre, grupo, tipo)
                mostrarDialogo = false
            }
        )
    }

}

@Composable
fun EjercicioItem(
    ejercicio: EjercicioEntity,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = ejercicio.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${ejercicio.grupoMuscular} -  ${ejercicio.tipo}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarEjercicioDialog(
    onDimiss: () -> Unit,
    onConfirm: (nombre: String, grupo: String, tipo:String) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var grupoSeleccionado by remember { mutableStateOf("Pecho") }
    var tipoSeleccionado by remember {mutableStateOf("Fuerza")}

    val gruposMusculares = listOf("Pecho","Espalda", "Piernas", "Hombros", "Brazos")
    val tipos = listOf("Fuerza", "Cardio")

    AlertDialog(
        onDismissRequest = onDimiss,
        title = {Text("Nuevo Ejercicio") } ,
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {nombre = it},
                    label = { Text("Nombre del ejercicio")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                var expandedGrupo by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expandedGrupo,
                    onExpandedChange = { expandedGrupo = it }

                ) {
                    OutlinedTextField(
                        value = grupoSeleccionado,
                        onValueChange = {},
                        readOnly =  true,
                        label = { Text("Grupo Muscular")},
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded =  expandedGrupo)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded =  expandedGrupo,
                        onDismissRequest = {expandedGrupo = false}
                    ) {
                        gruposMusculares.forEach { grupo ->
                            DropdownMenuItem(
                                text = {Text(grupo)},
                                onClick = {
                                    grupoSeleccionado = grupo
                                    expandedGrupo = false
                                }
                            )
                        }
                    }
                }

            }

        },
        confirmButton = {},
        dismissButton = {}
    )


}

