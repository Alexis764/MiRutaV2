package com.example.mirutav2.home.admin.bus

import com.example.mirutav2.home.bus.BusModel


interface BusAdminListener {

    fun onBtnUpdateClicked( busModel: BusModel )
    fun onBtnDeleteClicked( busModel: BusModel)

}