package com.example.mirutav2.home.route

interface RouteListener {

    fun onBtnFavoriteClicked(routeModel: RouteModel)
    fun onBtnExtendClicked(routeModel: RouteModel)
    fun onBtnMapClicked(routeModel: RouteModel)

}