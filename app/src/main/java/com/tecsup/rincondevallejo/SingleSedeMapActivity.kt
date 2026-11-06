package com.tecsup.rincondevallejo

import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tecsup.rincondevallejo.data.Sede

class SingleSedeMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var sede: Sede

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_sede_map)

        sede = intent.getSerializableExtra("sede", Sede::class.java) ?: run {
            finish()
            return
        }

        val ivBack = findViewById<ImageView>(R.id.ivBackSingleMap)
        val tvTitle = findViewById<TextView>(R.id.tvSingleMapTitle)
        val tvEspecialidad = findViewById<TextView>(R.id.tvSingleSedeEspecialidad)
        val tvDetalles = findViewById<TextView>(R.id.tvSingleSedeDetalles)

        ivBack.setOnClickListener { finish() }

        tvTitle.paintFlags = tvTitle.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        tvTitle.text = "Rincón de Vallejo: ${sede.nombre}"

        tvEspecialidad.text = "Especialidad: ${sede.especialidad}"
        tvDetalles.text =
            "Capacidad: ${sede.capacidad} personas | Mesas: ${sede.mesas}\n" +
            "Gerente: ${sede.gerente} | Jefe Personal: ${sede.jefePersonal}"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapSingleSede) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val ubicacion = LatLng(sede.latitud, sede.longitud)
        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.setPadding(0, 220, 0, 260)

        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(ubicacion)
                .title("Rincón de Vallejo - ${sede.nombre}")
                .snippet("${sede.especialidad} | Gerente: ${sede.gerente}")
        )
        marker?.showInfoWindow()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16.5f))
    }
}
