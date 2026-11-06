package com.tecsup.rincondevallejo

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etClave = findViewById<EditText>(R.id.etClave)
        val btnIngresar = findViewById<MaterialButton>(R.id.btnIngresar)
        val btnRecuperarClave = findViewById<MaterialButton>(R.id.btnRecuperarClave)

        btnIngresar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val clave = etClave.text.toString().trim()

            if (correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                etCorreo.error = "Ingrese un correo válido"
                etCorreo.requestFocus()
                return@setOnClickListener
            }

            if (clave.length < 4) {
                etClave.error = "La clave debe tener al menos 4 caracteres"
                etClave.requestFocus()
                return@setOnClickListener
            }

            Toast.makeText(this, "Bienvenido a Rincón de Vallejo", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ControlSedesActivity::class.java))
            finish()
        }

        btnRecuperarClave.setOnClickListener {
            val correoActual = etCorreo.text.toString().trim()
            val inputCorreo = EditText(this).apply {
                hint = "name@example.com"
                setText(correoActual)
                setPadding(48, 32, 48, 32)
            }

            AlertDialog.Builder(this)
                .setTitle("Recuperar contraseña")
                .setMessage("Ingrese su correo institucional para enviar el enlace de recuperación:")
                .setView(inputCorreo)
                .setPositiveButton("Enviar") { _, _ ->
                    val email = inputCorreo.text.toString().trim()
                    if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(
                            this,
                            "Enlace de recuperación enviado a $email",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this, "Correo no válido", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
