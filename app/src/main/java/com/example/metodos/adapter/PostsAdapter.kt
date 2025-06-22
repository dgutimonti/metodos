package com.example.metodos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.metodos.R
import com.example.metodos.model.Publicacion

class PostsAdapter(
    private val posts: List<Publicacion>,
    private val onItemClicked: (Publicacion) -> Unit // Listener para clics
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImageView: ImageView = itemView.findViewById(R.id.imageViewUsuarioPost)
        val userNameTextView: TextView = itemView.findViewById(R.id.textViewNombreUsuarioPost)
        val contentTextView: TextView = itemView.findViewById(R.id.textViewContenidoPost)
        val postImageView: ImageView = itemView.findViewById(R.id.imageViewImagenPost)
        val likesTextView: TextView = itemView.findViewById(R.id.textViewLikes)
        val commentsTextView: TextView = itemView.findViewById(R.id.textViewComentarios)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.userNameTextView.text = post.nombreUsuario
        holder.contentTextView.text = post.contenido
        holder.likesTextView.text = post.likes.toString()
        holder.commentsTextView.text = post.comentariosCount.toString()

        Glide.with(holder.itemView.context).load(post.usuarioFotoUrl).circleCrop().into(holder.userImageView)
        Glide.with(holder.itemView.context).load(post.imagenUrl).into(holder.postImageView)

        // --- INTERACTIVIDAD AÑADIDA ---
        holder.itemView.setOnClickListener {
            onItemClicked(post)
        }
    }

    override fun getItemCount() = posts.size
}

