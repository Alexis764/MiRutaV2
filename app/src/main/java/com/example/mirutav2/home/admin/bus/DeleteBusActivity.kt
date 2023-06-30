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

class DeleteBusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_bus)


        initVariable()
        deleteData()
        backAdmin()
    }


    // Variables
    private lateinit var DeleteDataPlateBus : EditText

    // Variables Botones
    private lateinit var btnDeleteBusData : Button
    private lateinit var btnCloseBus : ImageButton

    private fun initVariable() {
        // Variable
        DeleteDataPlateBus = findViewById(R.id.DeleteDataPlateBus)

        // Botones
        btnDeleteBusData = findViewById(R.id.btnDeleteBusData)
        btnCloseBus = findViewById(R.id.btnCloseBus)
    }


    // Eliminar el dato
    private fun deleteData() {
        btnDeleteBusData.setOnClickListener {
            val url = URLBASE +"/bus/eliminar/"+ DeleteDataPlateBus.text.toString()
            val queue = Volley.newRequestQueue(this)
            queue.add(deleteBus(url))
        }
    }

    // Navegacion de vuelta a Admin
    private fun backAdmin() {
        btnCloseBus.setOnClickListener {
            onBackPressed()
        }
    }


    // Funcion eliminar Bus
    private fun deleteBus(url: String): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("placaBus", DeleteDataPlateBus.text.toString())
        } catch (e: JSONException) {
            Log.e("DeleteBusJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, parametros,
            { response ->
                Toast.makeText(DeleteDataPlateBus.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
            },
            { error ->
                Log.e("DeleteBus", error.toString())
            }
        )

        return jsonObjectRequest
    }
}