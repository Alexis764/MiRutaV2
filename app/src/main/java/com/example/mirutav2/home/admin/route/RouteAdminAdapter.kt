package com.example.mirutav2.home.admin.route

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.route.RouteModel

class RouteAdminAdapter (var routeList : List<RouteModel>,
private val routeAdminListener: RouteAdminListener):
RecyclerView.Adapter<RouteAdminViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteAdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_listar_route,parent,false)
        return RouteAdminViewHolder(view)
    }

    override fun getItemCount() = routeList.size

    override fun onBindViewHolder(holder: RouteAdminViewHolder, position: Int) {
        holder.render(routeList[position])

        holder.btnitemUpdate.setOnClickListener {
            routeAdminListener.onBtnUpdateClicked(routeList[position])
        }

        holder.btnitemDelete.setOnClickListener {
            routeAdminListener.onBtnDeleteClicked(routeList[position])
            notifyDataSetChanged()
        }
    }


}