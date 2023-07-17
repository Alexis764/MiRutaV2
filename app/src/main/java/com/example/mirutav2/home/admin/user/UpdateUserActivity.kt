package com.example.mirutav2.home.admin.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.R
import com.example.mirutav2.MainActivity.Companion.URLBASE
import org.json.JSONException
import org.json.JSONObject

class UpdateUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)
        
        initData()
        backAdmin()
        updateData()
        
    }
    
    // Variables Datos
    private lateinit var UpdateDataEmail : EditText
    private lateinit var UpdateDataPassword : EditText
    private lateinit var UpdateDataName : EditText
    private lateinit var UpdateDataPhoto : EditText
    private lateinit var UpdateDataRol : EditText

    // Variable Botones
    private lateinit var btnCloseUser : Button
    private lateinit var btnUpdateUserData : Button

    // Recoleccion de datos
    private fun initData() {

        // Datos
        UpdateDataEmail = findViewById(R.id.UpdateDataEmail)
        UpdateDataPassword = findViewById(R.id.UpdateDataPassword)
        UpdateDataName = findViewById(R.id.UpdateDataName)
        UpdateDataPhoto = findViewById(R.id.UpdateDataPhoto)
        UpdateDataRol = findViewById(R.id.UpdateDataRol)

        // Botones

        btnUpdateUserData = findViewById(R.id.btnUpdateUserData)
        btnCloseUser = findViewById(R.id.btnCloseUser)
    }

    // Navegacion de vuelta a Admin
    private fun backAdmin() {
        btnCloseUser.setOnClickListener {
            onBackPressed()
        }
    }

    // Actualizar Datos
    private fun updateData() {
        btnUpdateUserData.setOnClickListener {
            val url = URLBASE +"/ActualizarUsuario/"+UpdateDataEmail.text.toString()
            val queue = Volley.newRequestQueue(this)
            queue.add(updateUser(url))
        }
    }

    // Funcion actualizar datos
    private fun updateUser(url : String) : JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("correoUsu", UpdateDataEmail.text.toString())
            parametros.put("contraseniaUsu", UpdateDataPassword.text.toString())
            parametros.put("nombreUsu", UpdateDataName.text.toString())
            parametros.put("fotoUsu", UpdateDataPhoto.text.toString())
            parametros.put("tipoUsuario", UpdateDataRol.text.toString())
            
        }catch (e:JSONException){
            Log.e("UpdateUserJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parametros,
            { response ->
                Toast.makeText(
                    UpdateDataEmail.context,
                    response.getString("respuesta"),
                    Toast.LENGTH_SHORT
                ).show()
            }, { error ->
                Log.e("UpdateUser", error.toString())
            })

        return jsonObjectRequest
    }
}