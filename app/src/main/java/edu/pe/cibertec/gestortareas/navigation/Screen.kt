package edu.pe.cibertec.gestortareas.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Menu : Screen("menu")
    object Productos : Screen("productos")
    object LibroMundo : Screen("libro_mundo")
    object Tareas : Screen("tareas")
}
