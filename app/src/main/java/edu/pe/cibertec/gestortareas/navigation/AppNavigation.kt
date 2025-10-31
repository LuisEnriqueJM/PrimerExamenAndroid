package edu.pe.cibertec.gestortareas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.pe.cibertec.gestortareas.data.repository.FirebaseRepository
import edu.pe.cibertec.gestortareas.ui.screens.*
import edu.pe.cibertec.gestortareas.viewmodel.AuthViewModel
import edu.pe.cibertec.gestortareas.viewmodel.AuthViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val repository = FirebaseRepository(context)
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(repository)
    )

    // Determinar ruta inicial basada en autenticación
    val startDestination = if (authViewModel.estaAutenticado) {
        Screen.Menu.route
    } else {
        Screen.Welcome.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Welcome.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Menu.route) {
            // Proteger ruta - redirigir si no está autenticado
            LaunchedEffect(authViewModel.estaAutenticado) {
                if (!authViewModel.estaAutenticado) {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                }
            }

            MenuScreen(
                onNavigateToProductos = {
                    navController.navigate(Screen.Productos.route)
                },
                onNavigateToLibroMundo = {
                    navController.navigate(Screen.LibroMundo.route)
                },
                onNavigateToTareas = {
                    navController.navigate(Screen.Tareas.route)
                },
                onLogout = {
                    authViewModel.cerrarSesion()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Productos.route) {
            ProductosScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.LibroMundo.route) {
            PantallaLibroMundo()
        }

        composable(Screen.Tareas.route) {
            PantallaPrincipal()
        }
    }
}
