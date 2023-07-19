package com.example.mirutav2.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mirutav2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.example.mirutav2.home.HomeActivity.Companion.USUEMAILPREFERENCE
import com.example.mirutav2.home.HomeActivity.Companion.USUNAMEPREFERENCE
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.flow.filter

class HomeFragment : Fragment() {

    //Constantes
    companion object {
        const val KEYTHEMEDARK = "key_theme"
    }



    //Variables para componentes
    private lateinit var tvWelcomeUser: TextView
    private lateinit var tvEmailUser: TextView
    private lateinit var switchDarkMode: SwitchMaterial



    //Variables
    private var firstTime = true



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        initComponent(rootView)
        initListeners()
        initUi()

        return rootView
    }



    //Conexion de componentes a vista
    private fun initComponent(rootView: View) {
        tvWelcomeUser = rootView.findViewById(R.id.tvWelcomeUser)
        tvEmailUser = rootView.findViewById(R.id.tvEmailUser)
        switchDarkMode = rootView.findViewById(R.id.switchDarkMode)
    }



    //Funciones click de componentes
    private fun initListeners() {
        switchDarkMode.setOnCheckedChangeListener { _, value ->
            changeDarkMode(value)

            CoroutineScope(Dispatchers.IO).launch {
                saveTheme(KEYTHEMEDARK, value)
            }
        }
    }



    //Iniciar los componentes de la interfaz respecto a valores predeterminados
    private fun initUi() {
        CoroutineScope(Dispatchers.IO).launch {
            getUserInfo().filter { firstTime }.collect{userModelInfo ->
                if (userModelInfo != null) {
                    requireActivity().runOnUiThread {
                        tvWelcomeUser.text = getString(R.string.welcome, userModelInfo.nombreUsu)
                        tvEmailUser.text = userModelInfo.correoUsu

                        if (userModelInfo.correoUsu.isNotEmpty()) {
                            changeDarkMode(switchDarkMode.isChecked)
                            firstTime = !firstTime
                        }

                        switchDarkMode.isChecked = userModelInfo.darkMode
                    }
                }
            }
        }

    }



    //Funciones para el manejo del modo oscuro
    //Funcion para guardar el estado de la opcion del usuario del modo oscuro
    private suspend fun saveTheme(key: String, value: Boolean) {
        tvWelcomeUser.context.dataStoreUserInfo.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    //Funcion para cambiar el modo oscuro
    private fun changeDarkMode(value: Boolean) {
        if (value) AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES) else AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }



    //Funcion para retornar la informacion del usuario
    private fun getUserInfo(): Flow<UserModelInfo?> {
        return tvWelcomeUser.context.dataStoreUserInfo.data.map {
            UserModelInfo(
                correoUsu = it[stringPreferencesKey(USUEMAILPREFERENCE)].orEmpty(),
                nombreUsu = it[stringPreferencesKey(USUNAMEPREFERENCE)].orEmpty(),
                darkMode = it[booleanPreferencesKey(KEYTHEMEDARK)] ?: false
            )
        }
    }



}
