package com.example.mirutav2.home

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mirutav2.R
import com.example.mirutav2.home.route.RouteAdapter
import com.example.mirutav2.home.route.RouteListener
import com.example.mirutav2.home.route.RouteModel

import com.google.android.material.textfield.TextInputEditText

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.home.HomeActivity.Companion.userModel
import com.example.mirutav2.map.MapActivity

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RouteFragment : Fragment(), RouteListener {

    //Variables
    private var cvSearchIsOpen = false

    private lateinit var queue : RequestQueue

    companion object {
        const val IDRUT = "idRut"
    }



    //Variables para componentes
    private lateinit var cvSearch: LinearLayout
    private lateinit var iedStarRoute: TextInputEditText
    private lateinit var iedDestinationRoute: TextInputEditText
    private lateinit var cvOpenSearch: CardView
    private lateinit var ivRow: ImageView



    //Variables para configurar recycler view
    private lateinit var shimmerRoute: LinearLayoutCompat
    private lateinit var rvRoutes: RecyclerView
    private lateinit var routeAdapter: RouteAdapter

    private var routeList = ArrayList<RouteModel>()
    private lateinit var routeListFilter : List<RouteModel>

    private var idFavoriteRouteList = ArrayList<Int>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_route, container, false)

        initComponent(rootView)
        initListeners()
        initUi()

        return rootView
    }



    //Conexion de componentes a vista
    private fun initComponent(rootView: View) {
        cvSearch = rootView.findViewById(R.id.cvSearch)
        iedStarRoute = rootView.findViewById(R.id.iedStarRoute)
        iedDestinationRoute = rootView.findViewById(R.id.iedDestinationRoute)
        cvOpenSearch = rootView.findViewById(R.id.cvOpenSearch)
        ivRow = rootView.findViewById(R.id.ivRow)
        shimmerRoute = rootView.findViewById(R.id.shimmerRoute)
        rvRoutes = rootView.findViewById(R.id.rvRoutes)

        queue = Volley.newRequestQueue(this.context)
    }



    //Inicio de funciones click
    private fun initListeners() {
        cvOpenSearch.setOnClickListener {
            changeCvSearchIsOpen()
            changeVisibilitySearch(cvSearchIsOpen)
            changeIvRow(cvSearchIsOpen)
        }
        iedStarRoute.addTextChangedListener {
            filterRoutes(iedStarRoute.text.toString(), iedDestinationRoute.text.toString())
        }
        iedDestinationRoute.addTextChangedListener {
            filterRoutes(iedStarRoute.text.toString(), iedDestinationRoute.text.toString())
        }
    }



    //Iniciar los componentes de la interfaz respecto a valores predeterminados
    private fun initUi() {
        changeVisibilitySearch(cvSearchIsOpen)
        changeIvRow(cvSearchIsOpen)
        initRvRoutes()
    }



    //Funciones para abrir y cerrar la tarjeta de buscar
    //Cambia la variable que guarda el estado de la tarjeta
    private fun changeCvSearchIsOpen() {
        cvSearchIsOpen = !cvSearchIsOpen
    }

    //Cambia la visibilidad de la tarjeta respecto a su estado
    private fun changeVisibilitySearch(cvSearchIsOpen: Boolean) {
        TransitionManager.beginDelayedTransition(cvSearch, AutoTransition())
        if (cvSearchIsOpen) {
            cvSearch.visibility = View.VISIBLE

        } else {
            cvSearch.visibility = View.GONE
        }
    }

    //Cambia la imagen de la flecha respecto al estado de la tarjeta
    private fun changeIvRow(cvSearchIsOpen: Boolean) {
        if (cvSearchIsOpen) ivRow.setImageResource(R.drawable.ic_row_close) else ivRow.setImageResource(R.drawable.ic_row_open)
    }



    //Funciones para traer las rutas desde la dase de datos y ponerlas en el recyclerview
    //Iniciar el recyclerview
    private fun initRvRoutes() {
        queue.add(getIdRoutesFavorites())
        queue.add(getRoutes())
    }

    //Funcion metodo get para traer los id de las rutas favoritas para el usuario
    private fun getIdRoutesFavorites(url: String = "$URLBASE/ruta/listaId/${userModel.correoUsu}"): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, {response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                idFavoriteRouteList.add(jsonArray[i] as Int)
                i++
            }

        }, {error ->
            Log.e("Volley_getIdRoutesFavorites", error.toString())
        })

        return stringRequest
    }

    //Funcion metodo get para traer las rutas
    private fun getRoutes(url: String = "$URLBASE/ruta/listar"): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                routeList.add(createRoute(jsonArray[i] as JSONObject))
                i++
            }

            toListRvRoutes(routeList)

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
            checkFavoriteRoute(idRut)
        )
    }

    //Funcion para comprobar si la ruta esta en la lista de rutas favoritas para el ususario
    private fun checkFavoriteRoute(idRut: Long): Boolean {
        return idFavoriteRouteList.find { it == idRut.toInt() } != null
    }

    //Funcion para asignar el adaptador al RvRoutes
    private fun toListRvRoutes(routeList: MutableList<RouteModel>) {
        routeAdapter = RouteAdapter(routeList, this)
        rvRoutes.layoutManager = LinearLayoutManager(this.context)
        rvRoutes.adapter = routeAdapter

        filterRoutes(iedStarRoute.text.toString(), iedDestinationRoute.text.toString())
        shimmerRoute.visibility = View.GONE
        rvRoutes.visibility = View.VISIBLE

    }



    //Funcion para filtrar las rutas
    private fun filterRoutes(startPlace: String, destinationPlace: String) {
        if (::routeAdapter.isInitialized) {
            routeListFilter = routeList.filter {
                it.lugarInicioRut.lowercase().contains(startPlace.lowercase())
                        && it.lugarDestinoRut.lowercase().contains(destinationPlace.lowercase())
            }

            routeAdapter.updateRouteList(routeListFilter)
        }
    }



    //Funcion lambda para el boton favoritos
    //Click en el boton de favorito
    override fun onBtnFavoriteClicked(routeModel: RouteModel) {
        routeModel.isFavorite = !routeModel.isFavorite

        val url = if (routeModel.isFavorite) "$URLBASE/ruta/agregarFav" else "$URLBASE/ruta/eliminarFav"

        queue.add(changeRouteFavorite(routeModel.idRut, url))
    }

    //Cambiar ruta favorita base de datos
    private fun changeRouteFavorite(idRut: Long, url: String) : JsonObjectRequest {
        val parameters = JSONObject()

        try {
            parameters.put("correoUsu", userModel.correoUsu)
            parameters.put("idRut", idRut)

        } catch (e: JSONException) {
            Log.e("addRouteFavorite_JSON", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parameters, {response ->
            Toast.makeText(cvSearch.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()

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
        val intent = Intent(this.context, MapActivity::class.java)
        intent.putExtra(IDRUT, routeModel.idRut)
        startActivity(intent)
    }

}