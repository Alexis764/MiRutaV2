package com.example.mirutav2.home.stop

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.route.RouteModel

class StopRouteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvStartRoute_stop = view.findViewById<TextView>(R.id.tvStartRoute_stop)
    private val tvDestinationRoute_stop = view.findViewById<TextView>(R.id.tvDestinationRoute_stop)

    fun render(route: RouteModel) {

        tvStartRoute_stop.text = route.lugarInicioRut
        tvDestinationRoute_stop.text = route.lugarDestinoRut

    }
}