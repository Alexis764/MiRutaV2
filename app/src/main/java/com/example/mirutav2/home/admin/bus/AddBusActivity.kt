package com.example.mirutav2.home.admin.bus

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
import org.json.JSONException
import org.json.JSONObject

class AddBusActivity : AppCompatActivity() {

    // Variable Datos
    private lateinit var AddDataPlate : EditText

    // Variable Botones
    private lateinit var btnCloseBus : ImageButton
    private lateinit var btnSendBusData : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bus)

        initData()
        backAdmin()
        sendData()

    }

    // Recoleccion de datos
    private fun initData() {
        AddDataPlate = findViewById(R.id.AddDataPlate)

        // Botones

        btnSendBusData = findViewById(R.id.btnSendBusData)
        btnCloseBus = findViewById(R.id.btnCloseBus)
    }

    // Navegacion de vuelta a Admin
    private fun backAdmin() {
        btnCloseBus.setOnClickListener {
            onBackPressed()
        }
    }

    // Enviar datos a BD
    private fun sendData() {
        btnSendBusData.setOnClickListener {

            val placa = AddDataPlate.text.toString().trim()

            if(placa.isNotEmpty()){
                val url = URLBASE +"/bus/guardar"
                val queue = Volley.newRequestQueue(this)
                queue.add(AddBus(url))
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun AddBus(url: String): JsonObjectRequest {
        val parametros = JSONObject()
        val AddLatitud = 10.00
        val AddLongitud = 10.00

        try {
            parametros.put("placaBus", AddDataPlate.text.toString())
            parametros.put("longitudBus", AddLongitud)
            parametros.put("latitudBus", AddLatitud)


        }catch (e: JSONException){
            Log.e("AddBusJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parametros, { response ->
            Toast.makeText(AddDataPlate.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()

        }, { error ->
            Log.e("AddBus", error.toString())
        })

        return jsonObjectRequest
    }
}