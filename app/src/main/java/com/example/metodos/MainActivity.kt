package com.example.metodos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.metodos.fragments.CommunityFragment
import com.example.metodos.fragments.FavoritesFragment
import com.example.metodos.fragments.HomeFragment
import com.example.metodos.fragments.ProfileFragment
import com.example.metodos.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Verificamos si es la primera vez que se crea la activity
        if (savedInstanceState == null) {
            // Establecemos el HomeFragment como el fragmento inicial
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            // Marcamos el ítem de home como seleccionado
            bottomNavigationView.selectedItemId = R.id.navigation_home
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_home -> {
                    selectedFragment = HomeFragment()
                }
                R.id.navigation_search -> {
                    selectedFragment = SearchFragment()
                }
                R.id.navigation_community -> {
                    // --- NAVEGACIÓN A COMUNIDAD ---
                    selectedFragment = CommunityFragment()
                }
                R.id.navigation_favorites -> {
                    selectedFragment = FavoritesFragment()
                }
                R.id.navigation_profile -> {
                    selectedFragment = ProfileFragment()
                }
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
            }
            true
        }
    }
}
