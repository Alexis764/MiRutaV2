package com.example.mirutav2.home.admin.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.R
import com.example.mirutav2.home.AdminFragment
import com.example.mirutav2.home.HomeActivity.Companion.URLBASE
import org.json.JSONException
import org.json.JSONObject

class AddUserActivity : AppCompatActivity() {

    // Variables Datos
    private lateinit var AddDataEmail : EditText
    private lateinit var AddDataPassword : EditText
    private lateinit var AddDataName : EditText
    private lateinit var AddDataPhoto : EditText
    private lateinit var AddDataRol : EditText

    // Variable Botones
    private lateinit var btnCloseUser : Button
    private lateinit var btnSendUserData : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        initData()
        backAdmin()
        //sendData()

    }

    // Recoleccion de datos

    private fun initData() {
        AddDataEmail = findViewById(R.id.AddDataEmail)
        AddDataPassword = findViewById(R.id.AddDataPassword)
        AddDataName = findViewById(R.id.AddDataName)
        AddDataPhoto = findViewById(R.id.AddDataPhoto)
        AddDataRol = findViewById(R.id.AddDataRol)
    }

    // Navegacion de vuelta a Admin

    private fun backAdmin() {
        btnCloseUser.setOnClickListener {

        }
    }

    private fun onBackPressed(adminFragment: Any) {

    }

    // Enviar/Añadir datos a BD

    /*private fun sendData() {
        val url = URLBASE+"/AgregarUsuario"
        val queue = Volley.newRequestQueue(this)
        queue.add(AddUser(url))
    }

    // Funcion agregar Usuario

    private fun AddUser(url : String): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("correoUsu", AddDataEmail.text.toString())
            parametros.put("contrasenaUsu", AddDataPassword.text.toString())
            parametros.put("nombreUsu", AddDataName.text.toString())
            parametros.put("fotoUsu", AddDataPhoto.text.toString())
            parametros.put("rol", AddDataRol.text.toString())

        }catch (e: JSONException){
            Log.e("AddUserJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parametros, { response ->
            Toast.makeText(requireContext(), response.getString("respuesta"), Toast.LENGTH_SHORT).show()

        }, { error ->
            Log.e("AddUser", error.toString())
        })

        return jsonObjectRequest
    }*/


}