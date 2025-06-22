package com.example.metodos.model

/**
 * Data class que representa el modelo de un Usuario.
 */
data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val ciudad: String,
    val fotoUrl: String
)
