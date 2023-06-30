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
import com.example.mirutav2.MainActivity.Companion.URLBASE
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
    private var longitudPar: Double = 0.0
    private var latitudPar: Double = 0.0
    private lateinit var imgPar: String


    private lateinit var queue: RequestQueue



    //Variables para componentes
    private lateinit var tvNameStopDialog: TextView
    private lateinit var ivPhotoStopDialog: ImageView
    private lateinit var tvAddressStopDialog: TextView
    private lateinit var btnCloseDialog: ImageView



    //Variables para configurar recyclerView
    private lateinit var rvRoutesToStopDialog: RecyclerView
    private var stopRouteList = ArrayList<RouteModel>()
    private lateinit var stopRouteAdapter: StopRouteAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_stop)

        initComponent()
        initArguments()
        initUi()
        initListeners()

    }



    //Conexion de componentes a vista
    private fun initComponent() {
        tvNameStopDialog = findViewById(R.id.tvNameStopDialog)
        ivPhotoStopDialog = findViewById(R.id.ivPhotoStopDialog)
        tvAddressStopDialog = findViewById(R.id.tvAddressStopDialog)
        rvRoutesToStopDialog = findViewById(R.id.rvRoutesToStopDialog)
        btnCloseDialog = findViewById(R.id.btnCloseDialog)

        queue = Volley.newRequestQueue(this)
    }



    //Trae el valor a partir de los argumentos enviados
    private fun initArguments() {
        idPar = intent.extras?.getLong(IDPAR) ?: 0
        nombrePar = intent.extras?.getString(NOMBREPAR).orEmpty()
        direccionPar = intent.extras?.getString(DIRECCIONPAR).orEmpty()
        longitudPar = intent.extras?.getDouble(LONGITUDPAR) ?: 0.0
        latitudPar = intent.extras?.getDouble(LATITUDPAR) ?: 0.0
        imgPar = intent.extras?.getString(IMGPAR).orEmpty()
    }



    //Renderiza la informacion en la vista
    private fun initUi() {
        tvNameStopDialog.text = nombrePar
        loadPhotoStop()
        tvAddressStopDialog.text = direccionPar
        initRvRoutesToStopDialog(idPar)
    }

    //Carga la imagen a la vista
    private fun loadPhotoStop() {
        try {
            Glide.with(ivPhotoStopDialog.context)
                .load(imgPar)
                .into(ivPhotoStopDialog)

        }  catch (e: Exception) {
            Log.e("StopImage_dialog", "No carga la imagen")
        }
    }



    //Funciones para traer las rutas desde la dase de datos y ponerlas en el recyclerview
    //Funcion para traer las rutas respecto a la parada(idPar)
    private fun initRvRoutesToStopDialog(idPar: Long) {
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

    //Funcion para asignar el adaptador al rvRoutesToStopDialog
    private fun toListRvRoutesToStopDialog(stopRouteList: ArrayList<RouteModel>) {
        stopRouteAdapter = StopRouteAdapter(stopRouteList)
        rvRoutesToStopDialog.layoutManager = LinearLayoutManager(this)
        rvRoutesToStopDialog.adapter = stopRouteAdapter
    }



    //Funciones click de los componentes
    private fun initListeners() {
        btnCloseDialog.setOnClickListener {
            onBackPressed()
        }
    }
}