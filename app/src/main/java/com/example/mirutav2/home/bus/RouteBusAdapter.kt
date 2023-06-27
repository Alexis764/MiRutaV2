package com.example.mirutav2.home.bus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.route.RouteModel

class RouteBusAdapter(
    var routeList: List<RouteModel>,
    private val routeBusListener: RouteBusListener
): RecyclerView.Adapter<RouteBusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteBusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route_bus, parent, false)
        return RouteBusViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteBusViewHolder, position: Int) {
        holder.render(routeList[position])

        holder.btnInitRoute.setOnClickListener {
            routeBusListener.onBtnInitRouteClicked(routeList[position])
        }
    }

    override fun getItemCount() = routeList.size

}