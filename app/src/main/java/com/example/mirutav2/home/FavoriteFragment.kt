package com.example.mirutav2.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.R
import com.example.mirutav2.home.route.RouteAdapter
import com.example.mirutav2.home.route.RouteListener
import com.example.mirutav2.home.route.RouteModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class FavoriteFragment : Fragment(), RouteListener {

    //Variables para configurar recycler view
    private lateinit var rvRoutesFavorite: RecyclerView
    private lateinit var routeAdapter: RouteAdapter

    private var routeList = ArrayList<RouteModel>()

    private lateinit var queue : RequestQueue



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)

        initComponent(rootView)
        initUi()

        return rootView
    }



    //Conexion de componentes a vista
    private fun initComponent(rootView: View) {
        rvRoutesFavorite = rootView.findViewById(R.id.rvRoutesFavorite)
        queue = Volley.newRequestQueue(this.context)
    }



    //Iniciar los componentes de la interfaz respecto a valores predeterminados
    private fun initUi() {
        initRvRoutesFavorite()
    }



    //Funciones para traer las rutas desde la dase de datos y ponerlas en el recyclerview
    //Iniciar el recyclerview
    private fun initRvRoutesFavorite() {
        val url = "http://192.168.20.23:8080/ruta/listarFav/alexis@gmail.com"
        queue.add(getRoutes(url))
    }

    //Funcion metodo get para traer las rutas
    private fun getRoutes(url: String): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                routeList.add(createRoute(jsonArray[i] as JSONObject))
                i++
            }

            toListRvRoutesFavorite(routeList)

        }, { error ->
            Log.e("Volley_getRoutes", error.toString())
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
            true
        )
    }

    //Funcion para asignar el adaptador al RvRoutesFavorite
    private fun toListRvRoutesFavorite(routeList: ArrayList<RouteModel>) {
        routeAdapter = RouteAdapter(routeList, this)
        rvRoutesFavorite.layoutManager = LinearLayoutManager(this.context)
        rvRoutesFavorite.adapter = routeAdapter
    }



    //Funcion lambda para el boton favoritos
    //Click en el boton de favorito
    override fun onBtnFavoriteClicked(routeModel: RouteModel) {
        routeModel.isFavorite = !routeModel.isFavorite

        val url = if (routeModel.isFavorite) "http://192.168.20.23:8080/ruta/agregarFav" else "http://192.168.20.23:8080/ruta/eliminarFav"

        queue.add(changeRouteFavorite(routeModel.idRut, url))
    }

    //Cambiar ruta favorita base de datos
    private fun changeRouteFavorite(idRut: Long, url: String) : JsonObjectRequest {
        val parameters = JSONObject()

        try {
            parameters.put("correoUsu", "alexis@gmail.com")
            parameters.put("idRut", idRut)

        } catch (e: JSONException) {
            Log.e("addRouteFavorite_JSON", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parameters, {response ->
            Toast.makeText(rvRoutesFavorite.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()

        }, {error ->
            Log.e("addRouteFavorite_Request", error.toString())
        })

        return jsonObjectRequest
    }



    //Funcion lambda para el boton extensible
    //Click en el boton del extendible
    override fun onBtnExtendClicked(routeModel: RouteModel) {
        routeModel.isOpenStop = !routeModel.isOpenStop
    }



    //Click en el boton del mapa
    override fun onBtnMapClicked(routeModel: RouteModel) {
        TODO("Not yet implemented")
    }

}