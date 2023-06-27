package com.example.mirutav2.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import com.example.mirutav2.home.HomeActivity.Companion.userModel
import com.example.mirutav2.home.RouteFragment.Companion.IDRUT
import com.example.mirutav2.home.bus.BusModel
import com.example.mirutav2.home.bus.RouteBusAdapter
import com.example.mirutav2.home.bus.RouteBusListener
import com.example.mirutav2.home.route.RouteModel
import com.example.mirutav2.map.MapActivity
import org.json.JSONArray
import org.json.JSONObject

class BusFragment : Fragment(), RouteBusListener {

    //Variables
    private lateinit var queue: RequestQueue

    private lateinit var busModel: BusModel

    companion object {
        const val PLACABUS = "placaBus"
    }



    //Variables para componentes
    private lateinit var tvBusPlate: TextView
    private lateinit var tvIdUsu: TextView



    //Variables para configurar recyclerview
    private lateinit var rvBus: RecyclerView
    private lateinit var routeBusAdapter: RouteBusAdapter
    private var routeList = ArrayList<RouteModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_bus, container, false)

        initComponent(rootView)
        initUi()

        return rootView
    }



    //Conexion de componentes a vista
    private fun initComponent(rootView: View) {
        tvBusPlate = rootView.findViewById(R.id.tvBusPlate)
        tvIdUsu = rootView.findViewById(R.id.tvIdUsu)

        rvBus = rootView.findViewById(R.id.rvBus)

        queue = Volley.newRequestQueue(this.context)
    }



    //Iniciar los componentes de la interfaz respecto a valores predeterminados
    private fun initUi() {
        queue.add(getBusInfo())
        queue.add(initRvBus())
    }



    //Traer la informacion del bus que maneja las rutas
    private fun getBusInfo(url: String = "$URLBASE/bus/buscarUsu/${userModel.identificacionUsu}"): StringRequest {
        val stringReques = StringRequest(Request.Method.GET, url, {response ->
            val jsonObject = JSONObject(response)

            val placaBus = jsonObject.getString("placaBus")
            val longitudBus = jsonObject.getDouble("longitudBus")
            val latitudBus = jsonObject.getDouble("latitudBus")
            busModel = BusModel(placaBus, longitudBus, latitudBus)

            setTextBusInfo()

        }, {error ->
            Log.e("Volley_getBusInfo", error.toString())
        })

        return stringReques
    }



    //Funcion metodo get para traer las rutas
    private fun initRvBus(url: String = "$URLBASE/ruta/listarBus/${userModel.identificacionUsu}"): StringRequest {
        val stringReques = StringRequest(Request.Method.GET, url, {response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                routeList.add(createRoute(jsonArray[i] as JSONObject))
                i++
            }

            toListRvBus(routeList)

        }, {error ->
            Log.e("Volley_getBusInfo", error.toString())
        })

        return stringReques
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
            lugarDestinoRut
        )
    }

    //Funcion para asignar el adaptador al RvBus
    private fun toListRvBus(routeList: ArrayList<RouteModel>) {
        routeBusAdapter = RouteBusAdapter(routeList, this)
        rvBus.layoutManager = LinearLayoutManager(this.context)
        rvBus.adapter = routeBusAdapter
    }



    //Funcion para asginar texto de informacion a los textview de la interfaz
    private fun setTextBusInfo() {
        tvBusPlate.text = getString(R.string.plateBus, busModel.placaBus)
        tvIdUsu.text = getString(R.string.idUsu, userModel.identificacionUsu.toString())
    }



    //Funcion lambda para el boton inciar ruta
    //Click en el boton de iniciar ruta
    override fun onBtnInitRouteClicked(routeModel: RouteModel) {
        val intent = Intent(this.context, MapActivity::class.java)
        intent.putExtra(IDRUT, routeModel.idRut)
        intent.putExtra(PLACABUS, busModel.placaBus)
        startActivity(intent)
    }



}
