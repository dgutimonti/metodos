package com.example.metodos.model

import com.google.gson.annotations.SerializedName

data class Publicacion(
    val id: Int,

    @SerializedName("nombre_usuario")
    val nombreUsuario: String,

    @SerializedName("usuario_foto_url")
    val usuarioFotoUrl: String,

    val contenido: String,

    @SerializedName("imagen_url")
    val imagenUrl: String,

    val likes: Int,

    @SerializedName("comentarios_count")
    val comentariosCount: Int
)
