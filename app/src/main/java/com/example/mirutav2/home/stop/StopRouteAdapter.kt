package com.example.mirutav2.home.stop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.route.RouteModel

class StopRouteAdapter(
    var stopRouteList: List<RouteModel>
) : RecyclerView.Adapter<StopRouteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopRouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stop_routes, parent, false)
        return StopRouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: StopRouteViewHolder, position: Int) {
        holder.render(stopRouteList[position])
    }

    override fun getItemCount() = stopRouteList.size

}