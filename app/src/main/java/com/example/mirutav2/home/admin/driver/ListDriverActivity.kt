package com.example.mirutav2.home.admin.driver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import org.json.JSONException
import org.json.JSONObject

// Variables Configuracion Recycler View
private lateinit var rvlistDriver : RecyclerView
private lateinit var driverAdapter : DriverAdminAdapter

private var driverList = ArrayList<DriverModelAdmin>()
// Variable Boton
private lateinit var btnCloseDriver : ImageButton

class ListDriverActivity : AppCompatActivity(), DriverAdminListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_driver)


        initVariables()
        backAdmin()
        listDriver()
        
    }

    private fun initVariables() {
        btnCloseDriver = findViewById(R.id.btnCloseDriver)

        rvlistDriver = findViewById(R.id.rvlistDriver)
    }

    private fun backAdmin() {
        btnCloseDriver.setOnClickListener {
            onBackPressed()
        }
    }
    
    // Funcion crear Conductor
    private fun createDriver(jsonDriver : JSONObject) : DriverModelAdmin {

        val identificacionCon = jsonDriver.getLong("identificacionCon")

        return DriverModelAdmin(
            identificacionCon
            
        )

    }

    // Funcion traer Conductores
    private fun listDriver(url : String = "$URLBASE/conductor/listar") {
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                // Limpiar la Lista
                driverList.clear()

                var i = 0
                val sizeArray = response.length()
                while (i < sizeArray) {
                    driverList.add(createDriver(response.getJSONObject(i)))
                    i++
                }

                torvlistDriver(driverList)
            },
            { error ->
                Log.e("Volley_getDriverAdmin", error.toString())
            })

        queue.add(jsonArrayRequest)
    }


    // Funcion asignar adaptador
    private fun torvlistDriver (driverList: MutableList<DriverModelAdmin>){
        driverAdapter = DriverAdminAdapter(driverList,this)
        rvlistDriver.layoutManager = LinearLayoutManager(this)
        rvlistDriver.adapter = driverAdapter
    }


    override fun onBtnDeleteClicked(driverModelAdmin: DriverModelAdmin) {
        val url = "$URLBASE/conductor/eliminar/${driverModelAdmin.identificacionCon}"
        val queue = Volley.newRequestQueue(this)
        queue.add(deleteDriver(url, driverModelAdmin))
    }

    // Funcion eliminar Usuario
    private fun deleteDriver(url: String, driverModelAdmin: DriverModelAdmin): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("identificacionCon", driverModelAdmin.identificacionCon.toString())
        } catch (e: JSONException) {
            Log.e("DeleteDriverJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, parametros,
            { response ->
                Toast.makeText(this, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
                // Eliminar el elemento de la lista después de eliminarlo en el servidor
                driverList.remove(driverModelAdmin)
            },
            { error ->
                Log.e("DeleteDriver", error.toString())
            }
        )

        return jsonObjectRequest
    }
}