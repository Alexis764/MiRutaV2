package com.example.mirutav2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.home.HomeActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_credentials")
class MainActivity : AppCompatActivity() {

    //Constantes
    companion object {
        const val URLBASE = "http://192.168.20.23:8080"
        const val IDUSU = "idUsu"

        const val IDUSUCREDENTIAL = "idUsuCredential"
    }



    //Variables
    private lateinit var queue : RequestQueue
    private var firstTime = true



    //Variables para componentes
    private lateinit var iedEmail: TextInputEditText
    private lateinit var iedPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnActivityRegister: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread.sleep(2000)
        splashScreen.setKeepOnScreenCondition{false}

        initCredentials()
        initComponent()
        initListeners()

    }



    //Funcion que comprobaria las user_credentials para iniciar sesion automaticamente
    private fun initCredentials() {
        CoroutineScope(Dispatchers.IO).launch {
            getCredentials().filter { firstTime }.collect{ credentialIdUsu ->
                if (credentialIdUsu != null) {
                    val idNeutral: Long = 0
                    if (credentialIdUsu != idNeutral){
                        startHome(credentialIdUsu)
                    }
                }
                firstTime = false
            }
        }
    }



    //Conexion de componentes a vista
    private fun initComponent() {
        iedEmail = findViewById(R.id.iedEmail)
        iedPassword = findViewById(R.id.iedPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnActivityRegister = findViewById(R.id.btnActivityRegister)

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
        btnActivityRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }



    //Funciones para comprobar el usuario e iniciar la siguiente actividad
    //Funcion para mandar los datos
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
                val idUsu = response.getLong("idUsu")

                CoroutineScope(Dispatchers.IO).launch {
                    saveCredentials(IDUSUCREDENTIAL, idUsu)
                }

                startHome(idUsu)

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
        finish()
    }



    //Funciones para manejar los datos de user_credentials con datastore preference
    //Funcion para guardar el email y password
    private suspend fun saveCredentials(key: String, value: Long) {
        dataStore.edit {
            it[longPreferencesKey(key)] = value
        }
    }

    //Funcion para retornar los datos guardados
    private fun getCredentials(): Flow<Long?> {
        return dataStore.data.map {
            it[longPreferencesKey(IDUSUCREDENTIAL)]
        }
    }



}