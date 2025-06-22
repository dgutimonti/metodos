package com.example.metodos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.metodos.R
import com.example.metodos.model.Refugio

class RefugioAdapter(
    private val refugios: List<Refugio>,
    private val onItemClicked: (Refugio) -> Unit
) : RecyclerView.Adapter<RefugioAdapter.RefugioViewHolder>() {

    class RefugioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val refugioImageView: ImageView = itemView.findViewById(R.id.imageViewRefugio)
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreRefugio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefugioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_refugio, parent, false)
        return RefugioViewHolder(view)
    }

    override fun getItemCount(): Int {
        return refugios.size
    }

    override fun onBindViewHolder(holder: RefugioViewHolder, position: Int) {
        val refugio = refugios[position]
        holder.nombreTextView.text = refugio.nombre

        // --- ACTUALIZACIÓN AQUÍ ---
        // Ahora usamos la URL de la foto del refugio
        Glide.with(holder.itemView.context)
            .load(refugio.fotoUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_home) // Un placeholder genérico
            .error(R.drawable.ic_home)       // Una imagen de error
            .into(holder.refugioImageView)

        holder.itemView.setOnClickListener {
            onItemClicked(refugio)
        }
    }
}


