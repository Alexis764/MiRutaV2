package com.example.mirutav2.home.route

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R

class RouteStopViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvNameStopRoute = view.findViewById<TextView>(R.id.tvNameStop_route)

    fun render(routeStopName: String) {
        tvNameStopRoute.text = routeStopName
    }

}