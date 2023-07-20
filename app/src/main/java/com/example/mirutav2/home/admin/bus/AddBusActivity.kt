package com.example.mirutav2.home.admin.bus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonArrayRequest
import com.example.mirutav2.home.admin.driver.DriverModelAdmin

class AddBusActivity : AppCompatActivity() {

    // Variable Datos
    private lateinit var AddDataPlate : EditText
    private lateinit var AddDataConductor: Spinner

    // Variable Botones
    private lateinit var btnCloseBus : ImageButton
    private lateinit var btnSendBusData : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bus)

        initData()
        backAdmin()
        sendData()
        getConductoresFromAPI()

    }

    // Recoleccion de datos
    private fun initData() {
        AddDataPlate = findViewById(R.id.AddDataPlate)
        AddDataConductor = findViewById(R.id.AddDataConductor)

        // Botones
        btnSendBusData = findViewById(R.id.btnSendBusData)
        btnCloseBus = findViewById(R.id.btnCloseBus)
    }

    // Navegacion de vuelta a Admin
    private fun backAdmin() {
        btnCloseBus.setOnClickListener {
            onBackPressed()
        }
    }

    // Enviar datos a BD
    private fun sendData() {
        btnSendBusData.setOnClickListener {

            val placa = AddDataPlate.text.toString().trim()
            val identificacionConductor = conductorList[AddDataConductor.selectedItemPosition].identificacionCon

            if (placa.isNotEmpty()) {
                val url = "$URLBASE/bus/guardar"
                val queue = Volley.newRequestQueue(this)
                queue.add(AddBus(url, identificacionConductor))
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun AddBus(url: String, identificacionConductor: Long): JsonObjectRequest {
        val parametros = JSONObject()
        val AddLatitud = 10.00
        val AddLongitud = 10.00

        try {
            parametros.put("placaBus", AddDataPlate.text.toString())
            parametros.put("longitudBus", AddLongitud)
            parametros.put("latitudBus", AddLatitud)
            parametros.put("identificacionCon", identificacionConductor)

        } catch (e: JSONException) {
            Log.e("AddBusJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parametros, { response ->
            Toast.makeText(AddDataPlate.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()

        }, { error ->
            Log.e("AddBus", error.toString())
        })

        return jsonObjectRequest
    }

    private val conductorList: MutableList<DriverModelAdmin> = mutableListOf()

    private fun getConductoresFromAPI() {
        Log.d("AddBusActivity", "Obteniendo conductores desde la API...")
        val url = "$URLBASE/conductor/listar"
        val queue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    conductorList.clear()
                    for (i in 0 until response.length()) {
                        val conductorJSON = response.getJSONObject(i)
                        val identificacionCon = conductorJSON.getLong("identificacionCon")
                        val conductor = DriverModelAdmin(identificacionCon)
                        conductorList.add(conductor)
                    }

                    updateConductorSpinner()
                } catch (e: JSONException) {
                    Log.e("AddBusActivity", "Error al procesar la respuesta JSON: ${e.message}")
                }
            },
            { error ->
                Log.e("AddBusActivity", "Error en la solicitud HTTP: ${error.message}")
            })

        queue.add(jsonArrayRequest)
    }

    private fun updateConductorSpinner() {
        val conductorIds = conductorList.map { it.identificacionCon.toString() } // Lista de identificaciones de conductores como Strings
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conductorIds)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        AddDataConductor.adapter = adapter
    }
}
