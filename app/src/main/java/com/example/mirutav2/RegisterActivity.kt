package com.example.mirutav2

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {



    //Variables
    private lateinit var queue : RequestQueue



    //Variables para componentes
    private lateinit var iedNameRegister: TextInputEditText
    private lateinit var iedEmailRegister: TextInputEditText
    private lateinit var iedPasswordRegister: TextInputEditText
    private lateinit var tvMessage: TextView
    private lateinit var btnRegister: Button

    //Variables para componentes del dialog
    private lateinit var iedPin1: TextInputEditText
    private lateinit var iedPin2: TextInputEditText
    private lateinit var iedPin3: TextInputEditText
    private lateinit var iedPin4: TextInputEditText
    private lateinit var iedPin5: TextInputEditText
    private lateinit var btnVerificationPin: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initComponent()
        initListeners()

    }



    //Conexion de componentes a vista
    private fun initComponent() {
        iedNameRegister = findViewById(R.id.iedNameRegister)
        iedEmailRegister = findViewById(R.id.iedEmailRegister)
        iedPasswordRegister = findViewById(R.id.iedPasswordRegister)
        tvMessage = findViewById(R.id.tvMessage)
        btnRegister = findViewById(R.id.btnRegister)

        queue = Volley.newRequestQueue(this)
    }



    //Funciones click de los componentes
    private fun initListeners() {
        btnRegister.setOnClickListener {
            val nombreUsu = iedNameRegister.text.toString()
            val correoUsu = iedEmailRegister.text.toString()
            val contraseniaUsu = iedPasswordRegister.text.toString()

            if (nombreUsu.isNotEmpty() && correoUsu.isNotEmpty() && contraseniaUsu.isNotEmpty()) {

                queue.add(verificationUser(nombreUsu, correoUsu, contraseniaUsu))

            } else {
                Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }



    //Funciones para registrar el usuario e iniciar la siguiente actividad
    //Funcion para mandar los datos a comprobacion
    private fun verificationUser(nombreUsu: String,
                             correoUsu: String,
                             contraseniaUsu: String,
                             url: String = "$URLBASE/usuario/comprobar"
    ): JsonObjectRequest {
        val parameters = JSONObject()

        try {
            parameters.put("correoUsu", correoUsu)
            parameters.put("contraseniaUsu", contraseniaUsu)
            parameters.put("nombreUsu", nombreUsu)
            parameters.put("fotoUsu", "linkFoto")
            parameters.put("tipoUsuario", 1)

        } catch (e: JSONException) {
            Log.e("verificationUser_JSON", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parameters, { response ->
            if (response.getBoolean("permiso")) {
                val pin1 = response.getInt("pinUno")
                val pin2 = response.getInt("pinDos")
                val pin3 = response.getInt("pinTres")
                val pin4 = response.getInt("pinCuatro")
                val pin5 = response.getInt("pinCinco")

                startDialogPinVerification(parameters, pin1, pin2, pin3, pin4, pin5)

            } else {
                tvMessage.visibility = View.VISIBLE
                tvMessage.text = response.getString("mensaje")
            }

        }, {error ->
            Log.e("verificationUser_Request", error.toString())
        })

        return jsonObjectRequest
    }

    //Funcion para abrir un dialog y comprobar los pines
    private fun startDialogPinVerification(
        parameters: JSONObject,
        pin1: Int,
        pin2: Int,
        pin3: Int,
        pin4: Int,
        pin5: Int
    ) {
        val dialog = Dialog(this)
        dialog.setContentView((R.layout.dialog_pin_verification))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        initComponentDialog(dialog)

        btnVerificationPin.setOnClickListener {
            val pin1Fingered = iedPin1.text.toString()
            val pin2Fingered = iedPin2.text.toString()
            val pin3Fingered = iedPin3.text.toString()
            val pin4Fingered = iedPin4.text.toString()
            val pin5Fingered = iedPin5.text.toString()

            if (pin1Fingered.isNotEmpty() && pin2Fingered.isNotEmpty() && pin3Fingered.isNotEmpty() && pin4Fingered.isNotEmpty() && pin5Fingered.isNotEmpty()) {
                if (pin1Fingered.toInt() == pin1 &&
                    pin2Fingered.toInt() == pin2 &&
                    pin3Fingered.toInt() == pin3 &&
                    pin4Fingered.toInt() == pin4 &&
                    pin5Fingered.toInt() == pin5) {

                    dialog.hide()
                    queue.add(registerUser(parameters))

                } else {
                    Toast.makeText(this, "Pin incorrecto", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Conexion de componentes a vista dialog
    private fun initComponentDialog(dialog: Dialog) {
        iedPin1 = dialog.findViewById(R.id.iedPin1)
        iedPin2 = dialog.findViewById(R.id.iedPin2)
        iedPin3 = dialog.findViewById(R.id.iedPin3)
        iedPin4 = dialog.findViewById(R.id.iedPin4)
        iedPin5 = dialog.findViewById(R.id.iedPin5)
        btnVerificationPin = dialog.findViewById(R.id.btnVerificationPin)
    }

    //Funcion para mandar los datos a registrar el usuario
    private fun registerUser(parameters: JSONObject, url: String = "$URLBASE/usuario/guardar"): JsonObjectRequest {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parameters, { response ->
            if (response.getBoolean("registro")) {
                Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                startLogin()
            }

        }, {error ->
            Log.e("registerUser_Request", error.toString())
        })

        return jsonObjectRequest
    }



    //Funcion para iniciar de nuevo la actividad de login
    private fun startLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}