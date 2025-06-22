package com.example.metodos.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.metodos.*
import com.example.metodos.model.Refugio
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        cargarTodosLosRefugiosEnMapa()
        configurarListenerDeMarcadores()
    }

    private fun cargarTodosLosRefugiosEnMapa() {
        apiService.getRefugios().enqueue(object : Callback<List<Refugio>> {
            override fun onResponse(call: Call<List<Refugio>>, response: Response<List<Refugio>>) {
                if (response.isSuccessful) {
                    val refugios = response.body()
                    if (!refugios.isNullOrEmpty()) {
                        val boundsBuilder = LatLngBounds.Builder()
                        refugios.forEach { refugio ->
                            val ubicacion = LatLng(refugio.latitud, refugio.longitud)
                            val marker = googleMap?.addMarker(
                                MarkerOptions()
                                    .position(ubicacion)
                                    .title(refugio.nombre)
                                    .snippet(refugio.direccion)
                            )
                            marker?.tag = refugio.id
                            boundsBuilder.include(ubicacion)
                        }
                        val bounds = boundsBuilder.build()
                        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                    }
                }
            }

            override fun onFailure(call: Call<List<Refugio>>, t: Throwable) {
                Log.e("SearchFragment", "Fallo al obtener refugios", t)
            }
        })
    }

    private fun configurarListenerDeMarcadores() {
        googleMap?.setOnInfoWindowClickListener { marker ->
            val refugioId = marker.tag as? Int
            if (refugioId != null) {
                val intent = Intent(activity, DetalleRefugioActivity::class.java).apply {
                    putExtra(DetalleRefugioActivity.EXTRA_REFUGIO_ID, refugioId)
                }
                startActivity(intent)
            }
        }
    }
}

