package com.example.mirutav2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.home.HomeActivity
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    //Constantes
    companion object {
        const val URLBASE = "http://192.168.20.23:8080"
        const val IDUSU = "idUsu"
    }



    //Variables
    private lateinit var queue : RequestQueue



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

        queue = Volley.newRequestQueue(this)
    }



    //Funciones click de los componentes
    private fun initListeners() {
        btnLogin.setOnClickListener {
            val email = iedEmail.text.toString()
            val password = iedPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                queue.add(loginUser(email, password))

            } else {
                Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }



    //Funciones para comprobar el usuario e iniciar la siguiente actividad
    //Funcion para mandar los datos y recibit una respuesta
    private fun loginUser(email: String, password: String, url: String = "$URLBASE/usuario/login"): JsonObjectRequest {
        val parameters = JSONObject()

        try {
            parameters.put("correoUsu", email)
            parameters.put("contraseniaUsu", password)

        } catch (e: JSONException) {
            Log.e("loginUsuario_JSON", e.toString())
        }
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parameters, {response ->
            if (response.getBoolean("acceso")) {
                startHome(response.getLong("idUsu"))

            } else {
                Toast.makeText(this, "Email o password incorrectos", Toast.LENGTH_SHORT).show()
            }

        }, {error ->
            Log.e("loginUsuario_Request", error.toString())
        })

        return jsonObjectRequest
    }


    //Iniciar la siguiente actividad
    private fun startHome(idUsu: Long) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(IDUSU, idUsu)
        startActivity(intent)
    }
}