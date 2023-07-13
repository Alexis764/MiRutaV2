package com.example.mirutav2.home.admin.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject

class AddUserActivity : AppCompatActivity() {

    // Variables Datos
    private lateinit var AddDataEmail : TextInputEditText
    private lateinit var AddDataPassword : TextInputEditText
    private lateinit var AddDataName : TextInputEditText
    private lateinit var AddDataPhoto : TextInputEditText
    private lateinit var AddDataRol : TextInputEditText

    // Variable Botones
    private lateinit var btnCloseUser : ImageButton
    private lateinit var btnSendUserData : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        initData()
        backAdmin()
        sendData()

    }

    // Recoleccion de datos

    private fun initData() {
        AddDataEmail = findViewById(R.id.AddDataEmail)
        AddDataPassword = findViewById(R.id.AddDataPassword)
        AddDataName = findViewById(R.id.AddDataName)
        AddDataPhoto = findViewById(R.id.AddDataPhoto)
        AddDataRol = findViewById(R.id.AddDataRol)

        // Botones

        btnSendUserData = findViewById(R.id.btnSendUserData)
        btnCloseUser = findViewById(R.id.btnCloseUser)
    }

    // Navegacion de vuelta a Admin

    private fun backAdmin() {
        btnCloseUser.setOnClickListener {
            onBackPressed()
        }
    }


    // Enviar/Añadir datos a BD

    private fun sendData() {
        btnSendUserData.setOnClickListener {

            // Limpiar espacios en blanco
            val email = AddDataEmail.text.toString().trim()
            val password = AddDataPassword.text.toString().trim()
            val name = AddDataName.text.toString().trim()
            val photo = AddDataPhoto.text.toString().trim()
            val rol = AddDataRol.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && photo.isNotEmpty() && rol.isNotEmpty()) {
                val url = "$URLBASE/usuario/guardar"
                val queue = Volley.newRequestQueue(this)
                queue.add(AddUser(url))
            }else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funcion agregar Usuario

    private fun AddUser(url : String): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("correoUsu", AddDataEmail.text.toString())
            parametros.put("contraseniaUsu", AddDataPassword.text.toString())
            parametros.put("nombreUsu", AddDataName.text.toString())
            parametros.put("fotoUsu", AddDataPhoto.text.toString())
            parametros.put("tipoUsuario", AddDataRol.text.toString())

        }catch (e: JSONException){
            Log.e("AddUserJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parametros, { response ->
            Toast.makeText(AddDataEmail.context, response.getString("registro"), Toast.LENGTH_SHORT).show()

        }, { error ->
            Log.e("AddUser", error.toString())
        })

        return jsonObjectRequest
    }


}