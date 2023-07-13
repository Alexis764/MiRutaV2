package com.example.mirutav2.home.admin.stop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity
import com.example.mirutav2.R
import org.json.JSONException
import org.json.JSONObject

class ListStopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_stop)
    }

    /*private fun deleteData() {
        btnDeleteStopData.setOnClickListener {
            val url = MainActivity.URLBASE + "/parada/eliminar/" + idPar.text.toString()
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
    }*/
}