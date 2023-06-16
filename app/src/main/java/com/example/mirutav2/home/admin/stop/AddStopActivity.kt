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
import com.example.mirutav2.R
import com.example.mirutav2.home.HomeActivity.Companion.URLBASE
import org.json.JSONException
import org.json.JSONObject

class AddStopActivity : AppCompatActivity() {

    // Variables Datos
    private lateinit var AddDataNameStop : EditText
    private lateinit var AddDataAddress : EditText
    private lateinit var AddDataLength : EditText
    private lateinit var AddDataLatitude : EditText
    private lateinit var AddDataImgStop : EditText

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
            val url = URLBASE+"/parada/agregar"
            val queue = Volley.newRequestQueue(this)
            queue.add(AddStop(url))
        }
    }

    // Funcion agregar parada
    private fun AddStop(url: String): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("nombrePara", AddDataNameStop.text.toString())
            parametros.put("direccionPara", AddDataAddress.text.toString())
            parametros.put("longitud", AddDataLength.text.toString())
            parametros.put("latitud", AddDataLatitude.text.toString())
            parametros.put("imgParada", AddDataImgStop.text.toString())
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