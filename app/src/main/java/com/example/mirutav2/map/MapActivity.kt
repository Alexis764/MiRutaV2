package com.example.mirutav2.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.R
import com.example.mirutav2.home.stop.StopModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    //Variables
    private var idRut: Long = 0
    private lateinit var queue: RequestQueue



    //Variables para paradas
    private var stopList = ArrayList<StopModel>()
    private var markerList = mutableListOf<Marker>()



    //Variable para manejo de maps
    private lateinit var map: GoogleMap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initComponent()
        initListeners()
        initArguments()
        initUi()

    }



    //Conexion de componentes a vista
    private fun initComponent() {
        queue = Volley.newRequestQueue(this)
    }



    //Inicio de funciones click
    private fun initListeners() {

    }



    //Trae el valor a partir de los argumentos enviados
    private fun initArguments() {
        idRut = intent.extras?.getLong("idRut") ?: 0
    }



    //Iniciar los componentes de la interfaz respecto a valores predeterminados
    private fun initUi() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }



    //Funcion para cuando el mapa este listo
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        queue.add(getStopsRoute())
    }



    //Funcion para crear los marcadores con las paradas
    private fun getStopsRoute(url: String = "http://192.168.20.23:8080/parada/listarRut/$idRut"): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                stopList.add(createStop(jsonArray[i] as JSONObject))
                i++
            }

            createMarkers(stopList)

        }, {error ->
            Log.e("Volley_getStops_map", error.toString())
        })

        return stringRequest
    }

    //Funcion para crear una parada
    private fun createStop(jsonStop: JSONObject): StopModel {
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

    //Funcion para crear un marcador por cada parada
    private fun createMarkers(stopList: ArrayList<StopModel>) {
        stopList.forEach {
            val coordinates = LatLng(it.latitudPar, it.longitudPar)
            val marker = map.addMarker(MarkerOptions().position(coordinates).title(it.nombrePar))
            if (marker != null) markerList.add(marker)
        }
    }
}