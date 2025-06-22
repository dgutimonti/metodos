package com.example.metodos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.metodos.model.Evento
import com.google.android.material.appbar.CollapsingToolbarLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleEventoActivity : AppCompatActivity() {

    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    companion object {
        const val EXTRA_EVENTO_ID = "EXTRA_EVENTO_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_evento)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventoId = intent.getIntExtra(EXTRA_EVENTO_ID, -1)
        if (eventoId != -1) {
            cargarDetallesEvento(eventoId)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun cargarDetallesEvento(id: Int) {
        apiService.getEventoById(id).enqueue(object: Callback<Evento> {
            override fun onResponse(call: Call<Evento>, response: Response<Evento>) {
                if (response.isSuccessful) {
                    val evento = response.body()
                    if (evento != null) {
                        poblarVistas(evento)
                    }
                } else {
                    Log.e("DetalleEvento", "Error al cargar evento: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Evento>, t: Throwable) {
                Log.e("DetalleEvento", "Fallo en la conexión", t)
            }
        })
    }

    private fun poblarVistas(evento: Evento) {
        val collapsingToolbar: CollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        val imageView: ImageView = findViewById(R.id.imageViewEventoDetalle)
        val fechaTextView: TextView = findViewById(R.id.textViewFechaDetalle)
        val ubicacionTextView: TextView = findViewById(R.id.textViewUbicacionDetalle)
        val descripcionTextView: TextView = findViewById(R.id.textViewDescripcionDetalle)

        collapsingToolbar.title = evento.titulo
        fechaTextView.text = evento.fecha
        ubicacionTextView.text = evento.ubicacion
        descripcionTextView.text = evento.descripcionLarga

        Glide.with(this)
            .load(evento.imagenUrl)
            .into(imageView)
    }
}
