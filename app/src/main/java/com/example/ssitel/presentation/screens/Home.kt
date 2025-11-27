package com.example.ssitel.presentation.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ssitel.presentation.components.MyOutlinedTextField
import com.example.ssitel.presentation.components.MyText
import com.example.ssitel.presentation.viewmodel.UsuarioViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun Home(
    modifier: Modifier = Modifier,
    navController: NavController,
    userId: Int,
    viewModel: UsuarioViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // estados locales para "completar perfil"
    var nuevoSobreMi by remember { mutableStateOf("") }
    var imagenFile by remember { mutableStateOf<File?>(null) }

    // Launcher para elegir imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imagenFile = uriToFile(context, uri)
        }
    }

    // Cargar usuario al entrar a Home
    LaunchedEffect(userId) {
        viewModel.cargarUsuario(userId)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Loading
        if (uiState.loading) {
            CircularProgressIndicator()
            return@Column
        }

        // Error
        if (uiState.error != null) {
            Text(
                text = uiState.error ?: "",
                color = Color.Red,
                fontSize = 18.sp
            )
            return@Column
        }

        // Usuario cargado
        uiState.usuario?.let { user ->

            val tieneImagen = !user.imagen.isNullOrBlank()
            val tieneSobreMi = !user.sobremi.isNullOrBlank()
            val debeCompletarPerfil = !tieneImagen || !tieneSobreMi

            // 👇 Si YA tiene imagen y sobremi: solo mostramos
            if (!debeCompletarPerfil) {

                AsyncImage(
                    model = user.imagen,
                    contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = user.nombre ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = user.sobremi ?: "",
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
            } else {
                // 👇 NO tiene foto o NO tiene sobremi: dejarle completarlos

                MyText(
                    text = "Completa tu perfil",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))



                Spacer(Modifier.height(16.dp))

                // Imagen:
                if (tieneImagen) {
                    AsyncImage(
                        model = user.imagen,
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Vista previa de imagen seleccionada (si ya eligió una)
                    if (imagenFile != null) {
                        AsyncImage(
                            model = imagenFile,
                            contentDescription = null,
                            modifier = Modifier
                                .size(250.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(2.dp, Color.Black, RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(250.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Sin foto")
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.width(250.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0D4DB1),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Seleccionar imagen")
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Sobremi: si ya tiene algo, lo mostramos; si no, editable
                if (tieneSobreMi) {
                    Text(
                        text = user.sobremi ?: "",
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                } else {
                    MyOutlinedTextField(
                        value = nuevoSobreMi,
                        onValueChange = { nuevoSobreMi = it },
                        placeholder = "Sobre mí",
                        isPassword = false
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Botón para GUARDAR perfil (solo si faltaba algo y ya lo llenó)
                Button(
                    onClick = {
                        val textoFinalSobreMi = when {
                            tieneSobreMi -> user.sobremi ?: ""
                            else -> nuevoSobreMi
                        }

                        val fileFinal = when {
                            tieneImagen -> null // tu API actual exige imagen, ojo
                            else -> imagenFile
                        }

                        // Por cómo está tu API, necesitarás SIEMPRE una imagen y un sobremi
                        if (textoFinalSobreMi.isNotBlank() && fileFinal != null && user.id != null) {
                            viewModel.actualizarUsuario(
                                id = user.id,
                                sobremi = textoFinalSobreMi,
                                imagenFile = fileFinal
                            )
                        }
                    },
                    enabled = (
                            (tieneSobreMi || nuevoSobreMi.isNotBlank()) &&
                                    (tieneImagen || imagenFile != null)
                            ),
                ) {
                    Text("Guardar perfil")
                }
            }
        }

        Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }   // Limpia todo el historial
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF921346),
                contentColor = Color.White
            )
        ) {
            Text("Cerrar sesión", fontSize = 18.sp)
        }


    }
}


fun uriToFile(context: Context, uri: Uri): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("perfil_", ".jpg", context.cacheDir)
    FileOutputStream(tempFile).use { out ->
        inputStream?.use { input ->
            input.copyTo(out)
        }
    }
    return tempFile
}
