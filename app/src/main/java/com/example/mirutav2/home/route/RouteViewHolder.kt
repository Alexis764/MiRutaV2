package com.example.mirutav2.home.route

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.R
import com.example.mirutav2.MainActivity.Companion.URLBASE
import org.json.JSONArray

class RouteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    //Variables para los componentes de la vista
    private val tvStartRoute = view.findViewById<TextView>(R.id.tvStartRoute)
    private val tvStartHour = view.findViewById<TextView>(R.id.tvStartHour)
    private val tvEndHour = view.findViewById<TextView>(R.id.tvEndHour)
    private val tvSchedule = view.findViewById<TextView>(R.id.tvSchedule)
    private val tvDestinationRoute = view.findViewById<TextView>(R.id.tvDestinationRoute)

    val ibFavorite = view.findViewById<ImageButton>(R.id.ibFavorite)

    val btnExtendStops = view.findViewById<FrameLayout>(R.id.btnExtendStops)
    val ivRowStops = view.findViewById<ImageView>(R.id.ivRowStops)
    val rvStopsToRoute = view.findViewById<RecyclerView>(R.id.rvStopsToRoute)

    val btnMap = view.findViewById<Button>(R.id.btnMap)



    //Variables para configurar recyclerview
    private var routeStopNameList = ArrayList<String>()
    private lateinit var routeStopAdapter: RouteStopAdapter



    //Funcion que renderiza la informacion en la vista
    fun render(route: RouteModel) {

        tvStartRoute.text = route.lugarInicioRut
        tvStartHour.text = route.horaInicioRut
        tvEndHour.text =  route.horaFinalRut
        tvSchedule.text = route.diasDisponiblesRut
        tvDestinationRoute.text = route.lugarDestinoRut


        val colourIbFavorite = if (route.isFavorite) R.color.red else R.color.white
        ibFavorite.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(tvStartRoute.context, colourIbFavorite)
        )


        val url = "$URLBASE/parada/listarNombreRut/${route.idRut}"
        val queue = Volley.newRequestQueue(tvStartRoute.context)
        queue.add(getStopNameToRoute(url))
        rvStopsToRoute.visibility = if (route.isOpenStop) View.VISIBLE else View.GONE
        if (route.isOpenStop) ivRowStops.setImageResource(R.drawable.ic_row_close) else ivRowStops.setImageResource(R.drawable.ic_row_open)
    }



    //Funcion para traer el nombre de las paradas respecto a la ruta(idRut)
    private fun getStopNameToRoute(url: String): StringRequest {
        routeStopNameList.clear()

        val stringRequest = StringRequest(Request.Method.GET, url, {response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                routeStopNameList.add(jsonArray[i] as String)
                i++
            }

            toListrvStopsToRoute(routeStopNameList)

        }, {error ->
            Log.e("Volley_getStopNameToRoute", error.toString())
        })

        return stringRequest
    }

    //Funcion para asignar el adaptador al rvStopsToRoute
    private fun toListrvStopsToRoute(routeStopNameList: ArrayList<String>) {
        routeStopAdapter = RouteStopAdapter(routeStopNameList)
        rvStopsToRoute.layoutManager = LinearLayoutManager(tvStartRoute.context)
        rvStopsToRoute.adapter = routeStopAdapter
    }
}