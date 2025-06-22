package com.example.metodos

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.metodos.model.GrupoComunidad
import com.google.android.material.appbar.CollapsingToolbarLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleGrupoActivity : AppCompatActivity() {

    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    companion object {
        const val EXTRA_GRUPO_ID = "EXTRA_GRUPO_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_grupo)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val grupoId = intent.getIntExtra(EXTRA_GRUPO_ID, -1)
        if (grupoId != -1) {
            cargarDetallesGrupo(grupoId)
        }
    }

    // Para que la flecha de "atrás" en la toolbar funcione
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun cargarDetallesGrupo(id: Int) {
        apiService.getGrupoById(id).enqueue(object: Callback<GrupoComunidad> {
            override fun onResponse(call: Call<GrupoComunidad>, response: Response<GrupoComunidad>) {
                if (response.isSuccessful) {
                    val grupo = response.body()
                    if (grupo != null) {
                        poblarVistas(grupo)
                    }
                } else {
                    Log.e("DetalleGrupo", "Error al cargar grupo: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GrupoComunidad>, t: Throwable) {
                Log.e("DetalleGrupo", "Fallo en la conexión", t)
            }
        })
    }

    private fun poblarVistas(grupo: GrupoComunidad) {
        val collapsingToolbar: CollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        val imageView: ImageView = findViewById(R.id.imageViewGrupoDetalle)
        val miembrosTextView: TextView = findViewById(R.id.textViewMiembrosDetalle)

        collapsingToolbar.title = grupo.nombre
        miembrosTextView.text = "${grupo.memberCount} miembros"

        Glide.with(this)
            .load(grupo.fotoUrl)
            .into(imageView)

        // TODO: Aquí cargaríamos las publicaciones del RecyclerView.
    }
}
