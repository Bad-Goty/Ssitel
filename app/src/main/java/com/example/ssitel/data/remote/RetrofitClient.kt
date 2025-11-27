package com.example.ssitel.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.9:3000/"

    private val interceptor = HttpLoggingInterceptor().apply {  // <- Esto imprime todas las peticiones y respuestas HTTP (URL, Headers, JSON enviado, JSON recibido)
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()  // <- IMPORTANTE para agregar interceptores, timeouts y hasta autentificacion de TOKENS, si no se agrega RETROFIT usa uno por defecto sin mostrar lo mencionado
        .addInterceptor(interceptor)
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()                                       // Crea el objeto Retrofit
            .baseUrl(BASE_URL)                                   // Establece la UTL base
            .client(client)                                      //se conecta el cliente
            .addConverterFactory(GsonConverterFactory.create())  //Retrofit necesita convertir JSON <-> objetos Kotlin.
            .build()                                             // Construye y devuelve la instancia
    }
}
