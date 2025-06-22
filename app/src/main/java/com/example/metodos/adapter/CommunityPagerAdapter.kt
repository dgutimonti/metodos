package com.example.metodos.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.metodos.fragments.EventsFragment
import com.example.metodos.fragments.GroupsFragment
import com.example.metodos.fragments.PostsFragment

class CommunityPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3 // Grupos, Publicaciones, Eventos

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GroupsFragment()
            1 -> PostsFragment()
            // Reemplazamos el placeholder con nuestro nuevo fragmento de eventos
            2 -> EventsFragment()
            else -> throw IllegalStateException("Posición de pestaña inválida")
        }
    }
}


