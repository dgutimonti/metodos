package com.example.metodos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.metodos.R
import com.example.metodos.model.Mascota

class MascotaAdapter(
    private val mascotas: List<Mascota>,
    private val onItemClicked: (Mascota) -> Unit // Un listener para manejar los clics
) : RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder>() {

    class MascotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mascotaImageView: ImageView = itemView.findViewById(R.id.imageViewMascota)
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreMascota)
        val edadTextView: TextView = itemView.findViewById(R.id.textViewEdadMascota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mascota, parent, false)
        return MascotaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mascotas.size
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = mascotas[position]

        holder.nombreTextView.text = mascota.nombre
        holder.edadTextView.text = "${mascota.edad} años"

        Glide.with(holder.itemView.context)
            .load(mascota.fotoUrl)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher_round)
            .into(holder.mascotaImageView)

        // --- INTERACTIVIDAD ---
        // Añadimos el listener al item completo
        holder.itemView.setOnClickListener {
            onItemClicked(mascota)
        }
    }
}
