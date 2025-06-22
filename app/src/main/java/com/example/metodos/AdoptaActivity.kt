package com.example.metodos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.metodos.model.Mascota
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdoptaActivity : AppCompatActivity() {

    private val apiService = REST.getRestEngine().create(APIServices::class.java)
    private var listaDeMascotas = listOf<Mascota>()
    private var indiceActual = 0

    // Vistas del layout
    private lateinit var mascotaImageView: ImageView
    private lateinit var nombreTextView: TextView
    private lateinit var edadTextView: TextView
    private lateinit var pasarButton: Button
    private lateinit var meGustaButton: Button
    private lateinit var verMasButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adopta)

        // Inicializar vistas
        mascotaImageView = findViewById(R.id.imageViewAdoptaMascota)
        nombreTextView = findViewById(R.id.textViewAdoptaNombre)
        edadTextView = findViewById(R.id.textViewAdoptaEdad)
        pasarButton = findViewById(R.id.buttonPasar)
        meGustaButton = findViewById(R.id.buttonMeGusta)
        verMasButton = findViewById(R.id.buttonVerMas)

        // Asignar listeners a los botones
        pasarButton.setOnClickListener { mostrarSiguienteMascota() }
        meGustaButton.setOnClickListener { mostrarSiguienteMascota() }
        verMasButton.setOnClickListener { verDetalles() }

        // Cargar la lista inicial de mascotas
        cargarMascotas()
    }

    private fun cargarMascotas() {
        apiService.getMascotas().enqueue(object : Callback<List<Mascota>> {
            override fun onResponse(call: Call<List<Mascota>>, response: Response<List<Mascota>>) {
                if (response.isSuccessful) {
                    val mascotas = response.body()
                    if (!mascotas.isNullOrEmpty()) {
                        listaDeMascotas = mascotas
                        mostrarMascotaActual()
                    } else {
                        Toast.makeText(this@AdoptaActivity, "No hay mascotas disponibles", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Mascota>>, t: Throwable) {
                Log.e("AdoptaActivity", "Error al cargar mascotas", t)
                Toast.makeText(this@AdoptaActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarMascotaActual() {
        if (listaDeMascotas.isNotEmpty()) {
            val mascota = listaDeMascotas[indiceActual]
            nombreTextView.text = mascota.nombre
            edadTextView.text = "${mascota.edad} años"
            Glide.with(this)
                .load(mascota.fotoUrl)
                .into(mascotaImageView)
        }
    }

    private fun mostrarSiguienteMascota() {
        if (listaDeMascotas.isNotEmpty()) {
            indiceActual++
            // Si llegamos al final, volvemos al principio
            if (indiceActual >= listaDeMascotas.size) {
                indiceActual = 0
                Toast.makeText(this, "Has visto todas las mascotas. Empezando de nuevo.", Toast.LENGTH_SHORT).show()
            }
            mostrarMascotaActual()
        }
    }

    private fun verDetalles() {
        if (listaDeMascotas.isNotEmpty()) {
            val mascotaActual = listaDeMascotas[indiceActual]
            val intent = Intent(this, DetalleMascotaActivity::class.java).apply {
                putExtra(DetalleMascotaActivity.EXTRA_MASCOTA_ID, mascotaActual.id)
            }
            startActivity(intent)
        }
    }
}
