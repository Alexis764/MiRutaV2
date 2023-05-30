package com.example.mirutav2.home.route

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R

class RouteStopAdapter(
    private val routeStopNameList: List<String>
) : RecyclerView.Adapter<RouteStopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteStopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route_stops, parent, false)
        return RouteStopViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteStopViewHolder, position: Int) {
        holder.render(routeStopNameList[position])
    }

    override fun getItemCount() = routeStopNameList.size
}