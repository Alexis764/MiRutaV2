package com.example.mirutav2.home.admin.bus

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.bus.BusModel

class BusAdminViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    // Variables componentes vista
    private val itemplace = view.findViewById<TextView>(R.id.item_admin_place)
    private val itemidentification = view.findViewById<TextView>(R.id.item_admin_identificactionBus)

    // Variable Botones
    val btnitemUpdate = view.findViewById<Button>(R.id.btn_item_admin_update_bus)
    val btnitemDelete = view.findViewById<Button>(R.id.btn_item_admin_delete_bus)

    // Funcion Renderizado

    fun render(busModel: BusModel){
        itemplace.text = busModel.placaBus
        /*itemidentification.text = busModel.*/
    }


}