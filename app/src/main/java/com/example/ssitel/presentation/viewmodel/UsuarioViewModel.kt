package com.example.ssitel.presentation.viewmodel

import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ssitel.data.remote.UsuarioResponse
import com.example.ssitel.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File



class UsuarioViewModel : ViewModel() {

    private val repo = UsuarioRepository()

    // Estado de la UI
    data class UiState(
        val loading: Boolean = false,
        val usuario: UsuarioResponse? = null,
        val error: String? = null,
        val mensaje: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    // 🔹 LOGIN (usa el /login del backend)
    fun login(nombre: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null,
                mensaje = null
            )

            try {
                val user = repo.login(nombre, password)

                _uiState.value = _uiState.value.copy(
                    loading = false,
                    usuario = user,
                    mensaje = user.message ?: "Login exitoso",
                    error = null
                )

            } catch (e: HttpException) {
                val msg = if (e.code() == 401 || e.code() == 404) {
                    "Usuario o contraseña incorrectos"
                } else {
                    "Error de servidor (${e.code()})"
                }
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = msg,
                    usuario = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Error desconocido",
                    usuario = null
                )
            }
        }
    }

    // 🔹 Obtener usuario por id (si lo quieres usar en Home)
    fun cargarUsuario(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null, mensaje = null)
            try {
                val user = repo.obtenerUsuario(id)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    usuario = user
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Error al cargar usuario"
                )
            }
        }
    }

    // 🔹 Crear usuario (si lo sigues usando como registro)
    fun crearUsuario(nombre: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null, mensaje = null)
            try {
                val user = repo.crearUsuario(nombre, password)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    usuario = user,
                    mensaje = user.message ?: "Usuario creado correctamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Error al crear usuario"
                )
            }
        }
    }

    // 🔹 Actualizar sobremi + imagen
    fun actualizarUsuario(id: Int, sobremi: String, imagenFile: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null, mensaje = null)
            try {
                val user = repo.actualizarUsuario(id, sobremi, imagenFile)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    usuario = user,
                    mensaje = user.message ?: "Usuario actualizado correctamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Error al actualizar usuario"
                )
            }
        }
    }
}