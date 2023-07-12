package com.example.mirutav2.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.mirutav2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope


val Context.dataStoreTheme: DataStore<Preferences> by preferencesDataStore(name = "theme")

class HomeFragment : Fragment() {

    private lateinit var switch: Switch
    private val KEY_THEME = "key_theme"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_home, container, false)

        initComponent(rootView)
        initUi()

        lifecycleScope.launch {
            getSettings().collect { theme ->
                // Datos Theme
                requireActivity().runOnUiThread {
                    if (switch.isChecked != theme) {
                        switch.isChecked = theme
                        switch.jumpDrawablesToCurrentState() // Detener cualquier animación en curso
                    }
                }
            }
        }


        return rootView
    }

    private fun initComponent(rootView: View) {
        switch = rootView.findViewById(R.id.switch1)
    }

    private fun initUi() {
        switch.setOnCheckedChangeListener { _, value ->
            if (value) {
                enableDarkMode()
            } else {
                disableDarkMode()
            }

            CoroutineScope(Dispatchers.IO).launch {
                saveTheme(KEY_THEME, value)
            }
        }
    }

    private suspend fun saveTheme(key: String, value: Boolean) {
        val context = requireContext()

        context.dataStoreTheme.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    private fun getSettings(): Flow<Boolean> {
        val context = requireContext()

        return context.dataStoreTheme.data.map { preferences ->
            preferences[booleanPreferencesKey(KEY_THEME)] ?: false
        }
    }

    private fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }
}
