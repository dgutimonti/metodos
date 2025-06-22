package com.example.metodos

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class REST {
    companion object {
        // --- CORRECCIÓN FINAL ---
        // Usamos la dirección IP de tu computadora en la red local (obtenida de ifconfig).
        // Esto funcionará tanto para el emulador como para un dispositivo físico en la misma red.
        private const val BASE_URL = "http://192.168.0.191/adopaw/api/"

        fun getRestEngine(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
