package com.example.mirutav2.home.bus

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.route.RouteModel

class RouteBusViewHolder(view: View): RecyclerView.ViewHolder(view) {

    //Variables para los componentes de la vista
    private val tvStartRouteBus = view.findViewById<TextView>(R.id.tvStartRouteBus)
    private val tvStartHourBus = view.findViewById<TextView>(R.id.tvStartHourBus)
    private val tvEndHourBus = view.findViewById<TextView>(R.id.tvEndHourBus)
    private val tvScheduleBus = view.findViewById<TextView>(R.id.tvScheduleBus)
    private val tvDestinationRouteBus = view.findViewById<TextView>(R.id.tvDestinationRouteBus)

    val btnInitRoute = view.findViewById<Button>(R.id.btnInitRoute)

    fun render(routeModel: RouteModel) {
        tvStartRouteBus.text = routeModel.lugarInicioRut
        tvStartHourBus.text = routeModel.horaInicioRut
        tvEndHourBus.text =  routeModel.horaFinalRut
        tvScheduleBus.text = routeModel.diasDisponiblesRut
        tvDestinationRouteBus.text = routeModel.lugarDestinoRut
    }
}