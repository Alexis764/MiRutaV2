package com.example.mirutav2.home.admin.route

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.route.RouteModel

class RouteAdminViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    // Variables componentes vista
    private val itemID = view.findViewById<TextView>(R.id.item_admin_idRoute)
    private val itemStartLocation = view.findViewById<TextView>(R.id.item_admin_startLocation)
    private val itemEndLocation = view.findViewById<TextView>(R.id.item_admin_endLocation)
    private val itemday = view.findViewById<TextView>(R.id.item_admin_disponibleDay)

    // Variable Botones

    val btnitemUpdate = view.findViewById<Button>(R.id.btn_item_admin_update_route)
    val btnitemDelete = view.findViewById<Button>(R.id.btn_item_admin_delete_route)

    // Funcion renderizado

    fun render(routeModel : RouteModel){

        itemID.text = routeModel.idRut.toString()
        itemStartLocation.text = routeModel.lugarInicioRut
        itemEndLocation.text = routeModel.lugarDestinoRut
        itemday.text = routeModel.diasDisponiblesRut

    }
}