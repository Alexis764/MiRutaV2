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
import com.example.mirutav2.MainActivity
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import org.json.JSONException
import org.json.JSONObject

class UpdateRouteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_route)


        initData()
        backAdmin()
        updateData()
    }

    // Variable Botones
    private lateinit var btnCloseRoute : ImageButton
    private lateinit var btnUpdaterRouteData : Button

    // Variables Datos
    private lateinit var UpdateDataStartLocation : EditText
    private lateinit var UpdateDataFinalLocation : EditText
    private lateinit var UpdateDataStartTime : EditText
    private lateinit var UpdateDataFinalTime : EditText
    private lateinit var UpdateDataDays : EditText


    // Recoleccion de datos
    private fun initData() {

        // Datos
        UpdateDataStartLocation = findViewById(R.id.UpdateDataStartLocation)
        UpdateDataFinalLocation = findViewById(R.id.UpdateDataFinalLocation)
        UpdateDataStartTime = findViewById(R.id.UpdateDataStartTime)
        UpdateDataFinalTime = findViewById(R.id.UpdateDataFinalTime)
        UpdateDataDays = findViewById(R.id.UpdateDataDays)


        // Botones

        btnCloseRoute = findViewById(R.id.btnCloseRoute)
        btnUpdaterRouteData = findViewById(R.id.btnUpdateRouteData)

    }

    // Navegacion de vuelta a Admin
    private fun backAdmin() {
        btnCloseRoute.setOnClickListener {
            onBackPressed()
        }
    }

    // Actualizar Datos
    private fun updateData() {
        btnUpdaterRouteData.setOnClickListener {
            val url = "$URLBASE/ruta/actualizar"
            val queue = Volley.newRequestQueue(this)
            queue.add(updateRoute(url))
        }
    }

    private fun updateRoute(url: String): JsonObjectRequest {
        val parametros = JSONObject()
        val routeId = intent.getLongExtra("idRut", -1)

        try {
            parametros.put("idRut", routeId)
            parametros.put("lugarInicioRut", UpdateDataStartLocation.text.toString())
            parametros.put("lugarDestinoRut",UpdateDataFinalLocation.text.toString())
            parametros.put("horaInicioRut",UpdateDataStartTime.text.toString())
            parametros.put("horaFinalRut",UpdateDataFinalTime.text.toString())
            parametros.put("diasDisponiblesRut", UpdateDataDays.text.toString())
        }catch (e:JSONException){
            Log.e("UpdateRouteJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url, parametros,
            { response ->
                Toast.makeText(
                    UpdateDataStartLocation.context,
                    response.getString("respuesta"),
                    Toast.LENGTH_SHORT
                ).show()
            }, { error ->
                Log.e("UpdateRoute", error.toString())
            })

        return jsonObjectRequest
    }
}