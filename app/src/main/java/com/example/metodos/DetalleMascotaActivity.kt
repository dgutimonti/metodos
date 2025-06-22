package com.example.metodos

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.metodos.model.Mascota
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleMascotaActivity : AppCompatActivity() {

    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    // Vistas del layout
    private lateinit var mascotaImageView: ImageView
    private lateinit var nombreTextView: TextView
    private lateinit var edadTextView: TextView
    private lateinit var tamanoTextView: TextView
    private lateinit var generoTextView: TextView
    private lateinit var razaTextView: TextView
    private lateinit var personalidadTextView: TextView
    private lateinit var historiaTextView: TextView
    private lateinit var favoritoButton: ImageButton // Nuevo botón

    companion object {
        const val EXTRA_MASCOTA_ID = "EXTRA_MASCOTA_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_mascota)

        // Inicializamos las vistas
        mascotaImageView = findViewById(R.id.imageViewDetalleMascota)
        nombreTextView = findViewById(R.id.textViewNombreDetalle)
        edadTextView = findViewById(R.id.textViewEdadDetalle)
        tamanoTextView = findViewById(R.id.textViewTamanoDetalle)
        generoTextView = findViewById(R.id.textViewGeneroDetalle)
        razaTextView = findViewById(R.id.textViewRazaDetalle)
        personalidadTextView = findViewById(R.id.textViewPersonalidadDetalle)
        historiaTextView = findViewById(R.id.textViewHistoriaDetalle)
        favoritoButton = findViewById(R.id.buttonFavorito) // Inicializamos el botón

        val mascotaId = intent.getIntExtra(EXTRA_MASCOTA_ID, -1)

        if (mascotaId != -1) {
            cargarDetallesMascota(mascotaId)
        }

        // Añadimos el listener al botón
        favoritoButton.setOnClickListener {
            // En una app real, aquí guardaríamos el favorito en la base de datos local o en el servidor.
            // Por ahora, solo mostramos un mensaje.
            Toast.makeText(this, "Añadido a favoritos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarDetallesMascota(id: Int) {
        apiService.getMascotaById(id).enqueue(object : Callback<Mascota> {
            override fun onResponse(call: Call<Mascota>, response: Response<Mascota>) {
                if (response.isSuccessful) {
                    response.body()?.let { poblarVistas(it) }
                } else {
                    Log.e("DetalleMascotaActivity", "Error al recibir detalles: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Mascota>, t: Throwable) {
                Log.e("DetalleMascotaActivity", "Fallo en la conexión", t)
            }
        })
    }

    private fun poblarVistas(mascota: Mascota) {
        nombreTextView.text = mascota.nombre
        edadTextView.text = mascota.edad.toString()
        tamanoTextView.text = mascota.tamano
        generoTextView.text = mascota.genero
        razaTextView.text = mascota.raza
        personalidadTextView.text = mascota.personalidad
        historiaTextView.text = mascota.historia

        Glide.with(this)
            .load(mascota.fotoUrl)
            .into(mascotaImageView)
    }
}