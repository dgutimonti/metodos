package com.example.metodos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.metodos.adapter.MascotaAdapter
import com.example.metodos.model.Mascota
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class FavoritosActivity : AppCompatActivity() {

    private lateinit var recyclerViewFavoritos: RecyclerView
    private val apiService = REST.getRestEngine().create(APIServices::class.java)
    private val listaMascotasFavoritas = mutableListOf<Mascota>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        recyclerViewFavoritos = findViewById(R.id.recyclerViewFavoritos)
        recyclerViewFavoritos.layoutManager = LinearLayoutManager(this)

        cargarMascotasFavoritas()
    }

    private fun cargarMascotasFavoritas() {
        // 1. Obtenemos la lista de IDs de favoritos
        apiService.getFavoritos().enqueue(object : Callback<List<Int>> {
            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                if (response.isSuccessful) {
                    val idsFavoritos = response.body()
                    if (!idsFavoritos.isNullOrEmpty()) {
                        // 2. Para cada ID, buscamos los detalles de la mascota
                        fetchDetallesDeFavoritos(idsFavoritos)
                    } else {
                        Log.d("FavoritosActivity", "No hay mascotas favoritas.")
                    }
                }
            }

            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                Log.e("FavoritosActivity", "Error al obtener IDs de favoritos", t)
            }
        })
    }

    private fun fetchDetallesDeFavoritos(ids: List<Int>) {
        val latch = CountDownLatch(ids.size) // Para saber cuándo han terminado todas las llamadas
        listaMascotasFavoritas.clear()

        for (id in ids) {
            apiService.getMascotaById(id).enqueue(object : Callback<Mascota> {
                override fun onResponse(call: Call<Mascota>, response: Response<Mascota>) {
                    if (response.isSuccessful) {
                        response.body()?.let { listaMascotasFavoritas.add(it) }
                    }
                    latch.countDown() // Marcamos que esta llamada ha terminado
                }

                override fun onFailure(call: Call<Mascota>, t: Throwable) {
                    latch.countDown() // Marcamos como terminada incluso si falla
                }
            })
        }

        // Esperamos en un hilo de fondo para no bloquear la UI
        Thread {
            latch.await()
            // Una vez que todas las llamadas terminaron, actualizamos la UI en el hilo principal
            runOnUiThread {
                actualizarRecyclerView()
            }
        }.start()
    }

    private fun actualizarRecyclerView() {
        val adapter = MascotaAdapter(listaMascotasFavoritas) { mascota ->
            val intent = Intent(this, DetalleMascotaActivity::class.java).apply {
                putExtra(DetalleMascotaActivity.EXTRA_MASCOTA_ID, mascota.id)
            }
            startActivity(intent)
        }
        recyclerViewFavoritos.adapter = adapter
    }
}
