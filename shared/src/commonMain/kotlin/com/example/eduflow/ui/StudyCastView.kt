package com.example.eduflow.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eduflow.api.GeminiApi
import com.example.eduflow.storage.HorarioStorage
import kotlinx.coroutines.launch

@Composable
fun StudyCastView(onVolver: () -> Unit) {
    val scope    = rememberCoroutineScope()
    val materias = remember { HorarioStorage.obtenerMaterias() }
    var materiaSeleccionada by remember { mutableStateOf<String?>(null) }
    var consejo  by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Beige)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onVolver,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("←", fontSize = 20.sp, color = VerdePrimario)
                }
                Spacer(Modifier.weight(1f))
                Text("StudyFlow", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, color = VerdePrimario)
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(40.dp)) // balance
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 90.dp)
            ) {
                Text("StudyCast", fontSize = 26.sp,
                    fontWeight = FontWeight.Bold, color = TextoPrimario)
                Text("Selecciona una materia y recibe\nun consejo personalizado con IA.",
                    fontSize = 13.sp, color = TextoSecundario, lineHeight = 19.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))

                if (materias.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFDDE8E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("SF", fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold, color = VerdePrimario)
                            }
                            Spacer(Modifier.height(14.dp))
                            Text("Sin materias registradas",
                                fontSize = 16.sp, fontWeight = FontWeight.Bold,
                                color = TextoPrimario)
                            Text("Regresa al Dashboard y agrega\nuna materia primero.",
                                fontSize = 13.sp, color = TextoSecundario,
                                modifier = Modifier.padding(top = 6.dp),
                                textAlign = TextAlign.Center, lineHeight = 19.sp)
                            Spacer(Modifier.height(20.dp))
                            Button(
                                onClick = onVolver,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = VerdePrimario)
                            ) {
                                Text("Ir al Dashboard", color = Color.White,
                                    fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                } else {
                    Text("Tu biblioteca", fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold, color = TextoPrimario,
                        modifier = Modifier.padding(bottom = 10.dp))

                    materias.forEach { (nombre, dificultad) ->
                        val seleccionada = materiaSeleccionada == nombre
                        val simbolo = when {
                            nombre.contains("mat", ignoreCase = true) ||
                                    nombre.contains("calc", ignoreCase = true) ||
                                    nombre.contains("alg", ignoreCase = true)  -> "∑"
                            nombre.contains("prog", ignoreCase = true) ||
                                    nombre.contains("cod", ignoreCase = true)  -> "<>"
                            nombre.contains("fis", ignoreCase = true)  -> "λ"
                            else -> "◈"
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable {
                                    materiaSeleccionada = nombre
                                    consejo = ""
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (seleccionada) VerdePrimario
                                else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                if (seleccionada) 4.dp else 2.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (seleccionada)
                                                Color.White.copy(alpha = 0.18f)
                                            else Color(0xFFDDE8E0)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(simbolo, fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (seleccionada) Color.White
                                        else VerdePrimario)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(nombre, fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (seleccionada) Color.White
                                        else TextoPrimario)
                                    Text("Dificultad: $dificultad/10",
                                        fontSize = 12.sp,
                                        color = if (seleccionada)
                                            Color.White.copy(0.72f)
                                        else TextoSecundario)
                                }
                                if (seleccionada) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color.White.copy(alpha = 0.18f)
                                    ) {
                                        Text("LISTO", fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            letterSpacing = 0.5.sp,
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp, vertical = 3.dp))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val tema = materiaSeleccionada ?: return@Button
                            cargando = true
                            consejo  = ""
                            scope.launch {
                                consejo  = GeminiApi().obtenerConsejo(tema)
                                cargando = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        enabled = materiaSeleccionada != null && !cargando,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdePrimario,
                            disabledContainerColor = Color(0xFFCCCCCC)
                        )
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(color = Color.White,
                                modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        } else {
                            Text(
                                if (materiaSeleccionada == null)
                                    "Selecciona una materia primero"
                                else
                                    "Pedir consejo — $materiaSeleccionada",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    if (consejo.isNotEmpty()) {
                        Spacer(Modifier.height(20.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = VerdePrimario),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White.copy(alpha = 0.18f)
                                ) {
                                    Text("STUDYCAST",
                                        fontSize = 10.sp, fontWeight = FontWeight.Bold,
                                        color = Color.White, letterSpacing = 1.sp,
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp, vertical = 4.dp))
                                }
                                Spacer(Modifier.height(10.dp))
                                Text("Consejo para $materiaSeleccionada",
                                    fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                                    color = Color.White.copy(0.8f))
                                Spacer(Modifier.height(8.dp))
                                Text(consejo, color = Color.White,
                                    fontSize = 14.sp, lineHeight = 22.sp)
                            }
                        }
                    }
                }
            }
        }

        // Bottom nav
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Beige,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomNavItem(label = "Dashboard", selected = false,
                    symbol = "⊞", onClick = onVolver)
                BottomNavItem(label = "StudyCast", selected = true, symbol = "▶")
                BottomNavItem(label = "Audio", selected = false, symbol = "♪")
                BottomNavItem(label = "Peers", selected = false, symbol = "⊙")
            }
        }
    }
}
