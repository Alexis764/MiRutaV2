package com.example.mirutav2.home.bus

import com.example.mirutav2.home.route.RouteModel

interface RouteBusListener {
    fun onBtnInitRouteClicked(routeModel: RouteModel)
}