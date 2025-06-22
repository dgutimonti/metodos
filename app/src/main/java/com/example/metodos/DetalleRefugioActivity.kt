package com.example.metodos

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.metodos.model.Refugio
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleRefugioActivity : AppCompatActivity(), OnMapReadyCallback {

    private val apiService = REST.getRestEngine().create(APIServices::class.java)
    private var googleMap: GoogleMap? = null
    private var refugioLocation: LatLng? = null

    // Vistas del layout
    private lateinit var nombreTextView: TextView
    private lateinit var direccionTextView: TextView
    private lateinit var telefonoTextView: TextView
    private lateinit var correoTextView: TextView
    private lateinit var horarioTextView: TextView


    companion object {
        const val EXTRA_REFUGIO_ID = "EXTRA_REFUGIO_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_refugio)

        // Inicializar vistas
        nombreTextView = findViewById(R.id.textViewNombreRefugioDetalle)
        direccionTextView = findViewById(R.id.textViewDireccionRefugio)
        telefonoTextView = findViewById(R.id.textViewTelefonoRefugio)
        correoTextView = findViewById(R.id.textViewCorreoRefugio)
        horarioTextView = findViewById(R.id.textViewHorarioRefugio)

        val refugioId = intent.getIntExtra(EXTRA_REFUGIO_ID, -1)
        if (refugioId != -1) {
            cargarDetallesRefugio(refugioId)
        }

        // Inicializar el fragmento del mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun cargarDetallesRefugio(id: Int) {
        apiService.getRefugioById(id).enqueue(object : Callback<Refugio> {
            override fun onResponse(call: Call<Refugio>, response: Response<Refugio>) {
                if (response.isSuccessful) {
                    val refugio = response.body()
                    if (refugio != null) {
                        poblarVistas(refugio)
                        // Guardamos la ubicación para usarla cuando el mapa esté listo
                        refugioLocation = LatLng(refugio.latitud, refugio.longitud)
                        // Si el mapa ya está listo, añadimos el marcador
                        googleMap?.let { addMarkerToMap(it) }
                    }
                } else {
                    Log.e("DetalleRefugioActivity", "Error al recibir detalles del refugio: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Refugio>, t: Throwable) {
                Log.e("DetalleRefugioActivity", "Fallo en la conexión", t)
            }
        })
    }

    private fun poblarVistas(refugio: Refugio) {
        nombreTextView.text = refugio.nombre
        direccionTextView.text = refugio.direccion
        telefonoTextView.text = refugio.telefono
        correoTextView.text = refugio.correo
        horarioTextView.text = refugio.horario
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Si ya tenemos la ubicación del refugio, añadimos el marcador
        refugioLocation?.let { addMarkerToMap(map) }
    }

    private fun addMarkerToMap(map: GoogleMap) {
        refugioLocation?.let { location ->
            map.addMarker(MarkerOptions().position(location).title(nombreTextView.text.toString()))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }
}
