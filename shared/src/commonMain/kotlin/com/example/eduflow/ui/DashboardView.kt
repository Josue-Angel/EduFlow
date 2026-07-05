//DashboardView.kt
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eduflow.service.HorarioService
import com.example.eduflow.storage.HorarioStorage

@Composable
fun DashboardView(onVerStudyCast: () -> Unit) {
    var mostrarFormulario by remember { mutableStateOf(false) }
    var materias by remember { mutableStateOf(HorarioStorage.obtenerMaterias()) }
    var mostrarMenu by remember { mutableStateOf(false) }
    var mostrarProximamente by remember { mutableStateOf(false) }

    if (mostrarFormulario) {
        AgregarMateriaDialog(
            onGuardar = { nombre, dificultad ->
                HorarioStorage.guardarMateria(nombre, dificultad)
                materias = HorarioStorage.obtenerMaterias()
                mostrarFormulario = false
            },
            onCerrar = { mostrarFormulario = false }
        )
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Beige)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 130.dp)  // espacio para nav + FAB
        ) {
            // Top bar estilo mockup
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hamburger — abre el menú lateral
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { mostrarMenu = true }
                ) {
                    repeat(3) {
                        Box(
                            Modifier
                                .width(22.dp)
                                .height(2.dp)
                                .background(VerdePrimario, RoundedCornerShape(1.dp))
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Text("StudyFlow", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, color = VerdePrimario)
                Spacer(Modifier.weight(1f))
                // Avatar — muestra el correo activo
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFDDE8E0))
                        .clickable { mostrarMenu = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text("A", fontSize = 14.sp,
                        fontWeight = FontWeight.Bold, color = VerdePrimario)
                }
            }

            // Saludo
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    "Hola de nuevo!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrimario
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    if (materias.isEmpty())
                        "Agrega tu primera materia para comenzar."
                    else
                        "Tienes ${materias.size} sesión(es) de estudio para hoy.",
                    fontSize = 13.sp,
                    color = TextoSecundario,
                    lineHeight = 19.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            // Encabezado sección materias
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Sesiones de hoy",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrimario
                )
                Spacer(Modifier.weight(1f))
                if (materias.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFDDE8E0)
                    ) {
                        Text(
                            "${materias.size} Pendiente${if (materias.size > 1) "s" else ""}",
                            fontSize = 12.sp,
                            color = VerdePrimario,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            if (materias.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFDDE8E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("SF", fontSize = 18.sp,
                                fontWeight = FontWeight.Bold, color = VerdePrimario)
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Sin materias aún",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoPrimario
                        )
                        Text(
                            "Toca el botón + para agregar tu primera materia.",
                            fontSize = 13.sp,
                            color = TextoSecundario,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else {
                materias.forEach { (nombre, dificultad) ->
                    MateriaCard(nombre = nombre, dificultad = dificultad)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Card StudyCast — estilo mockup verde oscuro
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { onVerStudyCast() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = VerdePrimario),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White.copy(alpha = 0.18f)
                    ) {
                        Text(
                            "PROXIMO STUDYCAST",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Consejos de estudio\npersonalizados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 26.sp
                    )
                    Text(
                        "Selecciona una materia y recibe\nun consejo con IA.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.72f),
                        modifier = Modifier.padding(top = 6.dp),
                        lineHeight = 19.sp
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("▶", fontSize = 12.sp, color = Color.White)
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("StudyCast", fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.8f))
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.18f)
                        ) {
                            Text(
                                "Abrir ahora",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier.padding(
                                    horizontal = 14.dp, vertical = 8.dp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Bottom navigation bar estilo mockup
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
                BottomNavItem(label = "Dashboard", selected = true, symbol = "⊞")
                BottomNavItem(label = "StudyCast", selected = false, symbol = "▶",
                    onClick = onVerStudyCast)
                BottomNavItem(label = "Audio", selected = false, symbol = "♪",
                    onClick = { mostrarProximamente = true })
                BottomNavItem(label = "Peers", selected = false, symbol = "⊙",
                    onClick = { mostrarProximamente = true })
            }
        }

        // FAB +
        FloatingActionButton(
            onClick = { mostrarFormulario = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 72.dp, end = 20.dp)
                .size(52.dp),
            containerColor = Color(0xFF8B6914),
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("+", fontSize = 26.sp, fontWeight = FontWeight.Light)
        }
    }

    if (mostrarProximamente) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable { mostrarProximamente = false },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFDDE8E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("SF", fontSize = 18.sp,
                            fontWeight = FontWeight.Black, color = VerdePrimario)
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("Próximamente", fontSize = 18.sp,
                        fontWeight = FontWeight.Bold, color = TextoPrimario)
                    Text("Esta sección estará disponible\nen una próxima versión.",
                        fontSize = 13.sp, color = TextoSecundario,
                        modifier = Modifier.padding(top = 6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 19.sp)
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { mostrarProximamente = false },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdePrimario)
                    ) {
                        Text("Entendido", color = Color.White,
                            fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    // Menú lateral
    if (mostrarMenu) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable { mostrarMenu = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(260.dp)
                    .align(Alignment.CenterStart)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(24.dp)
                        .windowInsetsPadding(WindowInsets.systemBars)
                ) {
                    // Logo
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFDDE8E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("SF", fontSize = 14.sp,
                                fontWeight = FontWeight.Black, color = VerdePrimario)
                        }
                        Spacer(Modifier.width(10.dp))
                        Text("StudyFlow", fontSize = 16.sp,
                            fontWeight = FontWeight.Bold, color = VerdePrimario)
                    }

                    Spacer(Modifier.height(32.dp))

                    // Cuenta activa
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFDDE8E0)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(VerdePrimario),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("A", fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text("Cuenta activa", fontSize = 11.sp,
                                    color = TextoSecundario)
                                Text("alumno@upt.mx", fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold, color = TextoPrimario)
                            }
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    // Cerrar sesión
                    OutlinedButton(
                        onClick = { /* cerrar sesión — por ahora solo cierra el menú */ mostrarMenu = false },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdePrimario),
                        border = BorderStroke(1.dp, VerdePrimario)
                    ) {
                        Text("Cerrar sesión", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }


}

@Composable
fun BottomNavItem(
    label: String,
    selected: Boolean,
    symbol: String,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (selected) VerdePrimario else Color.Transparent,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    symbol,
                    fontSize = 16.sp,
                    color = if (selected) Color.White else TextoSecundario
                )
            }
        }
        Spacer(Modifier.height(3.dp))
        Text(
            label,
            fontSize = 10.sp,
            color = if (selected) VerdePrimario else TextoSecundario,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun MateriaCard(nombre: String, dificultad: Int) {
    val colorBarra = when {
        dificultad >= 8 -> VerdePrimario
        dificultad >= 5 -> Color(0xFF8B6914)
        else            -> Color(0xFF4A9060)
    }
    val simbolo = when {
        nombre.contains("mat", ignoreCase = true) ||
                nombre.contains("calc", ignoreCase = true) ||
                nombre.contains("alg", ignoreCase = true)  -> "∑"
        nombre.contains("prog", ignoreCase = true) ||
                nombre.contains("cod", ignoreCase = true)  -> "<>"
        nombre.contains("fis", ignoreCase = true)  -> "λ"
        nombre.contains("quim", ignoreCase = true) -> "⚗"
        else                                       -> "◈"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFDDE8E0)),
                contentAlignment = Alignment.Center
            ) {
                Text(simbolo, fontSize = 18.sp, color = VerdePrimario,
                    fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(nombre, fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold, color = TextoPrimario)
                Spacer(Modifier.height(2.dp))
                Text("Sesión de estudio pendiente",
                    fontSize = 11.sp, color = TextoSecundario)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dificultad", fontSize = 11.sp, color = TextoSecundario)
                    Spacer(Modifier.weight(1f))
                    Text("$dificultad/10", fontSize = 11.sp,
                        fontWeight = FontWeight.Bold, color = colorBarra)
                }
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { dificultad / 10f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = colorBarra,
                    trackColor = Color(0xFFEEEEEE)
                )
            }
        }
    }
}

