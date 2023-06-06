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
import com.example.mirutav2.R
import com.example.mirutav2.home.HomeActivity
import com.example.mirutav2.home.HomeActivity.Companion.URLBASE
import org.json.JSONException
import org.json.JSONObject

class DeleteUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_user)


        initVariable()
        deleteData()
        backAdmin()
    }


    // Variables
    private lateinit var DeleteDataEmail : EditText

    // Variable Boton
    private lateinit var btnDeleteUserData : Button
    private lateinit var btnCloseUser : ImageButton

    private fun initVariable() {
        // Variable
        DeleteDataEmail = findViewById(R.id.DeleteDataEmail)

        // Botones
        btnDeleteUserData = findViewById(R.id.btnDeleteUserData)
        btnCloseUser = findViewById(R.id.btnCloseUser)
    }


    // Elimina el dato
    private fun deleteData() {
        btnDeleteUserData.setOnClickListener {
            val url = URLBASE +"/Usuario/EliminarUsuario/"+ DeleteDataEmail.text.toString()
            val queue = Volley.newRequestQueue(this)
            queue.add(deleteUser(url))
        }
    }

    // Navegacion de vuelta a Admin
    private fun backAdmin() {
        btnCloseUser.setOnClickListener {
            onBackPressed()
        }
    }


    // Funcion eliminar Usuario
    private fun deleteUser(url: String): JsonObjectRequest  {
        val parametros = JSONObject()

        try {
            parametros.put("correoUsu", DeleteDataEmail.text.toString())
        } catch (e: JSONException) {
            Log.e("DeleteUserJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, parametros,
            { response ->
                Toast.makeText(DeleteDataEmail.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
            },
            { error ->
                Log.e("DeleteUser", error.toString())
            }
        )

        return jsonObjectRequest
    }


}