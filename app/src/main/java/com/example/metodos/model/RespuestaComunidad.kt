package com.example.metodos.model

import com.google.gson.annotations.SerializedName

data class RespuestaComunidad(
    @SerializedName("mis_grupos")
    val misGrupos: List<GrupoComunidad>,

    @SerializedName("grupos_sugeridos")
    val gruposSugeridos: List<GrupoComunidad>
)