@Composable
fun AgregarMateriaDialog(
    onGuardar: (String, Int) -> Unit,
    onCerrar: () -> Unit
) {
    val servicio = HorarioService()
    var nombre     by remember { mutableStateOf("") }
    var dificultad by remember { mutableStateOf("") }
    var errorMsg   by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
            .clickable { onCerrar() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp)
                .clickable(enabled = false) {},
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Nueva materia", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold, color = TextoPrimario)
                Text("Agrégala a tus sesiones de hoy",
                    fontSize = 13.sp, color = TextoSecundario,
                    modifier = Modifier.padding(top = 2.dp, bottom = 20.dp))

                Text("Nombre de la materia", fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold, color = TextoPrimario,
                    modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it; errorMsg = "" },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    placeholder = { Text("Ej: Álgebra Lineal",
                        color = Color(0xFFBBBBBB), fontSize = 14.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = VerdePrimario,
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedContainerColor   = Color.White,
                        unfocusedContainerColor = Color(0xFFFAFAFA)
                    )
                )

                Spacer(Modifier.height(14.dp))

                Text("Nivel de dificultad (1-10)", fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold, color = TextoPrimario,
                    modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    value = dificultad,
                    onValueChange = { if (it.length <= 2) { dificultad = it; errorMsg = "" } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    placeholder = { Text("Del 1 al 10",
                        color = Color(0xFFBBBBBB), fontSize = 14.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = VerdePrimario,
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedContainerColor   = Color.White,
                        unfocusedContainerColor = Color(0xFFFAFAFA)
                    )
                )

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color(0xFFB00020), fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp))
                }

                Spacer(Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onCerrar,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = VerdePrimario
                        ),
                        border = BorderStroke(1.dp, VerdePrimario)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = {
                            val nivel = dificultad.toIntOrNull()
                            when {
                                nombre.isEmpty()             -> errorMsg = "Escribe el nombre"
                                nivel == null || nivel !in 1..10 ->
                                    errorMsg = "Ingresa un número del 1 al 10"
                                else -> onGuardar(nombre, nivel)
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdePrimario)
                    ) {
                        Text("Guardar", color = Color.White,
                            fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}