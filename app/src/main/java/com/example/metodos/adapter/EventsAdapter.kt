package com.example.metodos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.metodos.R
import com.example.metodos.model.Evento

class EventsAdapter(
    private val events: List<Evento>,
    private val onItemClicked: (Evento) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventImageView: ImageView = itemView.findViewById(R.id.imageViewEvento)
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTituloEvento)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewFechaEvento)
        val locationTextView: TextView = itemView.findViewById(R.id.textViewUbicacionEvento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.titleTextView.text = event.titulo
        holder.dateTextView.text = event.fecha
        holder.locationTextView.text = event.ubicacion

        Glide.with(holder.itemView.context).load(event.imagenUrl).into(holder.eventImageView)

        holder.itemView.setOnClickListener {
            onItemClicked(event)
        }
    }

    override fun getItemCount() = events.size
}