package com.example.mirutav2.home.admin.stop

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.stop.StopModel

class StopAdminViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    // Variables componentes vista
    private val itemID = view.findViewById<TextView>(R.id.item_admin_idStop)
    private val itemAddress = view.findViewById<TextView>(R.id.item_admin_address)
    private val itemnameStop = view.findViewById<TextView>(R.id.item_admin_nameStop)

    // Variable Botones
    val btnitemUpdate = view.findViewById<Button>(R.id.btn_item_admin_update_stop)
    val btnitemDelete = view.findViewById<Button>(R.id.btn_item_admin_delete_stop)

    // Funcion renderizado

    fun render(stopModel: StopModel){
        itemID.text = stopModel.idPar.toString()
        itemAddress.text = stopModel.direccionPar
        itemnameStop.text = stopModel.nombrePar
    }
}