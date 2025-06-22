package com.example.metodos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.metodos.R
import com.example.metodos.model.GrupoComunidad

class GroupAdapter(
    private val groups: List<GrupoComunidad>,
    private val onItemClicked: (GrupoComunidad) -> Unit // Listener para clics
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupImageView: ImageView = itemView.findViewById(R.id.imageViewGrupo)
        val nameTextView: TextView = itemView.findViewById(R.id.textViewNombreGrupo)
        val membersTextView: TextView = itemView.findViewById(R.id.textViewMiembros)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.nameTextView.text = group.nombre
        holder.membersTextView.text = "${group.memberCount} miembros"

        Glide.with(holder.itemView.context)
            .load(group.fotoUrl)
            .placeholder(R.drawable.ic_community)
            .into(holder.groupImageView)

        // --- INTERACTIVIDAD AÑADIDA ---
        holder.itemView.setOnClickListener {
            onItemClicked(group)
        }
    }

    override fun getItemCount() = groups.size
}
