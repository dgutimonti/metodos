package com.example.metodos.model

import com.google.gson.annotations.SerializedName

data class Evento(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val ubicacion: String,
    @SerializedName("descripcion_corta")
    val descripcionCorta: String,

    // --- CAMPO AÑADIDO ---
    @SerializedName("descripcion_larga")
    val descripcionLarga: String,

    @SerializedName("imagen_url")
    val imagenUrl: String
)

