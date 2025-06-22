package com.example.metodos

import com.example.metodos.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServices {
    @GET("mascotas.php")
    fun getMascotas(): Call<List<Mascota>>
    @GET("mascotas.php")
    fun getMascotaById(@Query("id") mascotaId: Int): Call<Mascota>
    @GET("refugios.php")
    fun getRefugios(): Call<List<Refugio>>
    @GET("refugios.php")
    fun getRefugioById(@Query("id") refugioId: Int): Call<Refugio>
    @GET("favoritos.php")
    fun getFavoritos(): Call<List<Int>>
    @GET("usuario.php")
    fun getUsuario(): Call<Usuario>
    @GET("comunidad.php")
    fun getGruposComunidad(): Call<RespuestaComunidad>
    @GET("comunidad.php")
    fun getGrupoById(@Query("id") grupoId: Int): Call<GrupoComunidad>
    @GET("publicaciones.php")
    fun getPublicaciones(): Call<List<Publicacion>>
    @GET("publicaciones.php")
    fun getPublicacionById(@Query("id") publicacionId: Int): Call<Publicacion>
    @GET("eventos.php")
    fun getEventos(): Call<List<Evento>>

    /**
     * --- MÉTODO AÑADIDO ---
     * Obtiene los detalles de un evento específico por su ID.
     */
    @GET("eventos.php")
    fun getEventoById(@Query("id") eventoId: Int): Call<Evento>
}



