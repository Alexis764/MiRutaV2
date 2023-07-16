package com.example.mirutav2.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.R
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.home.BusFragment.Companion.PLACABUS
import com.example.mirutav2.home.HomeActivity.Companion.userDriverId
import com.example.mirutav2.home.RouteFragment.Companion.IDRUT
import com.example.mirutav2.home.bus.BusModel
import com.example.mirutav2.home.stop.StopModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }


    //Variables
    private var idRut: Long = 0
    private lateinit var placaBus: String
    private lateinit var queue: RequestQueue
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var timerLocation: CountDownTimer



    //Variables para paradas
    private var stopList = ArrayList<StopModel>()
    private var markerStopList = mutableListOf<Marker>()

    //Variables para giros
    private var twistsList = ArrayList<TwistsModel>()

    //Variables para buses
    private var busList = ArrayList<BusModel>()
    private var markerBusList = mutableListOf<Marker>()



    //Variable para manejo de maps
    private lateinit var map: GoogleMap
    private lateinit var customInfoWindowAdapter: CustomInfoWindowAdapter



    //Variables para componentes
    private lateinit var btnCenterRoute: FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initArguments()
        initComponent()
        initUi()
        initListeners()

    }



    //Conexion de componentes a vista
    private fun initComponent() {
        queue = Volley.newRequestQueue(this)
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        timerLocation = object : CountDownTimer(10000, 1000) {
            override fun onTick(time: Long) {
                return
            }

            override fun onFinish() {
                if (placaBus.isNotEmpty()) {
                    if (isLocationPermissionGranted()) getLastKnownLocation() else requestLocationPermission()

                } else {
                    markerBusList.forEach { it.remove() }
                    busList.clear()
                    queue.add(getBusRoute())
                }

                timerLocation.start()
            }
        }

        btnCenterRoute = findViewById(R.id.btnCenterRoute)
    }



    //Inicio de funciones click
    private fun initListeners() {
        btnCenterRoute.setOnClickListener {
            val firstStop = stopList.first()
            val coordinates = LatLng(firstStop.latitudPar, firstStop.longitudPar)

            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(coordinates, 14f),
                2000,
                null
            )
        }
    }



    //Trae el valor a partir de los argumentos enviados
    private fun initArguments() {
        idRut = intent.extras?.getLong(IDRUT) ?: 0
        placaBus = intent.extras?.getString(PLACABUS).orEmpty()
    }



    //Iniciar los componentes de la interfaz respecto a valores predeterminados
    private fun initUi() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        timerLocation.start()
    }



    //Funcion para cuando el mapa este listo
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        queue.add(getStopsRoute())
        queue.add(getTwistsRoute())
        queue.add(getBusRoute())
        enableLocation()

        customInfoWindowAdapter = CustomInfoWindowAdapter(this)
        map.setInfoWindowAdapter(customInfoWindowAdapter)
    }



    //Funciones para crear los marcadores con las paradas
    //Funcion que trae la informacion de las paradas
    private fun getStopsRoute(url: String = "$URLBASE/parada/listarRut/$idRut"): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                stopList.add(createStop(jsonArray[i] as JSONObject))
                i++
            }

            createStopMarkers(stopList)

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
    private fun createStopMarkers(stopList: ArrayList<StopModel>) {
        stopList.forEach {
            val coordinates = LatLng(it.latitudPar, it.longitudPar)
            val marker = map.addMarker(MarkerOptions()
                .position(coordinates)
                .title(it.nombrePar)
            )
            marker?.tag = it.imgPar
            if (marker != null) markerStopList.add(marker)
        }
    }



    //Funciones para crear la ruta con los giros de las ruta
    //Funcion que trae la informacion de los giros
    private fun getTwistsRoute(url: String = "$URLBASE/giro/listarRut/$idRut"): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                twistsList.add(createTwist(jsonArray[i] as JSONObject))
                i++
            }

            createPolyline(twistsList)

        }, {error ->
            Log.e("Volley_getStops_map", error.toString())
        })

        return stringRequest
    }

    //Funcion para crear un giro
    private fun createTwist(jsonTwists: JSONObject): TwistsModel {
        val idGir = jsonTwists.getLong("idGir")
        val latitudGir = jsonTwists.getDouble("latitudGir")
        val longitudGir = jsonTwists.getDouble("longitudGir")
        val ordenGirHasRut = jsonTwists.getLong("ordenGirHasRut")

        return TwistsModel(
            idGir,
            latitudGir,
            longitudGir,
            ordenGirHasRut
        )
    }

    //Funcion para crear la polilinea respecto a los giros
    private fun createPolyline(twistsList: ArrayList<TwistsModel>) {
        val twistsListOrdered = twistsList.sortedBy { it.ordenGirHasRut }
        val latLngTwists = ArrayList<LatLng>()

        twistsListOrdered.forEach {
            latLngTwists.add(LatLng(it.latitudGir, it.longitudGir))
        }

        map.addPolyline(PolylineOptions()
            .addAll(latLngTwists)
            .width(20f)
            .color(ContextCompat.getColor(this, R.color.background))
            .startCap(RoundCap())
            .endCap(RoundCap())
        )
    }



    //Funciones para crear los marcadores con los buses
    //Funcion que trae la informacion de los buses
    private fun getBusRoute(url: String = "$URLBASE/bus/listarRut/$idRut"): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                busList.add(createBus(jsonArray[i] as JSONObject))
                i++
            }

            createBusMarkers(busList)

        }, {error ->
            Log.e("Volley_getStops_map", error.toString())
        })

        return stringRequest
    }

    //Funcion para crear una parada
    private fun createBus(jsonBus: JSONObject): BusModel {
        val placaBus = jsonBus.getString("placaBus")
        val longitudBus = jsonBus.getDouble("longitudBus")
        val latitudBus = jsonBus.getDouble("latitudBus")

        return BusModel(
            placaBus,
            longitudBus,
            latitudBus
        )
    }

    //Funcion para crear un marcador por cada parada
    private fun createBusMarkers(busList: ArrayList<BusModel>) {
        busList.forEach {
            val coordinates = LatLng(it.latitudBus, it.longitudBus)
            val marker = map.addMarker(MarkerOptions()
                .position(coordinates)
                .title(it.placaBus)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerbus))
            )
            if (marker != null) markerBusList.add(marker)
        }
    }



    //Funciones para manejo del permiso de ubicacion
    //Funcion para verficar si el metodo esta aceptado o no
    private fun isLocationPermissionGranted(): Boolean {
        return !(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }

    //Funcion para solicitar permiso de ubicacion
    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
            goLastLocation()

        } else {
            requestLocationPermission()
        }
    }

    //Funcion para pedir permiso de ubicacion
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            Toast.makeText(this, "Para activar localizacion acepta los permisos desde ajustes", Toast.LENGTH_SHORT).show()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    //Funcion para controlar la respuesta del usuario respecto a los permisos solicitados
    @SuppressLint("MissingPermission", "MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (
                grantResults.isNotEmpty() &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)
            ) {
                map.isMyLocationEnabled = true
                goLastLocation()

            } else {
                Toast.makeText(this, "Para activar localizacion acepta los permisos desde ajustes", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    //Funcion para controlar cambios en los permisos de localizacion
    @SuppressLint("MissingPermission")
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::map.isInitialized) return
        if (!isLocationPermissionGranted()) {
            map.isMyLocationEnabled = false
            Toast.makeText(this, "Para activar localizacion acepta los permisos desde ajustes", Toast.LENGTH_SHORT).show()
        }
    }

    //Funcion para ir a la ultima ubicacion del usuario automaticamente
    private fun goLastLocation() {
        try {
            val locationResult = locationProviderClient.lastLocation

            locationResult.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val lastKnownLocation = task.result

                    if (lastKnownLocation != null) {
                        val coordinates =LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)

                        map.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(coordinates, 14f),
                            4000,
                            null
                        )
                    }

                } else {
                    Log.d("goLastLocation", "La ubicacion actual es null, usando valores predeterminados")
                    Log.e("goLastLocation", "Exception: %s", task.exception)

                    val coordinates =LatLng(stopList.first().latitudPar, stopList.first().longitudPar)
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(coordinates, 14f),
                        4000,
                        null
                    )
                    map.isMyLocationEnabled = false
                }
            }

        } catch (e: SecurityException) {
            Log.e("goLastLocation Exception: %s", e.message, e)
        }
    }



    //Funcion para que la ultima localizacion del conductor se mande a la base de datos
    private fun sendLastLocation(lastKnownLocation: Location, url: String = "$URLBASE/bus/updateLocation"): JsonObjectRequest {
        val parameters = JSONObject()

        try {
            parameters.put("placaBus", placaBus)
            parameters.put("longitudBus", lastKnownLocation.longitude)
            parameters.put("latitudBus", lastKnownLocation.latitude)
            parameters.put("identificacionCon", userDriverId)

        } catch (e: JSONException) {
            Log.e("sendLastLocation_JSON", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url, parameters, {response ->
            Toast.makeText(this, response.getString("respuesta"), Toast.LENGTH_SHORT).show()

        }, {error ->
            Log.e("sendLastLocation_REQUEST", error.toString())
        })

        return jsonObjectRequest
    }

    //Funcion para obtener la ultima localizacion del dispositivo
    private fun getLastKnownLocation() {
        try {
            val locationResult = locationProviderClient.lastLocation

            locationResult.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val lastKnownLocation = task.result

                    if (lastKnownLocation != null) {
                        queue.add(sendLastLocation(lastKnownLocation))
                    }
                }
            }

        } catch (e: SecurityException) {
            Log.e("getLastKnownLocation Exception: %s", e.message, e)
        }
    }



    //Modificacion al metodo onBackPressed para finalizar el contador cuando se retrocede
    override fun onBackPressed() {
        super.onBackPressed()
        timerLocation.cancel()
    }
}