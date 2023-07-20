package com.example.mirutav2.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import android.util.Log
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import com.example.mirutav2.dataStoreUserInfo
import com.google.android.material.navigationrail.NavigationRailView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    //Constantes
    companion object {
        lateinit var userModel: UserModel
        var userDriverId: Long = 0
    }



    //Variables
    private lateinit var navController: NavController
    private lateinit var queue: RequestQueue



    //Variables para componentes
    private lateinit var nrvHome: NavigationRailView
    private lateinit var fcvHome: NavHostFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initUserInfo()
        initComponent()
        initController()

    }



    //Funcion para retornar la informacion del usuario
    private fun initUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            getUserInfo().collect{ userModelPreference ->
                if (userModelPreference != null) {
                    userModel = userModelPreference
                    runOnUiThread{ setMenuVisibility(userModel.tipoUsuario) }
                }
            }
        }
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



    //Funcion para cambiar la visibilidad de la opcion de administrador en el menu de navegacion
    private fun setMenuVisibility(typeUser: Int) {
        when (typeUser) {
            2 -> { //Driver
                nrvHome.menu.getItem(5).isEnabled = true
                nrvHome.menu.getItem(5).isVisible = true
                queue.add(getDriverUser())

            }
            0 -> { //Admin
                nrvHome.menu.getItem(4).isEnabled = true
                nrvHome.menu.getItem(4).isVisible = true
            }
        }
    }



    //Funciones para traer la informacion del conductor en caso de que ese sea le rol del usuario
    private fun getDriverUser(url: String = "$URLBASE/conductor/buscar/${userModel.idUsu}"): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, {response ->
            val jsonObject = JSONObject(response)

            userDriverId = jsonObject.getLong("identificacionCon")

        }, {error ->
            Log.e("Volley_getDriverUser", error.toString())
        })

        return stringRequest
    }



    //Funcion para retornar los datos guardados del usuario
    private fun getUserInfo(): Flow<UserModel?> {
        return dataStoreUserInfo.data.map { preference ->
            UserModel(
                idUsu = preference[longPreferencesKey(MainActivity.IDUSUPREFERENCE)] ?: 0,
                correoUsu = preference[stringPreferencesKey(MainActivity.CORREOUSUPREFERENCE)].orEmpty(),
                contraseniaUsu = preference[stringPreferencesKey(MainActivity.CONTRAUSUPREFERENCE)].orEmpty(),
                nombreUsu = preference[stringPreferencesKey(MainActivity.NOMBREUSUPREFERENCE)].orEmpty(),
                fotoUsu = preference[stringPreferencesKey(MainActivity.FOTOUSUPREFERENCE)].orEmpty(),
                tipoUsuario = preference[intPreferencesKey(MainActivity.TIPOUSUPREFERENCE)] ?: 1
            )
        }
    }



}