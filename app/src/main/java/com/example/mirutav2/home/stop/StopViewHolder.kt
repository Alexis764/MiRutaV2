package com.example.mirutav2.home.stop

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mirutav2.R

class StopViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val ivPhotoStop = view.findViewById<ImageView>(R.id.ivPhotoStop)
    private val tvNameStop = view.findViewById<TextView>(R.id.tvNameStop)
    private val tvAddressStop = view.findViewById<TextView>(R.id.tvAddressStop)

    fun render(stop: StopModel) {

        try {
            Glide.with(ivPhotoStop.context)
                .load(stop.imgPar)
                .circleCrop()
                .into(ivPhotoStop)

        } catch (e: Exception) {
            Log.e("StopImage", "No carga la imagen")
        }

        tvNameStop.text = stop.nombrePar
        tvAddressStop.text = stop.direccionPar

    }
}