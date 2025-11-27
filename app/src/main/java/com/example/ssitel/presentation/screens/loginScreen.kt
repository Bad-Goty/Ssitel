package com.example.ssitel.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ssitel.R
import com.example.ssitel.presentation.components.MyOutlinedTextField
import com.example.ssitel.presentation.components.MyText
import com.example.ssitel.presentation.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UsuarioViewModel = viewModel()   // 👈 ViewModel
) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(uiState.usuario?.id) {
        val id = uiState.usuario?.id
        if (id != null) {
            navController.navigate("home/$id") {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ssitel),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .border(
                    color = Color.Black,
                    width = 2.dp,
                    shape = RoundedCornerShape(20.dp)
                )
        )

        Spacer(Modifier.height(20.dp))

        MyOutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            placeholder = "Usuario"
        )

        Spacer(Modifier.height(20.dp))

        MyOutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Contraseña",
            isPassword = true
        )

        Spacer(Modifier.height(16.dp))

        // Mensaje de error
        if (uiState.error != null) {
            Text(
                text = uiState.error ?: "",
                color = Color.Red,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(8.dp))
        }

        // Mensaje de éxito / info
        if (uiState.mensaje != null) {
            Text(
                text = uiState.mensaje ?: "",
                color = Color(0xFF0B557D),
                fontSize = 14.sp
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (usuario.isNotBlank() && password.isNotBlank()) {
                    // 👇 Llamas al ViewModel, él llama al Repository, éste al API
                    viewModel.crearUsuario(usuario, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            enabled = !uiState.loading, // deshabilita mientras carga
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0B557D),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp)
        ) {
            if (uiState.loading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp),
                    color = Color.White
                )
            } else {
                MyText(
                    text = "Crear Sesión",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }

        Button(
            onClick = {
                if (usuario.isNotBlank() && password.isNotBlank()) {
                    viewModel.login(usuario, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0B557D),
                contentColor = Color.White
            ),
            enabled = !uiState.loading, // deshabilita mientras carga
            shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp)
        ) {

            if (uiState.loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        }

    }
}