package com.example.mirutav2.home

data class UserModel (
    val identificacionUsu: Long,
    val correoUsu: String,
    val contraseniaUsu: String,
    val nombreUsu: String,
    val fotoUsu: String,
    val tipoUsuario : Int
)