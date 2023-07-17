package com.example.mirutav2.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mirutav2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.mirutav2.home.HomeActivity.Companion.USUEMAILPREFERENCE
import com.example.mirutav2.home.HomeActivity.Companion.USUNAMEPREFERENCE
import com.google.android.material.switchmaterial.SwitchMaterial

val Context.dataStoreTheme: DataStore<Preferences> by preferencesDataStore(name = "theme")
class HomeFragment : Fragment() {

    //Constantes
    companion object {
        const val KEYTHEMEDARK = "key_theme"
    }



    //Variables para componentes
    private lateinit var tvWelcomeUser: TextView
    private lateinit var tvEmailUser: TextView
    private lateinit var switchDarkMode: SwitchMaterial



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        initComponent(rootView)
        initConfiguration()
        initUi()

        return rootView
    }



    //Funcion que restaurar la configuracion del usuario
    private fun initConfiguration() {
        lifecycleScope.launch {
            getSettings().collect { theme ->
                requireActivity().runOnUiThread {
                    if (switchDarkMode.isChecked != theme) {
                        switchDarkMode.isChecked = theme
                        switchDarkMode.jumpDrawablesToCurrentState() // Detener cualquier animación en curso
                    }
                }
            }
        }
    }



    //Conexion de componentes a vista
    private fun initComponent(rootView: View) {
        tvWelcomeUser = rootView.findViewById(R.id.tvWelcomeUser)
        tvEmailUser = rootView.findViewById(R.id.tvEmailUser)
        switchDarkMode = rootView.findViewById(R.id.switchDarkMode)
    }



    //Iniciar los componentes de la interfaz respecto a valores predeterminados
    private fun initUi() {
        switchDarkMode.setOnCheckedChangeListener { _, value ->
            if (value) {
                enableDarkMode()
            } else {
                disableDarkMode()
            }

            CoroutineScope(Dispatchers.IO).launch {
                saveTheme(KEYTHEMEDARK, value)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            getUserInfo().collect{userModelInfo ->
                if (userModelInfo != null) {
                    requireActivity().runOnUiThread {
                        tvWelcomeUser.text = getString(R.string.welcome, userModelInfo.nombreUsu)
                        tvEmailUser.text = userModelInfo.correoUsu
                    }
                }
            }
        }
    }



    //Funciones para el manejo del modo oscuro
    //Funcion para guardar el estado de la opcion del usuario
    private suspend fun saveTheme(key: String, value: Boolean) {
        val context = requireContext()

        context.dataStoreTheme.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    //Funcion para retornar la opcion del usuario
    private fun getSettings(): Flow<Boolean> {
        val context = requireContext()

        return context.dataStoreTheme.data.map { preferences ->
            preferences[booleanPreferencesKey(KEYTHEMEDARK)] ?: false
        }
    }

    //Funcion para activar el modo oscuro
    private fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
    }

    //Funcion para desactivar el modo oscuro
    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }



    //Funcion para retornar la informacion del usuario
    private fun getUserInfo(): Flow<UserModelInfo?> {
        return tvWelcomeUser.context.dataStoreUserInfo.data.map {
            UserModelInfo(
                correoUsu = it[stringPreferencesKey(USUEMAILPREFERENCE)].orEmpty(),
                nombreUsu = it[stringPreferencesKey(USUNAMEPREFERENCE)].orEmpty()
            )
        }
    }
}
