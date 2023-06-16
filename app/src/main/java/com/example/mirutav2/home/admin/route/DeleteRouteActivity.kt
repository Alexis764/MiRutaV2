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

class DeleteRouteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_route)

        initVariable()
        deleteData()
        backAdmin()
    }


    // Variables
    private lateinit var DeleteDataID : EditText

    // Variable Boton
    private lateinit var btnDeleteRouteData : Button
    private lateinit var btnCloseRoute : ImageButton


    private fun initVariable() {
        // Variable
        DeleteDataID = findViewById(R.id.DeleteDataID)

        // Botones
        btnDeleteRouteData = findViewById(R.id.btnDeleteRouteData)
        btnCloseRoute = findViewById(R.id.btnCloseRoute)
    }

    // Elimina el dato
    private fun deleteData() {
        btnDeleteRouteData.setOnClickListener {
            val url = URLBASE + "/ruta/eliminar/" + DeleteDataID.text.toString()
            val queue = Volley.newRequestQueue(this)
            queue.add(deleteRoute(url))
        }
    }

    private fun backAdmin() {
        btnCloseRoute.setOnClickListener {
            onBackPressed()
        }
    }

    private fun deleteRoute(url: String): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("idRut", DeleteDataID.text.toString())
        } catch (e: JSONException) {
            Log.e("DeleteRouteJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, parametros,
            { response ->
                Toast.makeText(DeleteDataID.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
            },
            { error ->
                Log.e("DeleteRoute", error.toString())
            }
        )

        return jsonObjectRequest
    }


}