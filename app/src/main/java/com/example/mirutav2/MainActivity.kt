package com.example.mirutav2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mirutav2.home.HomeActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    //Variables para componentes
    private lateinit var iedEmail: TextInputEditText
    private lateinit var iedPassword: TextInputEditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        splashScreen.setKeepOnScreenCondition{false}

        initComponent()
        initListeners()

    }

    //Conexion de componentes a vista
    private fun initComponent() {
        iedEmail = findViewById(R.id.iedEmail)
        iedPassword = findViewById(R.id.iedPassword)
        btnLogin = findViewById(R.id.btnLogin)
    }


    //Funciones click de los componentes
    private fun initListeners() {
        btnLogin.setOnClickListener {
            startHome()
        }
    }


    //Iniciar la siguiente actividad
    private fun startHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}