package com.example.metodos.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.metodos.*
import com.example.metodos.adapter.MascotaAdapter
import com.example.metodos.model.Mascota
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class FavoritesFragment : Fragment() {

    private lateinit var recyclerViewFavoritos: RecyclerView
    private val apiService = REST.getRestEngine().create(APIServices::class.java)
    private val listaMascotasFavoritas = mutableListOf<Mascota>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewFavoritos = view.findViewById(R.id.recyclerViewFavoritos)
        recyclerViewFavoritos.layoutManager = LinearLayoutManager(context)
        cargarMascotasFavoritas()
    }

    private fun cargarMascotasFavoritas() {
        apiService.getFavoritos().enqueue(object : Callback<List<Int>> {
            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                if (response.isSuccessful) {
                    val idsFavoritos = response.body()
                    if (!idsFavoritos.isNullOrEmpty()) {
                        fetchDetallesDeFavoritos(idsFavoritos)
                    }
                }
            }
            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                Log.e("FavoritesFragment", "Error al obtener IDs de favoritos", t)
            }
        })
    }

    private fun fetchDetallesDeFavoritos(ids: List<Int>) {
        val latch = CountDownLatch(ids.size)
        listaMascotasFavoritas.clear()

        ids.forEach { id ->
            apiService.getMascotaById(id).enqueue(object : Callback<Mascota> {
                override fun onResponse(call: Call<Mascota>, response: Response<Mascota>) {
                    if (response.isSuccessful) {
                        response.body()?.let { listaMascotasFavoritas.add(it) }
                    }
                    latch.countDown()
                }
                override fun onFailure(call: Call<Mascota>, t: Throwable) {
                    latch.countDown()
                }
            })
        }

        Thread {
            latch.await()
            activity?.runOnUiThread {
                actualizarRecyclerView()
            }
        }.start()
    }

    private fun actualizarRecyclerView() {
        recyclerViewFavoritos.adapter = MascotaAdapter(listaMascotasFavoritas) { mascota ->
            val intent = Intent(activity, DetalleMascotaActivity::class.java).apply {
                putExtra(DetalleMascotaActivity.EXTRA_MASCOTA_ID, mascota.id)
            }
            startActivity(intent)
        }
    }
}
