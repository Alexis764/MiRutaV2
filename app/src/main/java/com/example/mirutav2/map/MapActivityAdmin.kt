package com.example.mirutav2.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import com.example.mirutav2.home.HomeActivity.Companion.userModel
import com.example.mirutav2.home.HomeFragment.Companion.COD_SEL_IMAGE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.json.JSONException
import org.json.JSONObject

class MapActivityAdmin : AppCompatActivity(), OnMapReadyCallback {

    //Variables
    private lateinit var queue: RequestQueue
    private lateinit var locationProviderClient: FusedLocationProviderClient



    //Variable para manejo de maps
    private lateinit var mapAdmin: GoogleMap



    //Variables para componentes
    private lateinit var btnAddStop: FloatingActionButton
    private lateinit var btnCloseMapAdmin: ImageView

    //Variables para componentes de dialog
    private lateinit var iedAddressStopAdd: TextInputEditText
    private lateinit var iedNameStopAdd: TextInputEditText
    private lateinit var iedLatStopAdd: TextInputEditText
    private lateinit var iedLngStopAdd: TextInputEditText
    private lateinit var iedLinkImageStopAdd: TextInputEditText

    private lateinit var btnUploadImageStop: Button
    private lateinit var btnSaveStop: Button
    private lateinit var btnCloseSaveStop: Button



    //Variables firebase
    private lateinit var mFireStore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private val storagePath = "stop/*"

