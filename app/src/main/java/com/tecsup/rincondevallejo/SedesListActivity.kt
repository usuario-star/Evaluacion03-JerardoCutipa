package com.tecsup.rincondevallejo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tecsup.rincondevallejo.adapter.SedeAdapter
import com.tecsup.rincondevallejo.data.Sede
import com.tecsup.rincondevallejo.ui.SedeRegistroDialog
import com.tecsup.rincondevallejo.viewmodel.SedeViewModel

class SedesListActivity : AppCompatActivity() {

    private lateinit var sedeViewModel: SedeViewModel
    private lateinit var sedeAdapter: SedeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sedes_list)

        val ivBack = findViewById<ImageView>(R.id.ivBackSedes)
        val rvSedes = findViewById<RecyclerView>(R.id.rvSedes)
        val tvEmpty = findViewById<TextView>(R.id.tvEmptySedes)
        val fabAgregarSede = findViewById<FloatingActionButton>(R.id.fabAgregarSede)

        ivBack.setOnClickListener { finish() }

        sedeAdapter = SedeAdapter(
            onSedeClick = { sede ->
                // Al seleccionar una sede muestra el mapa con su ubicación
                val intent = Intent(this, SingleSedeMapActivity::class.java)
                intent.putExtra("sede", sede)
                startActivity(intent)
            },
            onSedeLongClick = { sede ->
                confirmarEliminacion(sede)
            }
        )

        rvSedes.layoutManager = LinearLayoutManager(this)
        rvSedes.adapter = sedeAdapter

        sedeViewModel = ViewModelProvider(this)[SedeViewModel::class.java]
        sedeViewModel.allSedes.observe(this) { sedes ->
            sedeAdapter.updateSedes(sedes)
            if (sedes.isEmpty()) {
                tvEmpty.visibility = View.VISIBLE
                rvSedes.visibility = View.GONE
            } else {
                tvEmpty.visibility = View.GONE
                rvSedes.visibility = View.VISIBLE
            }
        }

        fabAgregarSede.setOnClickListener {
            SedeRegistroDialog.mostrar(this) { nuevaSede ->
                sedeViewModel.insert(nuevaSede)
            }
        }
    }

    private fun confirmarEliminacion(sede: Sede) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar sede")
            .setMessage("¿Desea eliminar la sede \"${sede.nombre}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                sedeViewModel.delete(sede)
                Toast.makeText(this, "Sede eliminada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
