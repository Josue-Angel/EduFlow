package com.example.eduflow

import androidx.compose.runtime.*
import com.example.eduflow.ui.*

enum class Pantalla { LOGIN, DASHBOARD, STUDYCAST }
@Composable
fun App() {
    var pantalla by remember { mutableStateOf(Pantalla.LOGIN) }

    when (pantalla) {
        Pantalla.LOGIN      -> LoginView(
            onLoginExitoso = { pantalla = Pantalla.DASHBOARD }
        )
        Pantalla.DASHBOARD  -> DashboardView(
            onVerStudyCast = { pantalla = Pantalla.STUDYCAST }
        )
        Pantalla.STUDYCAST  -> StudyCastView(
            onVolver = { pantalla = Pantalla.DASHBOARD }
        )
    }
}