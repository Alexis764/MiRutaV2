package com.example.mirutav2.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mirutav2.MainActivity
import com.example.mirutav2.R
import com.google.android.material.navigationrail.NavigationRailView



class HomeActivity : AppCompatActivity() {

    //Constantes
    companion object {
        const val URLBASE = "http://192.168.0.7:8080"
    }

    //Variables
    private lateinit var navController: NavController

    //Variables para componentes
    private lateinit var nrvHome: NavigationRailView
    private lateinit var fcvHome: NavHostFragment
    //Variable Switch(Tema)
    private lateinit var switch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initComponent()
        initController()
        initSwitch()

    }


    //Conexion de componentes a vista
    private fun initComponent() {
        nrvHome = findViewById(R.id.nrvHome)
        fcvHome = supportFragmentManager.findFragmentById(R.id.fcvHome) as NavHostFragment

        //Swith
        switch = findViewById(R.id.switch1)

    }


    //Iniciar el controlador para navegacion
    private fun initController() {
        navController = fcvHome.navController
        nrvHome.setupWithNavController(navController)
    }

    private fun startMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    // Switch para Tema Claro/Oscuro
    private fun initSwitch() {

        val sp = getSharedPreferences("SP", Context.MODE_PRIVATE)
        val editor = sp.edit()

        switch.setOnClickListener {

            if (switch.isChecked) {
                editor.putInt("Theme",0)
            } else {
                editor.putInt("Theme",1)
            }
            editor.commit()

        }
    }


}