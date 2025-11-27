package com.example.ssitel.data.remote

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UsuarioApi {

    @GET("usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): UsuarioResponse

    @POST("usuarios")
    suspend fun crearUsuario(
        @Body body: Map<String, String>
    ): UsuarioResponse

    @Multipart
    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Int,
        @Part("sobremi") sobremi: String,
        @Part imagen: MultipartBody.Part
    ): UsuarioResponse

    @POST("login")
    suspend fun login(@Body body: Map<String, String>): UsuarioResponse

}