package com.example.mirutav2.home.admin.bus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.bus.BusModel

class BusAdminAdapter (
    var busList : List<BusModel>,
    private val busadminlistener : BusAdminListener) :
    RecyclerView.Adapter<BusAdminViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusAdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_listar_bus,parent,false)
        return BusAdminViewHolder(view)
    }

    override fun getItemCount() = busList.size

    override fun onBindViewHolder(holder: BusAdminViewHolder, position: Int) {
        holder.render(busList[position])


        holder.btnitemDelete.setOnClickListener {
            busadminlistener.onBtnDeleteClicked(busList[position])
            notifyDataSetChanged()
        }
    }


}