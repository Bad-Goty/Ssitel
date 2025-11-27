package com.example.ssitel.data.repository

import com.example.ssitel.data.remote.RetrofitClient
import com.example.ssitel.data.remote.UsuarioApi
import com.example.ssitel.data.remote.UsuarioResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UsuarioRepository {

    private val api = RetrofitClient.instance.create(UsuarioApi::class.java)

    suspend fun obtenerUsuario(id: Int) = api.getUsuario(id)

    suspend fun crearUsuario(nombre: String, password: String) =
        api.crearUsuario(
            mapOf(
                "nombre" to nombre,
                "password" to password
            )
        )

    suspend fun actualizarUsuario(
        id: Int,
        sobremi: String,
        imagen: File
    ): UsuarioResponse {

        val requestFile = imagen.asRequestBody("image/*".toMediaType())

        val body = MultipartBody.Part.createFormData(
            "imagen",
            imagen.name,
            requestFile
        )

        return api.actualizarUsuario(id, sobremi, body)
    }


    suspend fun login(nombre: String, password: String): UsuarioResponse {
        return api.login(
            mapOf(
                "nombre" to nombre,
                "password" to password
            )
        )
    }

}
