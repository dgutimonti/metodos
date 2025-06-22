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
import com.example.metodos.adapter.RefugioAdapter
import com.example.metodos.model.Mascota
import com.example.metodos.model.Refugio
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerViewMascotas: RecyclerView
    private lateinit var recyclerViewRefugios: RecyclerView
    private lateinit var cardAdoptaAhora: MaterialCardView
    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewMascotas = view.findViewById(R.id.recyclerViewMascotas)
        recyclerViewRefugios = view.findViewById(R.id.recyclerViewRefugios)
        cardAdoptaAhora = view.findViewById(R.id.cardAdoptaAhora)

        recyclerViewMascotas.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewRefugios.layoutManager = LinearLayoutManager(context)

        cardAdoptaAhora.setOnClickListener {
            startActivity(Intent(activity, AdoptaActivity::class.java))
        }

        cargarMascotasRecomendadas()
        cargarRefugiosCercanos()
    }

    private fun cargarMascotasRecomendadas() {
        apiService.getMascotas().enqueue(object : Callback<List<Mascota>> {
            override fun onResponse(call: Call<List<Mascota>>, response: Response<List<Mascota>>) {
                if (response.isSuccessful) {
                    val mascotas = response.body()
                    if (!mascotas.isNullOrEmpty()) {
                        recyclerViewMascotas.adapter = MascotaAdapter(mascotas) { mascota ->
                            val intent = Intent(activity, DetalleMascotaActivity::class.java).apply {
                                putExtra(DetalleMascotaActivity.EXTRA_MASCOTA_ID, mascota.id)
                            }
                            startActivity(intent)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Mascota>>, t: Throwable) {
                Log.e("HomeFragment", "Fallo al obtener mascotas", t)
            }
        })
    }

    private fun cargarRefugiosCercanos() {
        apiService.getRefugios().enqueue(object : Callback<List<Refugio>> {
            override fun onResponse(call: Call<List<Refugio>>, response: Response<List<Refugio>>) {
                if (response.isSuccessful) {
                    val refugios = response.body()
                    if (!refugios.isNullOrEmpty()) {
                        recyclerViewRefugios.adapter = RefugioAdapter(refugios) { refugio ->
                            val intent = Intent(activity, DetalleRefugioActivity::class.java).apply {
                                putExtra(DetalleRefugioActivity.EXTRA_REFUGIO_ID, refugio.id)
                            }
                            startActivity(intent)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Refugio>>, t: Throwable) {
                Log.e("HomeFragment", "Fallo al obtener refugios", t)
            }
        })
    }
}

