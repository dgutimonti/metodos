package com.example.metodos

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.metodos.model.Publicacion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetallePublicacionActivity : AppCompatActivity() {

    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    companion object {
        const val EXTRA_PUBLICACION_ID = "EXTRA_PUBLICACION_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_publicacion)

        val publicacionId = intent.getIntExtra(EXTRA_PUBLICACION_ID, -1)
        if (publicacionId != -1) {
            cargarDetallesPublicacion(publicacionId)
        }
    }

    private fun cargarDetallesPublicacion(id: Int) {
        apiService.getPublicacionById(id).enqueue(object : Callback<Publicacion> {
            override fun onResponse(call: Call<Publicacion>, response: Response<Publicacion>) {
                if (response.isSuccessful) {
                    val publicacion = response.body()
                    if (publicacion != null) {
                        poblarVistas(publicacion)
                    }
                } else {
                    Log.e("DetallePublicacion", "Error al cargar publicacion: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Publicacion>, t: Throwable) {
                Log.e("DetallePublicacion", "Fallo en la conexión", t)
            }
        })
    }

    private fun poblarVistas(publicacion: Publicacion) {
        val postView: View = findViewById(R.id.post_detail_content)

        val userImageView: ImageView = postView.findViewById(R.id.imageViewUsuarioPost)
        val userNameTextView: TextView = postView.findViewById(R.id.textViewNombreUsuarioPost)
        val contentTextView: TextView = postView.findViewById(R.id.textViewContenidoPost)
        val postImageView: ImageView = postView.findViewById(R.id.imageViewImagenPost)
        val likesTextView: TextView = postView.findViewById(R.id.textViewLikes)
        val commentsTextView: TextView = postView.findViewById(R.id.textViewComentarios)

        userNameTextView.text = publicacion.nombreUsuario
        contentTextView.text = publicacion.contenido
        likesTextView.text = publicacion.likes.toString()
        commentsTextView.text = publicacion.comentariosCount.toString()

        Glide.with(this).load(publicacion.usuarioFotoUrl).circleCrop().into(userImageView)
        Glide.with(this).load(publicacion.imagenUrl).into(postImageView)
    }
}
