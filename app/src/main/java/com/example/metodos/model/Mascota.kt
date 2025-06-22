package com.example.metodos.model

/**
 * Data class que representa el modelo de una Mascota.
 * Se usará para mapear la respuesta JSON de la API a un objeto Kotlin.
 */
data class Mascota(
    val id: Int,
    val nombre: String,
    val edad: Int,
    val raza: String,
    val tamano: String,
    val genero: String,
    val personalidad: String,
    val historia: String,
    val fotoUrl: String
)