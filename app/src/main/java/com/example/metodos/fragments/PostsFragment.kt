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
import com.example.metodos.adapter.PostsAdapter
import com.example.metodos.model.Publicacion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsFragment : Fragment() {

    private lateinit var rvPosts: RecyclerView
    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPosts = view.findViewById(R.id.recyclerViewPosts)
        rvPosts.layoutManager = LinearLayoutManager(context)

        cargarPublicaciones()
    }

    private fun cargarPublicaciones() {
        apiService.getPublicaciones().enqueue(object: Callback<List<Publicacion>> {
            override fun onResponse(call: Call<List<Publicacion>>, response: Response<List<Publicacion>>) {
                if (response.isSuccessful) {
                    val posts = response.body()
                    if (!posts.isNullOrEmpty()) {
                        // Pasamos el listener de clics al crear el adapter
                        rvPosts.adapter = PostsAdapter(posts) { publicacion ->
                            val intent = Intent(activity, DetallePublicacionActivity::class.java).apply {
                                putExtra(DetallePublicacionActivity.EXTRA_PUBLICACION_ID, publicacion.id)
                            }
                            startActivity(intent)
                        }
                    }
                } else {
                    Log.e("PostsFragment", "Error al cargar publicaciones: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Publicacion>>, t: Throwable) {
                Log.e("PostsFragment", "Fallo en la conexión", t)
            }
        })
    }
}

