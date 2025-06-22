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
import com.example.metodos.adapter.EventsAdapter
import com.example.metodos.model.Evento
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsFragment : Fragment() {

    private lateinit var rvEvents: RecyclerView
    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvEvents = view.findViewById(R.id.recyclerViewEvents)
        rvEvents.layoutManager = LinearLayoutManager(context)

        cargarEventos()
    }

    private fun cargarEventos() {
        apiService.getEventos().enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                if (response.isSuccessful) {
                    val events = response.body()
                    if (!events.isNullOrEmpty()) {
                        rvEvents.adapter = EventsAdapter(events) { evento ->
                            val intent = Intent(activity, DetalleEventoActivity::class.java).apply {
                                putExtra(DetalleEventoActivity.EXTRA_EVENTO_ID, evento.id)
                            }
                            startActivity(intent)
                        }
                    }
                } else {
                    Log.e("EventsFragment", "Error al cargar eventos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                Log.e("EventsFragment", "Fallo en la conexión", t)
            }
        })
    }
}