package com.example.mirutav2.home.admin.driver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R


class DriverAdminAdapter(
    var driverList : List<DriverModelAdmin>,
    private val driverAdminListener: DriverAdminListener) :
        RecyclerView.Adapter<DriverAdminViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverAdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_listar_driver,parent,false)
        return DriverAdminViewHolder(view)
    }

    override fun getItemCount() = driverList.size

    override fun onBindViewHolder(holder: DriverAdminViewHolder, position: Int) {
        holder.render(driverList[position])


        holder.btnitemDelete.setOnClickListener {
            driverAdminListener.onBtnDeleteClicked(driverList[position])
            notifyDataSetChanged()
        }

    }

}