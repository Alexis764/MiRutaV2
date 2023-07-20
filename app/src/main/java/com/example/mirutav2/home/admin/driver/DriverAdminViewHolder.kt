package com.example.mirutav2.home.admin.driver

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R

class DriverAdminViewHolder(view : View) : RecyclerView.ViewHolder(view){

    // Variables componentes vista
    private val itemDocument = view.findViewById<TextView>(R.id.item_admin_documentDriver)

    // Variable Botones
    val btnitemDelete = view.findViewById<Button>(R.id.btn_item_admin_delete_driver)


    // Funcion renderizado
    fun render (driverModelAdmin: DriverModelAdmin){
        itemDocument.text = driverModelAdmin.identificacionCon.toString()
    }
}