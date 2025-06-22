package com.example.metodos.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.metodos.APIServices
import com.example.metodos.R
import com.example.metodos.REST
import com.example.metodos.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private val apiService = REST.getRestEngine().create(APIServices::class.java)

    // Declaramos todas las vistas que vamos a manipular
    private lateinit var profileImageView: ImageView
    private lateinit var profileNameTextView: TextView
    private lateinit var profileEmailTextView: TextView
    private lateinit var profilePhoneTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos (creamos) la vista desde el archivo XML del layout
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Una vez que la vista está creada, encontramos cada componente por su ID
        profileImageView = view.findViewById(R.id.profile_image)
        profileNameTextView = view.findViewById(R.id.textViewProfileName)
        profileEmailTextView = view.findViewById(R.id.textViewProfileEmail)
        profilePhoneTextView = view.findViewById(R.id.textViewProfilePhone)

        // Iniciamos la carga de datos desde nuestra API
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        apiService.getUsuario().enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                // Verificamos si la llamada a la API fue exitosa
                if (response.isSuccessful) {
                    val usuario = response.body()
                    if (usuario != null) {
                        // Si recibimos datos, los ponemos en las vistas correspondientes
                        profileNameTextView.text = usuario.nombre
                        profileEmailTextView.text = usuario.correo
                        profilePhoneTextView.text = usuario.telefono

                        // Usamos la librería Glide para cargar la imagen desde la URL
                        Glide.with(this@ProfileFragment)
                            .load(usuario.fotoUrl)
                            .placeholder(R.drawable.ic_profile_placeholder) // Imagen mientras carga la real
                            .error(R.drawable.ic_profile_placeholder)       // Imagen si hay un error
                            .into(profileImageView) // El ImageView donde se mostrará
                    }
                } else {
                    // Si hubo un error en el servidor (ej. 404), lo registramos
                    Log.e("ProfileFragment", "Error al recibir datos del usuario: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                // Si hubo un error de conexión, lo registramos
                Log.e("ProfileFragment", "Fallo en la conexión al obtener usuario", t)
            }
        })
    }
}
