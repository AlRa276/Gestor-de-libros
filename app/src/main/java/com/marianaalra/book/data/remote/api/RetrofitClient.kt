package com.marianaalra.book.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton de configuración para Retrofit.
 * Se encarga de instanciar y proveer el servicio "ApiService".
 *
 * Configuración:
 * - Base URL: https://api-gestorlibros-production.up.railway.app/api
 * - Interceptor de logging para debug en Logcat
 * - Timeouts configurados para evitar cuelgues
 * - Conversor Gson para JSON ↔ Kotlin
 */
object RetrofitClient {

    // La URL base obligatoria para todas las peticiones a la API
    private const val BASE_URL = "https://api-gestorlibros-production.up.railway.app/api/"

    /**
     * Crea un Interceptor para mostrar en la consola (Logcat) de Android Studio
     * todo lo que entra y sale por red (Cuerpo de peticiones, URL, respuestas).
     * IMPORTANTE: Comentar en producción por razones de seguridad.
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Agregamos el Interceptor a un cliente de OkHttp (el motor que usa Retrofit por debajo).
     * También configuramos timeouts para evitar que las peticiones se cuelguen indefinidamente.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Instancia final de Retrofit correctamente configurada.
     * Se crea de manera lazy para optimizar memoria.
     */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Agregamos Gson para que Retrofit sepa cómo convertir de JSON a Clase Data
            .addConverterFactory(GsonConverterFactory.create())
            // Le pasamos el cliente con nuestro Interceptor de Logs
            .client(client)
            .build()
    }

    /**
     * Exponemos el servicio `ApiService` para que los Repositorios de la app puedan usarlo.
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}