    private lateinit var imageUrl: Uri
    private val photo = "photo"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_admin)

        initComponent()
        initUi()
        initListeners()

    }



    //Conexion de componentes a vista
    private fun initComponent() {
        queue = Volley.newRequestQueue(this)
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        btnAddStop = findViewById(R.id.btnAddStop)
        btnCloseMapAdmin = findViewById(R.id.btnCloseMapAdmin)

        mFireStore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
    }



    //Iniciar los componentes de la interfaz respecto a valores predeterminados
    private fun initUi() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapAdmin) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }



    //Inicio de funciones click
    private fun initListeners() {
        btnAddStop.setOnClickListener {
            if (::mapAdmin.isInitialized) {
                mapAdmin.setOnMapClickListener {
                    val dialog = Dialog(this)
                    dialog.setContentView(R.layout.dialog_add_stop)
                    dialog.show()
                    dialog.setCanceledOnTouchOutside(false)
                    initComponentDialog(dialog)

                    iedLatStopAdd.setText(it.latitude.toString())
                    iedLngStopAdd.setText(it.longitude.toString())

                    btnUploadImageStop.setOnClickListener {openGallery()}

                    btnSaveStop.setOnClickListener {
                        val iedAddressStopAddIn = iedAddressStopAdd.text.toString()
                        val iedNameStopAddIn = iedNameStopAdd.text.toString()
                        val iedLatStopAddIn = iedLatStopAdd.text.toString()
                        val iedLngStopAddIn = iedLngStopAdd.text.toString()
                        val iedLinkImageStopAddIn = iedLinkImageStopAdd.text.toString()

                        if (iedAddressStopAddIn.isNotEmpty() && iedNameStopAddIn.isNotEmpty() &&
                            iedLatStopAddIn.isNotEmpty() && iedLngStopAddIn.isNotEmpty() &&
                            iedLinkImageStopAddIn.isNotEmpty()) {

                            queue.add(saveStop(iedAddressStopAddIn, iedNameStopAddIn, iedLatStopAddIn, iedLngStopAddIn, iedLinkImageStopAddIn))

                        } else {
                            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    }

                    btnCloseSaveStop.setOnClickListener { dialog.hide() }
                }
            }
        }
        btnCloseMapAdmin.setOnClickListener {onBackPressed()}
    }



    //Funcion para cuando el mapa este listo
    override fun onMapReady(googleMap: GoogleMap) {
        mapAdmin = googleMap
        enableLocation()
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
        if (!::mapAdmin.isInitialized) return
        if (isLocationPermissionGranted()) {
            mapAdmin.isMyLocationEnabled = true
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
                MapActivity.REQUEST_CODE_LOCATION
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
            MapActivity.REQUEST_CODE_LOCATION -> if (
                grantResults.isNotEmpty() &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)
            ) {
                mapAdmin.isMyLocationEnabled = true
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
        if (!::mapAdmin.isInitialized) return
        if (!isLocationPermissionGranted()) {
            mapAdmin.isMyLocationEnabled = false
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
                        val coordinates = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)

                        mapAdmin.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(coordinates, 14f),
                            4000,
                            null
                        )
                    }

                } else {
                    Log.d("goLastLocation", "La ubicacion actual es null, usando valores predeterminados")
                    Log.e("goLastLocation", "Exception: %s", task.exception)

                    val coordinates = LatLng(4.709799339033192, -74.22583784419491)
                    mapAdmin.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(coordinates, 14f),
                        4000,
                        null
                    )
                    mapAdmin.isMyLocationEnabled = false
                }
            }

        } catch (e: SecurityException) {
            Log.e("goLastLocation Exception: %s", e.message, e)
        }
    }



    //Funciones para el detalle de guardar parada
    //Conexion de componentes a vista dialog
    private fun initComponentDialog(dialog: Dialog) {
        iedAddressStopAdd = dialog.findViewById(R.id.iedAddressStopAdd)
        iedNameStopAdd = dialog.findViewById(R.id.iedNameStopAdd)
        iedLatStopAdd = dialog.findViewById(R.id.iedLatStopAdd)
        iedLngStopAdd = dialog.findViewById(R.id.iedLngStopAdd)
        iedLinkImageStopAdd = dialog.findViewById(R.id.iedLinkImageStopAdd)

        btnUploadImageStop = dialog.findViewById(R.id.btnUploadImageStop)
        btnSaveStop = dialog.findViewById(R.id.btnSaveStop)
        btnCloseSaveStop = dialog.findViewById(R.id.btnCloseSaveStop)
    }


    //Funcion para abrir galeria
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, COD_SEL_IMAGE)
    }

    //Funcion para recibir el resultado de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == COD_SEL_IMAGE) {
                imageUrl = data?.data!!
                uploadImage(imageUrl)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //Funcion para subir la imagen a firebase
    private fun uploadImage(imageUrl: Uri) {
        val ruteStoragePhoto = storagePath + "" + photo + "" + iedLatStopAdd.text.toString() + "" + iedLngStopAdd.text.toString() + "" + userModel.idUsu
        val reference = storageReference.child(ruteStoragePhoto)

        reference.putFile(imageUrl).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isSuccessful) {
                if (uriTask.isSuccessful) {
                    uriTask.addOnSuccessListener { uri ->
                        val uriDownload = uri.toString()
                        iedLinkImageStopAdd.setText(uriDownload)
                        Toast.makeText(this, "Foto subida con exito", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    //Funcion para guardar la parada en la api
    private fun saveStop(iedAddressStopAddIn: String,
                         iedNameStopAddIn: String,
                         iedLatStopAddIn: String,
                         iedLngStopAddIn: String,
                         iedLinkImageStopAddIn: String,
                         url: String = "${URLBASE}/parada/guardar"
    ): JsonObjectRequest {
        val parameters = JSONObject()

        try {
            parameters.put("nombrePar", iedAddressStopAddIn)
            parameters.put("direccionPar", iedNameStopAddIn)
            parameters.put("longitudPar", iedLatStopAddIn)
            parameters.put("latitudPar", iedLngStopAddIn)
            parameters.put("imgPar", iedLinkImageStopAddIn)

        } catch (e: JSONException){
            Log.e("saveStop_JSON", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parameters, {response ->
            Toast.makeText(this, response.getString("respuesta"), Toast.LENGTH_SHORT).show()

        }, {error ->
            Log.e("saveStop_REQUEST", error.toString())
        })

        return jsonObjectRequest
    }



}