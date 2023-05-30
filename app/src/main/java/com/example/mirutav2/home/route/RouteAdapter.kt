package com.example.mirutav2.home.route

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R

class RouteAdapter(
    var routeList: List<RouteModel>,
    private val routeListener: RouteListener
) : RecyclerView.Adapter<RouteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.render(routeList[position])

        holder.ibFavorite.setOnClickListener {
            routeListener.onBtnFavoriteClicked(routeList[position])
            notifyItemChanged(position)
        }

        holder.btnExtendStops.setOnClickListener {
            routeListener.onBtnExtendClicked(routeList[position])
            notifyItemChanged(position)
        }

        holder.btnMap.setOnClickListener {
            routeListener.onBtnMapClicked(routeList[position])
        }

    }

    override fun getItemCount() = routeList.size



    @SuppressLint("NotifyDataSetChanged")
    fun updateRouteList(routeList: List<RouteModel>){
        this.routeList = routeList
        notifyDataSetChanged()
    }



}