package com.example.mirutav2.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.IDUSU
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import com.example.mirutav2.home.route.RouteModel
import com.google.android.material.navigationrail.NavigationRailView
import org.json.JSONArray
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    //Constantes
    companion object {
        lateinit var userModel: UserModel
    }



    //Variables
    private lateinit var navController: NavController
    private lateinit var queue: RequestQueue
    private var idUsu: Long = 0



    //Variables para componentes
    private lateinit var nrvHome: NavigationRailView
    private lateinit var fcvHome: NavHostFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        idUsu = intent.extras?.getLong(IDUSU) ?: 0

        initComponent()
        initController()
        queue.add(getUser())

    }



    //Conexion de componentes a vista
    private fun initComponent() {
        nrvHome = findViewById(R.id.nrvHome)
        fcvHome = supportFragmentManager.findFragmentById(R.id.fcvHome) as NavHostFragment

        queue = Volley.newRequestQueue(this)
    }



    //Iniciar el controlador para navegacion
    private fun initController() {
        navController = fcvHome.navController
        nrvHome.setupWithNavController(navController)
    }



    //Funciones para traer la informacion del usuario
    private fun getUser(url: String = "$URLBASE/usuario/buscar/$idUsu"): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, {response ->
            val jsonObject = JSONObject(response)

            createUser(jsonObject)

        }, {error ->
            Log.e("Volley_getUser", error.toString())
        })

        return stringRequest
    }

    private fun createUser(jsonObject: JSONObject) {
        val idUsu = jsonObject.getLong("idUsu")
        val correoUsu = jsonObject.getString("correoUsu")
        val contraseniaUsu = jsonObject.getString("contraseniaUsu")
        val nombreUsu = jsonObject.getString("nombreUsu")
        val fotoUsu = jsonObject.getString("fotoUsu")
        val tipoUsuario = jsonObject.getInt("tipoUsuario")

        userModel = UserModel(idUsu, correoUsu, contraseniaUsu, nombreUsu, fotoUsu, tipoUsuario)
    }

}