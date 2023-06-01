package com.example.mirutav2.home.stop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.mirutav2.R
import com.example.mirutav2.home.HomeActivity.Companion.URLBASE
import com.example.mirutav2.home.StopFragment.Companion.DIRECCIONPAR
import com.example.mirutav2.home.StopFragment.Companion.IDPAR
import com.example.mirutav2.home.StopFragment.Companion.IMGPAR
import com.example.mirutav2.home.StopFragment.Companion.LATITUDPAR
import com.example.mirutav2.home.StopFragment.Companion.LONGITUDPAR
import com.example.mirutav2.home.StopFragment.Companion.NOMBREPAR
import com.example.mirutav2.home.route.RouteModel
import org.json.JSONArray
import org.json.JSONObject

class DialogStopActivity : AppCompatActivity() {

    //Variables
    private var idPar: Long = 0
    private lateinit var nombrePar: String
    private lateinit var direccionPar: String
    private var longitudPar: Float = 0.0f
    private var latitudPar: Float = 0.0f
    private lateinit var imgPar: String


    private lateinit var queue: RequestQueue



    //Variables para componentes
    private lateinit var tvNameStop_dialog: TextView
    private lateinit var ivPhotoStop_dialog: ImageView
    private lateinit var tvAddressStop_dialog: TextView
    private lateinit var tvLatitudeStop_dialog: TextView
    private lateinit var tvLongitudeStop_dialog: TextView



    //Variables para configurar recyclerView
    private lateinit var rvRoutesToStop_dialog: RecyclerView
    private var stopRouteList = ArrayList<RouteModel>()
    private lateinit var stopRouteAdapter: StopRouteAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_stop)

        initComponent()
        initArguments()
        initUi()

    }



    //Conexion de componentes a vista
    private fun initComponent() {
        tvNameStop_dialog = findViewById(R.id.tvNameStop_dialog)
        ivPhotoStop_dialog = findViewById(R.id.ivPhotoStop_dialog)
        tvAddressStop_dialog = findViewById(R.id.tvAddressStop_dialog)
        tvLatitudeStop_dialog = findViewById(R.id.tvLatitudeStop_dialog)
        tvLongitudeStop_dialog = findViewById(R.id.tvLongitudeStop_dialog)
        rvRoutesToStop_dialog = findViewById(R.id.rvRoutesToStop_dialog)

        queue = Volley.newRequestQueue(this)
    }



    //Trae el valor a partir de los argumentos enviados
    private fun initArguments() {
        idPar = intent.extras?.getLong(IDPAR) ?: 0
        nombrePar = intent.extras?.getString(NOMBREPAR).orEmpty()
        direccionPar = intent.extras?.getString(DIRECCIONPAR).orEmpty()
        longitudPar = intent.extras?.getFloat(LONGITUDPAR) ?: 0.0f
        latitudPar = intent.extras?.getFloat(LATITUDPAR) ?: 0.0f
        imgPar = intent.extras?.getString(IMGPAR).orEmpty()
    }



    //Renderiza la informacion en la vista
    private fun initUi() {
        tvNameStop_dialog.text = nombrePar
        loadPhotoStop()
        tvAddressStop_dialog.text = direccionPar
        tvLatitudeStop_dialog.text = latitudPar.toString()
        tvLongitudeStop_dialog.text = longitudPar.toString()
        initRvRoutesToStop_dialog(idPar)
    }

    //Carga la imagen a la vista
    private fun loadPhotoStop() {
        try {
            Glide.with(ivPhotoStop_dialog.context)
                .load(imgPar)
                .circleCrop()
                .into(ivPhotoStop_dialog)

        }  catch (e: Exception) {
            Log.e("StopImage_dialog", "No carga la imagen")
        }
    }



    //Funciones para traer las rutas desde la dase de datos y ponerlas en el recyclerview
    //Funcion para traer las rutas respecto a la parada(idPar)
    private fun initRvRoutesToStop_dialog(idPar: Long) {
        val url = "$URLBASE/ruta/listarPar/$idPar"
        queue.add(getStopNameToRoute(url))
    }

    //Funcion metodo get para traer las rutas
    private fun getStopNameToRoute(url: String): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                stopRouteList.add(createRoute(jsonArray[i] as JSONObject))
                i++
            }

            toListRvRoutesToStopDialog(stopRouteList)

        }, {error ->
            Log.e("Volley_getStopNameToRoute", error.toString())
        })

        return stringRequest
    }

    //Funcion para crear una ruta
    private fun createRoute(jsonRoute: JSONObject): RouteModel {
        val idRut = jsonRoute.getLong("idRut")
        val lugarInicioRut = jsonRoute.getString("lugarInicioRut")
        val horaInicioRut = jsonRoute.getString("horaInicioRut")
        val horaFinalRut = jsonRoute.getString("horaFinalRut")
        val diasDisponiblesRut = jsonRoute.getString("diasDisponiblesRut")
        val lugarDestinoRut = jsonRoute.getString("lugarDestinoRut")

        return RouteModel(
            idRut,
            lugarInicioRut,
            horaInicioRut,
            horaFinalRut,
            diasDisponiblesRut,
            lugarDestinoRut,
        )
    }

    //Funcion para asignar el adaptador al rvRoutesToStop_dialog
    private fun toListRvRoutesToStopDialog(stopRouteList: ArrayList<RouteModel>) {
        stopRouteAdapter = StopRouteAdapter(stopRouteList)
        rvRoutesToStop_dialog.layoutManager = LinearLayoutManager(this)
        rvRoutesToStop_dialog.adapter = stopRouteAdapter
    }
}