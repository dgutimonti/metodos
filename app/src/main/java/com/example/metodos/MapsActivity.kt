package com.example.metodos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.metodos.model.Refugio
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * This callback is triggered when the map is ready to be used.
     */
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
                            // MUY IMPORTANTE: Guardamos el ID del refugio en la etiqueta del marcador.
                            marker?.tag = refugio.id
                            boundsBuilder.include(ubicacion)
                        }
                        // Movemos la cámara para que todos los marcadores sean visibles
                        val bounds = boundsBuilder.build()
                        val padding = 100 // offset from edges of the map in pixels
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                        googleMap?.animateCamera(cameraUpdate)
                    }
                } else {
                    Log.e("MapsActivity", "Error al recibir refugios: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Refugio>>, t: Throwable) {
                Log.e("MapsActivity", "Fallo en la conexión", t)
            }
        })
    }

    private fun configurarListenerDeMarcadores() {
        googleMap?.setOnInfoWindowClickListener { marker ->
            val refugioId = marker.tag as? Int
            if (refugioId != null) {
                // Creamos un Intent para abrir la pantalla de detalles del refugio
                val intent = Intent(this, DetalleRefugioActivity::class.java).apply {
                    putExtra(DetalleRefugioActivity.EXTRA_REFUGIO_ID, refugioId)
                }
                startActivity(intent)
            }
        }
    }
}
