package com.example.mirutav2.home.admin.stop

import com.example.mirutav2.home.stop.StopModel

interface StopAdminListener {

    fun onBtnUpdateCliecked ( stopModel: StopModel )
    fun onBtnDeleteClicked ( stopModel: StopModel )

}