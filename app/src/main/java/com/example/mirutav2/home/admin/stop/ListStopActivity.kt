package com.example.mirutav2.home.admin.stop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import com.example.mirutav2.home.stop.StopModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Variables Configuracion Recycler View
private lateinit var rvlistStop : RecyclerView
private lateinit var stopAdapter : StopAdminAdapter
private var stopList = ArrayList<StopModel>()

// Variable Boton
private lateinit var btnCloseStop : ImageButton

class ListStopActivity : AppCompatActivity(), StopAdminListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_stop)
        
        val queue = Volley.newRequestQueue(this)
        queue.add(listStop())

        initVariables()
        backAdmin()
    }

    private fun initVariables() {
        btnCloseStop = findViewById(R.id.btnCloseStop)

        rvlistStop = findViewById(R.id.rvlistStop)
    }

    private fun backAdmin() {
        btnCloseStop.setOnClickListener {
            onBackPressed()
        }
    }

    // Funcion crear Stop
    private fun createStop(jsonStop : JSONObject) : StopModel{
        val idPar = jsonStop.getLong("idPar")
        val nombrePar = jsonStop.getString("nombrePar")
        val direccionPar = jsonStop.getString("direccionPar")
        val longitudPar = jsonStop.getDouble("longitudPar")
        val latitudPar = jsonStop.getDouble("latitudPar")
        val imgPar = jsonStop.getString("imgPar")

        return StopModel(
            idPar,
            nombrePar,
            direccionPar,
            longitudPar,
            latitudPar,
            imgPar
        )
    }
    private fun listStop(url : String = "$URLBASE/parada/listar"): StringRequest{
        val stringRequest = StringRequest(Request.Method.GET, url,{response ->
            val jsonArray = JSONArray(response)

            // Limpiar la lista
            stopList.clear()

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray){
                stopList.add(createStop(jsonArray[i] as JSONObject))
                i++
            }

            torvlistStop(stopList)

        },{ error ->
            Log.e("Volley_getStopAdmin", error.toString())
        })

        return stringRequest
    }

    private fun torvlistStop(stopList: MutableList<StopModel>) {
        stopAdapter = StopAdminAdapter(stopList,this)
        rvlistStop.layoutManager = LinearLayoutManager(this)
        rvlistStop.adapter = stopAdapter

    }

    override fun onBtnUpdateCliecked(stopModel: StopModel) {
        val intent = Intent(this, UpdateStopActivity::class.java)
        intent.putExtra("stopId", stopModel.idPar)
        startActivity(intent)
    }

    override fun onBtnDeleteClicked(stopModel: StopModel) {
        val url = "$URLBASE/parada/eliminar/${stopModel.idPar}"
        val queue = Volley.newRequestQueue(this)
        queue.add(deleteStop(url,stopModel))
    }

    // Funcion eliminar Parada
    private fun deleteStop(url: String, stopModel: StopModel): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("idPar", stopModel.idPar.toString())
        } catch (e: JSONException) {
            Log.e("DeleteStopJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, parametros,
            { response ->
                Toast.makeText(this, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
                // Eliminar el elemento de la lista después de eliminarlo en el servidor
                stopList.remove(stopModel)
            },
            { error ->
                Log.e("DeleteStop", error.toString())
            }
        )

        return jsonObjectRequest
    }
}