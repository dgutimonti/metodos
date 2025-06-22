package com.example.metodos.model

// Usamos la anotación de GSON para que el nombre del campo en Kotlin
// pueda ser diferente al del JSON (miembros vs memberCount).
import com.google.gson.annotations.SerializedName

data class GrupoComunidad(
    val id: Int,
    val nombre: String,
    @SerializedName("miembros") // Mapea el campo 'miembros' del JSON
    val memberCount: Int,
    val fotoUrl: String
)
