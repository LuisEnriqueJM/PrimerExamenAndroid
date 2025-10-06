package edu.pe.cibertec.gestortareas.ui.screens

import android.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.pe.cibertec.gestortareas.ui.components.BarraNotificacion
import edu.pe.cibertec.gestortareas.ui.components.DialogoConfirmacion
import edu.pe.cibertec.gestortareas.ui.components.DialogoNuevaTarea
import edu.pe.cibertec.gestortareas.ui.components.TarjetaTarea
import edu.pe.cibertec.gestortareas.viewmodel.TareasViewModel
import org.w3c.dom.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    viewModel: TareasViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestor de Tareas",
                    style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.mostrarDialogoNuevaTarea() },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar tarea"
                )
            }
        }
    ) { paddingValues ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            ContenidoPrincial(viewModel)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomCenter

            ){
                BarraNotificacion(
                    notificacion = viewModel.notificacion,
                    onDismiss = {viewModel.ocultarNotificacion()}
                )
            }
        }
        if(viewModel.estadoDialogo.mostrarDialogoNuevaTarea){
            DialogoNuevaTarea(
                titulo = viewModel.tituloNuevaTarea,
                descripcion = viewModel.descripcionNuevaTarea,
                prioridad = viewModel.priroridadNuevaTarea,
                onTituloChange = {viewModel.actualizarTitulo(it)},
                onDescripcionChange = {viewModel.actualizarDescripcionTarea(it) },
                onPrioridadChange = {viewModel.actualizarPrioridad(it)},
                onDismiss = {viewModel.ocultarDialogoNuevaTarea()},
                onConfirmar = {viewModel.agregarTarea()}
            )
        }

        if(viewModel.estadoDialogo.mostrarConfirmacion){
            viewModel.estadoDialogo.tareaAEliminar?.let { tarea ->
                DialogoConfirmacion(
                    tarea = tarea,
                    onConfirmar = { viewModel.eliminarTarea() },
                    onDismiss = { viewModel.ocultarDialogoConfirmacion() }
                )
            }
        }
    }
}

@Composable
fun ContenidoPrincial(viewModel: TareasViewModel) {
    if (viewModel.tareas.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "No hay tareas",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Presione el boton + para agregar nueva tarea",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
            ) {
                items(items = viewModel.tareas, key = { it.id }) { tarea ->
                    TarjetaTarea(
                        tarea = tarea,
                        onCompletadaChange = { viewModel.alternarCompletadaTarea(tarea) },
                        onEliminar = { viewModel.mostrarDaalogoConfirmacion(tarea) }
                    )
                }
            }
        }
    }
}