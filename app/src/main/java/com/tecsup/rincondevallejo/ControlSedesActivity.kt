package com.tecsup.rincondevallejo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ControlSedesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_sedes)

        val btnGoogleMaps = findViewById<MaterialButton>(R.id.btnGoogleMaps)
        val btnMantenedor = findViewById<MaterialButton>(R.id.btnMantenedor)

        // Botón 01: Google Maps - Mapa con todas las sedes registradas
        btnGoogleMaps.setOnClickListener {
            startActivity(Intent(this, AllSedesMapActivity::class.java))
        }

        // Botón 02: Mantenedor - Listado y registro de sedes
        btnMantenedor.setOnClickListener {
            startActivity(Intent(this, SedesListActivity::class.java))
        }
    }
}
