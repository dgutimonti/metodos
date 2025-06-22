package com.example.metodos.model

import com.google.gson.annotations.SerializedName

data class Refugio(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val telefono: String,
    val correo: String,
    val horario: String,
    val latitud: Double,
    val longitud: Double,
    val fotoUrl: String // Campo añadido
)

