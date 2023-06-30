package com.example.mirutav2.home.admin.route

import com.example.mirutav2.home.route.RouteModel

interface RouteAdminListener {

    fun onBtnUpdateClicked ( routeModel : RouteModel )
    fun onBtnDeleteClicked ( routeModel: RouteModel )
}