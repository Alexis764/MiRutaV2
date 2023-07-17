package com.example.mirutav2.home.admin.stop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.stop.StopModel

class StopAdminAdapter(var stopList : List<StopModel>,
private val stopAdminListener: StopAdminListener) :
RecyclerView.Adapter<StopAdminViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopAdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_listar_stop, parent, false)
        return StopAdminViewHolder(view)
    }

    override fun getItemCount() = stopList.size

    override fun onBindViewHolder(holder: StopAdminViewHolder, position: Int) {
        holder.render(stopList[position])

        holder.btnitemUpdate.setOnClickListener {
            stopAdminListener.onBtnUpdateCliecked(stopList[position])
        }

        holder.btnitemDelete.setOnClickListener {
            stopAdminListener.onBtnDeleteClicked(stopList[position])
            notifyDataSetChanged()
        }
    }


}