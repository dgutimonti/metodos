package com.example.metodos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginEmailButton: Button = findViewById(R.id.buttonLoginEmail)
        val guestTextView: TextView = findViewById(R.id.textViewGuest)

        // Asignamos el listener a ambos botones para que hagan lo mismo por ahora
        loginEmailButton.setOnClickListener { navigateToMain() }
        guestTextView.setOnClickListener { navigateToMain() }

        // TODO: En el futuro, los botones de Google, Facebook y Apple tendrán su propia lógica.
    }

    private fun navigateToMain() {
        // Creamos un Intent para ir a la MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // Finalizamos la LoginActivity para que el usuario no pueda volver a ella
        finish()
    }
}
