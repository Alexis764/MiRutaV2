package com.example.mirutav2.home.stop

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R

class StopAdapter(
    var stopList: List<StopModel>,
    private val stopListener: StopListener
) : RecyclerView.Adapter<StopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stop, parent, false)
        return StopViewHolder(view)
    }

    override fun onBindViewHolder(holder: StopViewHolder, position: Int) {
        holder.render(stopList[position])

        holder.itemView.setOnClickListener {
            stopListener.onItemStopClicked(stopList[position])
        }
    }

    override fun getItemCount() = stopList.size



    @SuppressLint("NotifyDataSetChanged")
    fun updateStopList(stopList: List<StopModel>){
        this.stopList = stopList
        notifyDataSetChanged()
    }
}