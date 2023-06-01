package com.example.mirutav2.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mirutav2.R
import com.google.android.material.navigationrail.NavigationRailView

class HomeActivity : AppCompatActivity() {

    //Constantes
    companion object {
        const val URLBASE = "http://192.168.20.23:8080"
    }

    //Variables
    private lateinit var navController: NavController

    //Variables para componentes
    private lateinit var nrvHome: NavigationRailView
    private lateinit var fcvHome: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initComponent()
        initController()

    }

    //Conexion de componentes a vista
    private fun initComponent() {
        nrvHome = findViewById(R.id.nrvHome)
        fcvHome = supportFragmentManager.findFragmentById(R.id.fcvHome) as NavHostFragment
    }


    //Iniciar el controlador para navegacion
    private fun initController() {
        navController = fcvHome.navController
        nrvHome.setupWithNavController(navController)
    }

}