package com.example.mirutav2.home.admin.bus

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
import com.example.mirutav2.home.bus.BusModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


// Variables Configuracion Recycler View
private lateinit var rvlistBus : RecyclerView
private lateinit var busAdapter : BusAdminAdapter

private var busList = ArrayList<BusModel>()

// Variable Boton
private lateinit var btnCloseBus : ImageButton

class ListBusActivity : AppCompatActivity(), BusAdminListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_bus)

        val queue = Volley.newRequestQueue(this)
        queue.add(listBus())

        initVariables()
        backAdmin()

    }

    private fun initVariables() {
        btnCloseBus = findViewById(R.id.btnCloseBus)

        rvlistBus = findViewById(R.id.rvlistBus)
    }

    private fun backAdmin() {
        btnCloseBus.setOnClickListener {
            onBackPressed()
        }
    }

    // Funcion crear Bus
    private fun createBus (jsonBus : JSONObject) : BusModel{

        val placaBus = jsonBus.getString("placaBus")
        val longitudBus = jsonBus.getDouble("longitudBus")
        val latitudBus = jsonBus.getDouble("latitudBus")
        /*val identificacionUsu = jsonBus.getLong("identificacionUsu")*/

        return BusModel(
            placaBus,
            longitudBus,
            latitudBus,
            /*identificacionUsu*/
        )
    }

    // Funcion traer Buses
    private fun listBus(url : String = "$URLBASE/bus/listar") : StringRequest{
        val stringRequest = StringRequest(Request.Method.GET, url,{ response ->
            val jsonArray = JSONArray(response)

            // Limpiar la Lista
            busList.clear()

            var i = 0
            val sizeArray = jsonArray.length()
            while(i < sizeArray){
                busList.add(createBus(jsonArray[i] as JSONObject))
                i++
            }

            torvlistBus(busList)

        },{error ->
            Log.e("Volley_getBusAdmin", error.toString())
        })

        return stringRequest
    }

    // Funcion asignar adaptador
    private fun torvlistBus(busList: MutableList<BusModel>) {
        busAdapter = BusAdminAdapter(busList,this)
        rvlistBus.layoutManager = LinearLayoutManager(this)
        rvlistBus.adapter = busAdapter
    }

    override fun onBtnUpdateClicked(busModel: BusModel) {

    }

    override fun onBtnDeleteClicked(busModel: BusModel) {
        val url = URLBASE +"/bus/eliminar/${busModel.placaBus}"
        val queue = Volley.newRequestQueue(this)
        queue.add(deleteBus(url, busModel))
    }

    // Funcion eliminar Bus
    private fun deleteBus(url: String, busModel: BusModel): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("placaBus", busModel.placaBus)
        } catch (e: JSONException) {
            Log.e("DeleteBusJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, parametros,
            { response ->
                Toast.makeText(this, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
                // Eliminar el elemento de la lista después de eliminarlo en el servidor
                busList.remove(busModel)
            },
            { error ->
                Log.e("DeleteBus", error.toString())
            }
        )

        return jsonObjectRequest
    }
}