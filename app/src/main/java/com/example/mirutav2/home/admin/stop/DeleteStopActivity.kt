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
import com.example.mirutav2.R
import com.example.mirutav2.MainActivity.Companion.URLBASE
import org.json.JSONException
import org.json.JSONObject

class DeleteStopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_stop)

        initVariable()
        deleteData()
        backAdmin()
    }

    // Variables
    private lateinit var idPar : EditText

    // Variables Botones
    private lateinit var btnDeleteStopData : Button
    private lateinit var btnCloseStop : ImageButton


    // Inicializacion de Variables
    private fun initVariable() {
        // Variable
        idPar = findViewById(R.id.DeleteDataidParStop)

        // Botones
        btnDeleteStopData = findViewById(R.id.btnDeleteStopData)
        btnCloseStop = findViewById(R.id.btnCloseStop)

    }

    // Eliminar el dato
    private fun deleteData() {
        btnDeleteStopData.setOnClickListener {
            val url = URLBASE + "/parada/eliminar/" + idPar.text.toString()
            val queue = Volley.newRequestQueue(this)
            queue.add(deleteStop(url))
        }
    }

    // Navegacion de vuelta a Admin
    private fun backAdmin() {
        btnCloseStop.setOnClickListener {
            onBackPressed()
        }
    }

    // Funcion eliminar Parada
    private fun deleteStop(url: String): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("idPar", idPar.text.toString())
        } catch (e: JSONException) {
            Log.e("DeleteStopJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, parametros,
            { response ->
                Toast.makeText(idPar.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
            },
            { error ->
                Log.e("DeleteStop", error.toString())
            }
        )

        return jsonObjectRequest
    }


}