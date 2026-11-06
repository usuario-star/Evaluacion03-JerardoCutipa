package com.tecsup.rincondevallejo.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.tecsup.rincondevallejo.R
import com.tecsup.rincondevallejo.data.Sede
import java.util.Locale

object SedeRegistroDialog {

    private val especialidades = listOf(
        "Comida Norteña Tradicional",
        "Cabrito y Pato Guisado",
        "Pescados y Mariscos",
        "Criolla Tradicional",
        "Carnes y Parrillas",
        "Buffet Ejecutivo"
    )

    fun mostrar(
        context: Context,
        coordenadasPrellenadas: Pair<Double, Double>? = null,
        onRegistrar: (Sede) -> Unit
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_registro_sede, null)

        val etNombreSede = dialogView.findViewById<EditText>(R.id.etNombreSede)
        val etCapacidad = dialogView.findViewById<EditText>(R.id.etCapacidad)
        val etMesas = dialogView.findViewById<EditText>(R.id.etMesas)
        val spEspecialidad = dialogView.findViewById<Spinner>(R.id.spEspecialidad)
        val etGerente = dialogView.findViewById<EditText>(R.id.etGerente)
        val etJefePersonal = dialogView.findViewById<EditText>(R.id.etJefePersonal)
        val etCoordenadas = dialogView.findViewById<EditText>(R.id.etCoordenadas)
        val btnRegistrarSede = dialogView.findViewById<MaterialButton>(R.id.btnRegistrarSede)

        // Configurar Spinner haciendo uso explícito de AdapterView
        val spinnerAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            especialidades
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEspecialidad.adapter = spinnerAdapter

        var especialidadSeleccionada = especialidades[0]
        spEspecialidad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                especialidadSeleccionada = especialidades[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                especialidadSeleccionada = especialidades[0]
            }
        }

        coordenadasPrellenadas?.let { (lat, lon) ->
            etCoordenadas.setText(
                String.format(Locale.US, "%.5f, %.5f", lat, lon)
            )
        }

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnRegistrarSede.setOnClickListener {
            val nombre = etNombreSede.text.toString().trim()
            val capacidadStr = etCapacidad.text.toString().trim()
            val mesasStr = etMesas.text.toString().trim()
            val gerente = etGerente.text.toString().trim()
            val jefePersonal = etJefePersonal.text.toString().trim()
            val coordsStr = etCoordenadas.text.toString().trim()

            if (nombre.isEmpty() || capacidadStr.isEmpty() || mesasStr.isEmpty() ||
                gerente.isEmpty() || jefePersonal.isEmpty() || coordsStr.isEmpty()
            ) {
                Toast.makeText(context, "Complete todos los campos de la sede", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val capacidad = capacidadStr.toIntOrNull()
            val mesas = mesasStr.toIntOrNull()
            if (capacidad == null || capacidad <= 0 || mesas == null || mesas <= 0) {
                Toast.makeText(context, "Capacidad y mesas deben ser números mayores a 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val coords = parseCoordenadas(coordsStr)
            if (coords == null) {
                Toast.makeText(
                    context,
                    "Ingrese coordenadas válidas (Lat, Lon), ej: -8.1116, -79.0288",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val nuevaSede = Sede(
                nombre = nombre,
                capacidad = capacidad,
                mesas = mesas,
                especialidad = especialidadSeleccionada,
                gerente = gerente,
                jefePersonal = jefePersonal,
                latitud = coords.first,
                longitud = coords.second
            )

            onRegistrar(nuevaSede)
            Toast.makeText(context, "Sede registrada correctamente", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun parseCoordenadas(raw: String): Pair<Double, Double>? {
        val partes = raw.split(",", ";", " ")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (partes.size < 2) return null
        val lat = partes[0].toDoubleOrNull() ?: return null
        val lon = partes[1].toDoubleOrNull() ?: return null
        if (lat !in -90.0..90.0 || lon !in -180.0..180.0) return null
        return Pair(lat, lon)
    }
}
