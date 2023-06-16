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
import com.example.mirutav2.R
import com.example.mirutav2.home.HomeActivity.Companion.URLBASE
import org.json.JSONException
import org.json.JSONObject

class AddRouteActivity : AppCompatActivity() {

    // Variables Datos
    private lateinit var AddDataStartLocation : EditText
    private lateinit var AddDataFinalLocation : EditText
    private lateinit var AddDataStartTime : EditText
    private lateinit var AddDataFinalTime : EditText

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
            val url = URLBASE + "/ruta/agregar"
            val queue = Volley.newRequestQueue(this)
            queue.add(AddRoute(url))
        }
    }

    // Funcion agregar Ruta
    private fun AddRoute(url: String): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("lugarInicio", AddDataStartLocation.text.toString())
            parametros.put("lugarFinal", AddDataFinalLocation.text.toString())
            parametros.put("horaInicio", AddDataStartTime.text.toString())
            parametros.put("horaFinal", AddDataFinalTime.text.toString())
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