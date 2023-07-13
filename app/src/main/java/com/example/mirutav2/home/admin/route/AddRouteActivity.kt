package com.example.mirutav2.home.admin.route

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject

class AddRouteActivity : AppCompatActivity() {

    // Variables Datos
    private lateinit var AddDataStartLocation : TextInputEditText
    private lateinit var AddDataFinalLocation : TextInputEditText
    private lateinit var AddDataStartTime : TextInputEditText
    private lateinit var AddDataFinalTime : TextInputEditText
    private lateinit var AddDataDays : TextInputEditText

    // Variable Botones
    private lateinit var btnCloseRoute : ImageButton
    private lateinit var btnSendRouteData : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_route)

        initData()
        backAdmin()
        sendData()


    }

    // Recoleccion de datos
    private fun initData() {
        AddDataStartLocation = findViewById(R.id.AddDataStartLocation)
        AddDataFinalLocation = findViewById(R.id.AddDataFinalLocation)
        AddDataStartTime = findViewById(R.id.AddDataStartTime)
        AddDataFinalTime = findViewById(R.id.AddDataFinalTime)
        AddDataDays = findViewById(R.id.AddDataDays)

        // Botones

        btnSendRouteData = findViewById(R.id.btnSendRouteData)
        btnCloseRoute = findViewById(R.id.btnCloseRoute)
    }

    // Navegacion de vuelta a Admin

    private fun backAdmin() {
        btnCloseRoute.setOnClickListener {
            onBackPressed()
        }
    }

    // Enviar los datos a la BD
    private fun sendData() {
        btnSendRouteData.setOnClickListener {

            // Limpiar espacios en blanco
            val lugarInicio = AddDataStartLocation.text.toString().trim()
            val lugarFinal = AddDataFinalLocation.text.toString().trim()
            val horaInicio = AddDataStartTime.text.toString().trim()
            val horaFinal = AddDataFinalTime.text.toString().trim()
            val diasdispo = AddDataDays.text.toString().trim()

            if(lugarInicio.isNotEmpty() && lugarFinal.isNotEmpty() && horaInicio.isNotEmpty() && horaFinal.isNotEmpty() && diasdispo.isNotEmpty()){
                val url = "$URLBASE/ruta/guardar"
                val queue = Volley.newRequestQueue(this)
                queue.add(AddRoute(url))
            }else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funcion agregar Ruta
    private fun AddRoute(url: String): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("lugarInicioRut", AddDataStartLocation.text.toString())
            parametros.put("lugarDestinoRut", AddDataFinalLocation.text.toString())
            parametros.put("horaInicioRut", AddDataStartTime.text.toString())
            parametros.put("horaFinalRut", AddDataFinalTime.text.toString())
            parametros.put("diasDisponiblesRut", AddDataDays.text.toString())

        }catch (e: JSONException){
            Log.e("AddRouteJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parametros, { response ->
            Toast.makeText(AddDataStartLocation.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()

        }, { error ->
            Log.e("AddRoute", error.toString())
        })

        return jsonObjectRequest
    }

}