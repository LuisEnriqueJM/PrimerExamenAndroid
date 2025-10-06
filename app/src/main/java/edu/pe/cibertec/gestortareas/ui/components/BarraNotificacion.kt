package edu.pe.cibertec.gestortareas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

import edu.pe.cibertec.gestortareas.model.Notificacion
import edu.pe.cibertec.gestortareas.model.TipoNotificacion
import kotlinx.coroutines.delay


@Composable
fun BarraNotificacion(
    notificacion: Notificacion?,
    onDismiss: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(notificacion) {
        notificacion?.let {
            if (it.visible){
                snackbarHostState.showSnackbar(
                    message = it.mensaje,
                    duration = SnackbarDuration.Short
                )
                delay(2000)
                onDismiss()
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                containerColor = obtenerColorNotificacion(notificacion?.tipo),
                contentColor = Color.White,
                action = {
                    IconButton(onClick = {data.dismiss()}) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                }
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Icon(
                        imageVector = obtenerIconoNotificacion(notificacion?.tipo),
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        text = data.visuals.message
                    )

                }
            }

        }
    )

}
@Composable
private fun RowScope.obtenerIconoNotificacion(tipo: TipoNotificacion?): ImageVector {
    return when (tipo){
        TipoNotificacion.ERROR -> Icons.Default.Close
        TipoNotificacion.INFO -> Icons.Default.Info
        TipoNotificacion.ADVERTENCIA -> Icons.Default.Warning
        TipoNotificacion.EXITO -> Icons.Default.CheckCircle
        else -> Icons.Default.Info
    }
}

@Composable
fun obtenerColorNotificacion(tipo: TipoNotificacion?): Color {
    return when (tipo){
        TipoNotificacion.EXITO -> Color.Green
        TipoNotificacion.ERROR -> Color.Red
        TipoNotificacion.INFO -> Color.Blue
        TipoNotificacion.ADVERTENCIA -> Color.Yellow
        else -> Color.Green
    }
}
