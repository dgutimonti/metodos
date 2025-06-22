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
import com.example.metodos.adapter.GroupAdapter
import com.example.metodos.model.RespuestaComunidad
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupsFragment : Fragment() {

    private lateinit var rvMisGrupos: RecyclerView
    private lateinit var rvGruposSugeridos: RecyclerView
    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMisGrupos = view.findViewById(R.id.recyclerViewMisGrupos)
        rvGruposSugeridos = view.findViewById(R.id.recyclerViewGruposSugeridos)
        rvMisGrupos.layoutManager = LinearLayoutManager(context)
        rvGruposSugeridos.layoutManager = LinearLayoutManager(context)

        cargarGrupos()
    }

    private fun cargarGrupos() {
        apiService.getGruposComunidad().enqueue(object: Callback<RespuestaComunidad> {
            override fun onResponse(call: Call<RespuestaComunidad>, response: Response<RespuestaComunidad>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        // Pasamos el listener de clics al crear los adapters
                        rvMisGrupos.adapter = GroupAdapter(it.misGrupos) { grupo ->
                            navegarADetalleGrupo(grupo.id)
                        }
                        rvGruposSugeridos.adapter = GroupAdapter(it.gruposSugeridos) { grupo ->
                            navegarADetalleGrupo(grupo.id)
                        }
                    }
                } else {
                    Log.e("GroupsFragment", "Error al cargar grupos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RespuestaComunidad>, t: Throwable) {
                Log.e("GroupsFragment", "Fallo en la conexión", t)
            }
        })
    }

    private fun navegarADetalleGrupo(grupoId: Int) {
        val intent = Intent(activity, DetalleGrupoActivity::class.java).apply {
            putExtra(DetalleGrupoActivity.EXTRA_GRUPO_ID, grupoId)
        }
        startActivity(intent)
    }
}
