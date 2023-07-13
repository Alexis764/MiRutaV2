package com.example.mirutav2.home.admin.stop

import android.content.Context
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

class AddStopActivity : AppCompatActivity() {

    // Variables Datos
    private lateinit var AddDataNameStop : TextInputEditText
    private lateinit var AddDataAddress : TextInputEditText
    private lateinit var AddDataLength : TextInputEditText
    private lateinit var AddDataLatitude : TextInputEditText
    private lateinit var AddDataImgStop : TextInputEditText

    // Variables Botones
    private lateinit var btnCloseStop : ImageButton
    private lateinit var btnSendStopData : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stop)

        initData()
        backAdmin()
        sendData()

    }


    // Recoleccion de datos
    private fun initData() {
        AddDataNameStop = findViewById(R.id.AddDataNameStop)
        AddDataAddress = findViewById(R.id.AddDataAddress)
        AddDataLength = findViewById(R.id.AddDataLength)
        AddDataLatitude = findViewById(R.id.AddDataLatitude)
        AddDataImgStop = findViewById(R.id.AddDataImgStop)

        // Botones

        btnSendStopData = findViewById(R.id.btnSendStopData)
        btnCloseStop = findViewById(R.id.btnCloseStop)
    }

    // Navegacion de vuelta a Admin
    private fun backAdmin() {
        btnCloseStop.setOnClickListener {
            onBackPressed()
        }
    }

    private fun sendData() {
        btnSendStopData.setOnClickListener {

            // Limpiar espacios en blanco
            val nombrePara = AddDataNameStop.text.toString().trim()
            val direccionPara = AddDataAddress.text.toString().trim()
            val longitud = AddDataLength.text.toString().trim()
            val latitud = AddDataLatitude.text.toString().trim()
            val imgParada = AddDataImgStop.text.toString().trim()

            if(nombrePara.isNotEmpty() && direccionPara.isNotEmpty() && longitud.isNotEmpty() && latitud.isNotEmpty() && imgParada.isNotEmpty()){
                val url = "$URLBASE/parada/guardar"
                val queue = Volley.newRequestQueue(this)
                queue.add(AddStop(url))
            }else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funcion agregar parada
    private fun AddStop(url: String): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("nombrePar", AddDataNameStop.text.toString())
            parametros.put("direccionPar", AddDataAddress.text.toString())
            parametros.put("longitudPar", AddDataLength.text.toString())
            parametros.put("latitudPar", AddDataLatitude.text.toString())
            parametros.put("imgPar", AddDataImgStop.text.toString())
        } catch (e: JSONException){
            Log.e("AddStopJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST,url,parametros, { response ->
            Toast.makeText(AddDataNameStop.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
        }, { error -> Log.e("AddStop", error.toString())
        })


        return jsonObjectRequest
    }
}