package com.example.mirutav2.home.admin.route

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
import com.example.mirutav2.home.route.RouteModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Variables Configuracion Recycler View
private lateinit var rvlistRoute : RecyclerView
private lateinit var routeAdapter : RouteAdminAdapter

private var routeList = ArrayList<RouteModel>()

// Variable boton
private lateinit var btnCloseRoute : ImageButton

class ListRouteActivity : AppCompatActivity(), RouteAdminListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_route)

        val queue = Volley.newRequestQueue(this)
        queue.add(listRoute())

        initVariables()
        backAdmin()

    }

    private fun initVariables() {
        btnCloseRoute = findViewById(R.id.btnCloseRoute)

        rvlistRoute = findViewById(R.id.rvlistRoute)
    }

    private fun backAdmin() {
        btnCloseRoute.setOnClickListener {
            onBackPressed()
        }
    }

    // Funcion crear Route
    private fun createRoute(jsonRoute : JSONObject) : RouteModel{
        val idRut = jsonRoute.getLong("idRut")
        val lugarInicioRut = jsonRoute.getString("lugarInicioRut")
        val lugarDestinoRut = jsonRoute.getString("lugarDestinoRut")
        val diasDisponiblesRut = jsonRoute.getString("diasDisponiblesRut")

        val horaInicioRut = jsonRoute.getString("horaInicioRut")
        val horaFinalRut = jsonRoute.getString("horaFinalRut")

        return RouteModel(
            idRut,
            lugarInicioRut,
            lugarDestinoRut,
            diasDisponiblesRut,
            horaInicioRut,
            horaFinalRut
        )
    }

    // Funcion traer Rutas
    private fun listRoute(url : String = "$URLBASE/ruta/listar"): StringRequest{
        val stringRequest = StringRequest(Request.Method.GET, url,{ response ->
            val jsonArray = JSONArray(response)

            // Limpiar la lista
            routeList.clear()

            var i = 0
            val sizeArray = jsonArray.length()
            while(i < sizeArray){
                routeList.add(createRoute(jsonArray[i] as JSONObject))
                i++
            }

            torvlistRoute(routeList)

        },{ error ->
            Log.e("Volley_getRouteAdmin", error.toString())
        })

        return stringRequest
    }

    private fun torvlistRoute(routeList: MutableList<RouteModel>) {
        routeAdapter = RouteAdminAdapter(routeList,this)
        rvlistRoute.layoutManager = LinearLayoutManager(this)
        rvlistRoute.adapter = routeAdapter
    }

    override fun onBtnUpdateClicked(routeModel: RouteModel) {

    }

    override fun onBtnDeleteClicked(routeModel: RouteModel) {
        val url = URLBASE + "/ruta/eliminar/${routeModel.idRut}"
        val queue = Volley.newRequestQueue(this)
        queue.add(deleteRoute(url,routeModel))
    }

    // Funcion eliminar Ruta
    private fun deleteRoute(url: String, routeModel: RouteModel): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("idRut", routeModel.idRut.toString())
        } catch (e: JSONException) {
            Log.e("DeleteRouteJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, parametros,
            { response ->
                Toast.makeText(this, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
                // Eliminar el elemento de la lista después de eliminarlo en el servidor
                routeList.remove(routeModel)
            },
            { error ->
                Log.e("DeleteRoute", error.toString())
            }
        )

        return jsonObjectRequest
    }
}