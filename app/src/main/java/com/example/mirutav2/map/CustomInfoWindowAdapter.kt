package com.example.mirutav2.map

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.mirutav2.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(private val context: Context): GoogleMap.InfoWindowAdapter {

    @SuppressLint("InflateParams")
    override fun getInfoContents(marker: Marker): View? {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.item_custom_window, null)

        val tvCustomStopName = rootView.findViewById<TextView>(R.id.tvCustomStopName)
        val ivCustomStopImage = rootView.findViewById<ImageView>(R.id.ivCustomStopImage)

        tvCustomStopName.text = marker.title
        if (marker.tag.toString().isNotEmpty()) {
            try {
                Glide.with(context)
                    .load(marker.tag)
                    .into(ivCustomStopImage)

            } catch (e: Exception) {
                Log.e("StopImageCustom", "No carga la imagen")
            }

        } else {
            ivCustomStopImage.isVisible = false
        }

        return rootView
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

}