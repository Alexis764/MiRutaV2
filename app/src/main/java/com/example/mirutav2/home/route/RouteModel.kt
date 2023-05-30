package com.example.mirutav2.home.route

data class RouteModel (
    val idRut: Long,

    val lugarInicioRut: String,
    val horaInicioRut: String,
    val horaFinalRut: String,
    val diasDisponiblesRut: String,
    val lugarDestinoRut: String,

    var isFavorite: Boolean = false,
    var isOpenStop: Boolean = false
)