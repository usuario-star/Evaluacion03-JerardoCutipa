package com.tecsup.rincondevallejo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tecsup.rincondevallejo.data.Sede
import com.tecsup.rincondevallejo.ui.SedeRegistroDialog
import com.tecsup.rincondevallejo.viewmodel.SedeViewModel
import java.util.Locale

class AllSedesMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var sedeViewModel: SedeViewModel
    private var googleMapRef: GoogleMap? = null
    private var listaActualSedes: List<Sede> = emptyList()
    private val markerSedeMap = mutableMapOf<Marker, Sede>()
    private var ultimaCoordenadaRegistrada: LatLng? = null
    private var camaraInicializada = false

    private lateinit var layoutDetalle: LinearLayout
    private lateinit var tvMapNombreSede: TextView
    private lateinit var tvMapEspecialidad: TextView
    private lateinit var tvMapCapacidadMesas: TextView
    private lateinit var tvMapJefeCoordenadas: TextView
    private lateinit var tvMapGerente: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_sedes_map)

        val ivBack = findViewById<ImageView>(R.id.ivBackAllMap)
        val fabRegistrar = findViewById<FloatingActionButton>(R.id.fabRegistrarEnMapa)
        val ivCerrarDetalle = findViewById<ImageView>(R.id.ivCerrarDetalleMapa)

        layoutDetalle = findViewById(R.id.layoutBottomDetalleMapa)
        tvMapNombreSede = findViewById(R.id.tvMapNombreSede)
        tvMapEspecialidad = findViewById(R.id.tvMapEspecialidad)
        tvMapCapacidadMesas = findViewById(R.id.tvMapCapacidadMesas)
        tvMapJefeCoordenadas = findViewById(R.id.tvMapJefeCoordenadas)
        tvMapGerente = findViewById(R.id.tvMapGerente)

        ivBack.setOnClickListener { finish() }
        ivCerrarDetalle.setOnClickListener { layoutDetalle.visibility = View.GONE }

        sedeViewModel = ViewModelProvider(this)[SedeViewModel::class.java]
        sedeViewModel.allSedes.observe(this) { sedes ->
            listaActualSedes = sedes
            renderizarMarcadores()
        }

        // Mostrar el formulario de registro de sede desde la pantalla de Google Maps
        fabRegistrar.setOnClickListener {
            abrirDialogoRegistroEnMapa(null)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapAllSedes) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapRef = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.setPadding(0, 240, 0, 60)

        googleMap.setOnMarkerClickListener { marker ->
            val sede = markerSedeMap[marker]
            if (sede != null) {
                mostrarDetalleInferior(sede)
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(marker.position, 15.8f)
                )
            }
            false
        }

        googleMap.setOnMapClickListener {
            layoutDetalle.visibility = View.GONE
        }

        // Pulsación larga en el mapa precarga las coordenadas en el registro
        googleMap.setOnMapLongClickListener { latLng ->
            abrirDialogoRegistroEnMapa(Pair(latLng.latitude, latLng.longitude))
        }

        renderizarMarcadores()
    }

    private fun abrirDialogoRegistroEnMapa(coordsPrellenadas: Pair<Double, Double>?) {
        SedeRegistroDialog.mostrar(this, coordsPrellenadas) { nuevaSede ->
            ultimaCoordenadaRegistrada = LatLng(nuevaSede.latitud, nuevaSede.longitud)
            sedeViewModel.insert(nuevaSede)
            mostrarDetalleInferior(nuevaSede)
        }
    }

    private fun renderizarMarcadores() {
        val map = googleMapRef ?: return
        map.clear()
        markerSedeMap.clear()

        if (listaActualSedes.isEmpty()) return

        for (sede in listaActualSedes) {
            val pos = LatLng(sede.latitud, sede.longitud)
            val marker = map.addMarker(
                MarkerOptions()
                    .position(pos)
                    .title(sede.nombre)
                    .snippet(sede.especialidad)
            )
            if (marker != null) {
                markerSedeMap[marker] = sede
            }
        }

        val focoReciente = ultimaCoordenadaRegistrada
        if (focoReciente != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(focoReciente, 16f))
            ultimaCoordenadaRegistrada = null
        } else if (!camaraInicializada) {
            camaraInicializada = true
            // Centrar sobre el Centro Histórico de Trujillo donde se ven las sedes
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(-8.1118, -79.0282),
                    14.8f
                )
            )
        }
    }

    private fun mostrarDetalleInferior(sede: Sede) {
        tvMapNombreSede.text = "RINCÓN DE VALLEJO - ${sede.nombre}"
        tvMapEspecialidad.text = "Especialidad: ${sede.especialidad}"
        tvMapCapacidadMesas.text = "Capacidad: ${sede.capacidad} personas  |  Mesas: ${sede.mesas}"
        val coordsFmt = String.format(Locale.US, "%.4f, %.4f", sede.latitud, sede.longitud)
        tvMapJefeCoordenadas.text = "Jefe de Personal: ${sede.jefePersonal} ($coordsFmt)"
        tvMapGerente.text = "Gerente: ${sede.gerente}"
        layoutDetalle.visibility = View.VISIBLE
    }
}
