package com.example.mirutav2.home.admin.stop

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

class UpdateStopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_stop)

        initData()
        backAdmin()
        updateData()
    }

    // Variable Datos
    private lateinit var UpdateNameStop : EditText
    private lateinit var UpdateAddress : EditText
    private lateinit var UpdateLength : EditText
    private lateinit var UpdateLatitude : EditText
    private lateinit var UpdateImgStop : EditText

    // Variable Botones
    private lateinit var btnCloseStop : ImageButton
    private lateinit var btnUpdateStopData : Button


    // Recoleccion de datos
    private fun initData() {

        // Datos
        UpdateNameStop = findViewById(R.id.UpdateDataNameStop)
        UpdateAddress = findViewById(R.id.UpdateDataAddress)
        UpdateLength = findViewById(R.id.UpdateDataLength)
        UpdateLatitude = findViewById(R.id.UpdateDataLatitude)
        UpdateImgStop = findViewById(R.id.UpdateDataImgStop)

        // Botones

        btnUpdateStopData = findViewById(R.id.btnUpdateStopData)
        btnCloseStop = findViewById(R.id.btnCloseStop)
    }

    // Navegacion de vuelta a Admin
    private fun backAdmin() {
        btnCloseStop.setOnClickListener {
            onBackPressed()
        }
    }

    // Actualizar Datos
    private fun updateData() {
        btnUpdateStopData.setOnClickListener {
            val url = "${URLBASE}/parada/actualizar"
            val queue = Volley.newRequestQueue(this)
            queue.add(updateStop(url))
        }
    }

    // Funcion actualizar datos

    private fun updateStop(url: String): JsonObjectRequest {
        val parametros = JSONObject()
        val stopId = intent.getLongExtra("idPar", -1)

        try {
            parametros.put("idPar",stopId)
            parametros.put("nombrePar",UpdateNameStop.text.toString())
            parametros.put("direccionPar",UpdateAddress.text.toString())
            parametros.put("longitudPar",UpdateLength.text.toString())
            parametros.put("latitudPar", UpdateLatitude.text.toString())
            parametros.put("imgPar",UpdateImgStop.text.toString())
        }catch (e:JSONException){
            Log.e("UpdateStopJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url, parametros,
            {response ->
                Toast.makeText(
                    UpdateNameStop.context,
                    response.getString("respuesta"),
                    Toast.LENGTH_SHORT
                ).show()
            },{error ->
                Log.e("UpdateStop", error.toString())
            })

        return jsonObjectRequest
    }


}