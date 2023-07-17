package com.example.mirutav2.home.admin.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.*
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import com.example.mirutav2.home.HomeActivity
import com.example.mirutav2.home.HomeActivity.Companion.userModel
import com.example.mirutav2.home.UserModel
import com.example.mirutav2.home.admin.bus.ListBusActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


// Variables Configuracion recycler View
private lateinit var rvlistUser : RecyclerView
private lateinit var userAdapter : UserAdminAdapter

private var userList = ArrayList<UserModel>()

// Variable boton
private lateinit var btnCloseUser : ImageButton

class ListUserActivity : AppCompatActivity(), UserAdminListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)

        val queue = Volley.newRequestQueue(this)
        queue.add(listUser())

        initVariables()
        backAdmin()

    }


    private fun initVariables() {
        btnCloseUser = findViewById(R.id.btnCloseUser)

        rvlistUser = findViewById(R.id.rvlistUser)
    }

    private fun backAdmin() {
        btnCloseUser.setOnClickListener {
            onBackPressed()
        }
    }

    // Funcion crear Usuario
    private fun createUser(jsonUser : JSONObject) : UserModel {
        val idUsu = jsonUser.getLong("idUsu")
        val correoUsu = jsonUser.getString("correoUsu")
        val contraseniaUsu = jsonUser.getString("contraseniaUsu")
        val nombreUsu = jsonUser.getString("nombreUsu")
        val fotoUsu = jsonUser.getString("fotoUsu")
        val tipoUsuario = jsonUser.getInt("tipoUsuario")

        return UserModel(
            idUsu,
            correoUsu,
            contraseniaUsu,
            nombreUsu,
            fotoUsu,
            tipoUsuario
        )

    }


    // Funcion traer Usuarios
    private fun listUser(url : String = "$URLBASE/usuario/listar") : StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url,{ response ->
            val jsonArray = JSONArray(response)

            // Limpiar la Lista
            userList.clear()

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray){
                userList.add(createUser(jsonArray[i] as JSONObject))
                i++
            }

            torvlistUser(userList)

        }, { error ->
            Log.e("Volley_getUserAdmin", error.toString())
        })

        return stringRequest
    }


    // Funcion asignar adaptador
    private fun torvlistUser (userList: MutableList<UserModel>){
        userAdapter = UserAdminAdapter(userList,this)
        rvlistUser.layoutManager = LinearLayoutManager(this)
        rvlistUser.adapter = userAdapter
    }

    override fun onBtnUpdateClicked(userModel: UserModel) {
        val intent = Intent(this, UpdateUserActivity::class.java)
        intent.putExtra("userId", userModel.idUsu)
        startActivity(intent)
    }

    override fun onBtnDeleteClicked(userModel: UserModel) {
        val url = "$URLBASE/usuario/eliminar/${userModel.idUsu}"
        val queue = Volley.newRequestQueue(this)
        queue.add(deleteUser(url, userModel))
    }

    // Funcion eliminar Usuario
    private fun deleteUser(url: String, userModel: UserModel): JsonObjectRequest {
        val parametros = JSONObject()

        try {
            parametros.put("idUsu", userModel.idUsu.toString())
        } catch (e: JSONException) {
            Log.e("DeleteUserJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, parametros,
            { response ->
                Toast.makeText(this, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
                // Eliminar el elemento de la lista después de eliminarlo en el servidor
                userList.remove(userModel)
            },
            { error ->
                Log.e("DeleteUser", error.toString())
            }
        )

        return jsonObjectRequest
    }

}