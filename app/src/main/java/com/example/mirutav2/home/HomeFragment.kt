package com.example.mirutav2.home

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.example.mirutav2.R
import com.example.mirutav2.home.HomeActivity.Companion.userModel
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity
import com.example.mirutav2.MainActivity.Companion.FOTOUSUPREFERENCE
import com.example.mirutav2.dataStoreUserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

val Context.dataStoreTheme: DataStore<Preferences> by preferencesDataStore(name = "theme")
class HomeFragment : Fragment() {

    //Constantes
    companion object {
        const val KEYTHEMEDARK = "key_theme"

        const val COD_SEL_IMAGE = 300
    }



    //Variables para componentes
    private lateinit var tvWelcomeUser: TextView
    private lateinit var tvEmailUser: TextView
    private lateinit var ivPhotoUser: ImageView
    private lateinit var switchDarkMode: SwitchMaterial
    private lateinit var btnUploadImage: Button
    private lateinit var btnLogout: Button

    //Variables para componentes del dialog
    private lateinit var btnLogoutCon: Button
    private lateinit var btnCancelLogout: Button



    //Variables
    private var firstTime = true
    private lateinit var queue: RequestQueue



    //Variables firebase
    private lateinit var mFireStore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private val storagePath = "user/*"

    private lateinit var imageUrl: Uri
    private val photo = "photo"



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
        ivPhotoUser = rootView.findViewById(R.id.ivPhotoUser)
        switchDarkMode = rootView.findViewById(R.id.switchDarkMode)
        btnUploadImage = rootView.findViewById(R.id.btnUploadImage)
        btnLogout = rootView.findViewById(R.id.btnLogout)

        queue = Volley.newRequestQueue(this.context)

        mFireStore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
    }



    //Funciones click de componentes
    private fun initListeners() {
        switchDarkMode.setOnCheckedChangeListener { _, value ->
            changeDarkMode(value)
            CoroutineScope(Dispatchers.IO).launch {
                saveTheme(KEYTHEMEDARK, value)
            }
        }

        btnUploadImage.setOnClickListener {
            openGallery()
        }

        btnLogout.setOnClickListener {
            val dialog = Dialog(tvWelcomeUser.context)
            dialog.setContentView(R.layout.dialog_logout)
            dialog.show()
            initComponentDialog(dialog)

            btnLogoutCon.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch{
                    deleteUserInfo()
                }
                val intent = Intent(tvWelcomeUser.context, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            btnCancelLogout.setOnClickListener {
                dialog.hide()
            }
        }
    }



    //Iniciar los componentes de la interfaz respecto a valores predeterminados
    private fun initUi() {
        CoroutineScope(Dispatchers.IO).launch {
            getSettings().filter { firstTime }.collect{ darkMode ->
                if (darkMode != null) {
                    requireActivity().runOnUiThread{
                        switchDarkMode.isChecked = darkMode
                        changeDarkMode(darkMode)
                    }
                    firstTime = !firstTime
                }
            }
        }

        tvWelcomeUser.text = getString(R.string.welcome, userModel.nombreUsu)
        tvEmailUser.text = userModel.correoUsu
        loadPhotoUser(userModel.fotoUsu)
    }

    //Funcion para cargar la foto del usuario
    private fun loadPhotoUser(fotoUsu: String) {
        try {
            Glide.with(tvWelcomeUser.context)
                .load(fotoUsu)
                .circleCrop()
                .into(ivPhotoUser)

        } catch (e: Exception) {
            Log.e("loadPhotoUser", "No carga la imagen")
        }
    }



    //Funciones para el manejo del modo oscuro
    //Funcion para guardar el estado de la opcion del usuario del modo oscuro
    private suspend fun saveTheme(key: String, value: Boolean) {
        tvWelcomeUser.context.dataStoreTheme.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    //Funcion para retornar la opcion del modo oscuro del usuario
    private fun getSettings(): Flow<Boolean?> {
        return tvWelcomeUser.context.dataStoreTheme.data.map { preferences ->
            preferences[booleanPreferencesKey(KEYTHEMEDARK)] ?: false
        }
    }

    //Funcion para cambiar el modo oscuro
    private fun changeDarkMode(value: Boolean) {
        if (value) AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES) else AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }



    //Conexion de componentes a vista dialog
    private fun initComponentDialog(dialog: Dialog) {
        btnLogoutCon = dialog.findViewById(R.id.btnLogoutCon)
        btnCancelLogout = dialog.findViewById(R.id.btnCancelLogout)
    }

    //Eliminar la informacion del usuario cuando se cierre sesion
    private suspend fun deleteUserInfo() {
        tvWelcomeUser.context.dataStoreUserInfo.edit { preference ->
            preference.clear()
        }
    }



    //Funciones para subir foto a firebase
    //Funcion para abrir galeria
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, COD_SEL_IMAGE)
    }

    //Funcion para recibir el resultado de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == COD_SEL_IMAGE) {
                imageUrl = data?.data!!
                uploadImage(imageUrl)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //Funcion para subir la imagen a firebase
    private fun uploadImage(imageUrl: Uri) {
        val ruteStoragePhoto = storagePath + "" + photo + "" + userModel.correoUsu + "" + userModel.idUsu
        val reference = storageReference.child(ruteStoragePhoto)

        reference.putFile(imageUrl).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isSuccessful) {
                if (uriTask.isSuccessful) {
                    uriTask.addOnSuccessListener { uri ->
                        val uriDownload = uri.toString()
                        loadPhotoUser(uriDownload)
                        queue.add(updateImageUser(uriDownload))
                        CoroutineScope(Dispatchers.IO).launch { updateImageUserInfo(uriDownload) }
                        Toast.makeText(tvWelcomeUser.context, "Foto actualizada", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    //Funcion que guarda la imagen en preference
    private suspend fun updateImageUserInfo(uriDownload: String) {
        tvWelcomeUser.context.dataStoreUserInfo.edit {
            it[stringPreferencesKey(FOTOUSUPREFERENCE)] = uriDownload
        }
    }

    //Funcion para actualizar la url de la imagen en la api
    private fun updateImageUser(uriDownload: String, url: String = "${MainActivity.URLBASE}/usuario/actualizar"): JsonObjectRequest {
        val parameters = JSONObject()

        try {
            parameters.put("idUsu", userModel.idUsu)
            parameters.put("correoUsu", userModel.correoUsu)
            parameters.put("contraseniaUsu", userModel.contraseniaUsu)
            parameters.put("nombreUsu", userModel.nombreUsu)
            parameters.put("fotoUsu", uriDownload)
            parameters.put("tipoUsuario", userModel.tipoUsuario)

        } catch (e: JSONException) {
            Log.e("updateImageUser_JSON", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url, parameters, { response ->
            Toast.makeText(tvWelcomeUser.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()

        }, {error ->
            Log.e("updateImageUser_REQUEST", error.toString())
        })

        return jsonObjectRequest
    }


}
