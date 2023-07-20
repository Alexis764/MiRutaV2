package com.example.mirutav2.home.admin.user

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.R
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject

class AddUserActivity : AppCompatActivity() {

    // Items Spinner
    val roles = arrayOf("Administracion", "Usuario", "Conductor")


    // Variables Datos
    private lateinit var AddDataEmail : TextInputEditText
    private lateinit var AddDataPassword : TextInputEditText
    private lateinit var AddDataName : TextInputEditText
    private lateinit var AddDataPhoto : TextInputEditText
    private lateinit var AddDataRol: Spinner

    // Variable Botones
    private lateinit var btnCloseUser : ImageButton
    private lateinit var btnSendUserData : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        initData()
        backAdmin()
        sendData()
        initSpinner()

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

    // Spinner
    private fun initSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        AddDataRol.adapter = adapter
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
            val selectedRole = AddDataRol.selectedItem.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && photo.isNotEmpty() && selectedRole.isNotEmpty()) {
                if (selectedRole == "Conductor") {
                    // Mostrar el cuadro de diálogo para solicitar el documento
                    showDocumentDialog()
                } else {
                    // Si no es "Conductor", guardar el usuario sin solicitar documento
                    saveUser() // Pasa null como ID del usuario ya que aún no se ha creado
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDocumentDialog() {
        val documentDialog = Dialog(this)
        documentDialog.setContentView(R.layout.dialog_request_document)
        val btnAcceptDocument = documentDialog.findViewById<Button>(R.id.btnAcceptDocument)
        val editTextDocument = documentDialog.findViewById<EditText>(R.id.editTextDocument)

        btnAcceptDocument.setOnClickListener {
            val document = editTextDocument.text.toString().trim()
            if (document.isNotEmpty()) {
                documentDialog.dismiss()
                // Guardar el usuario con el documento ingresado
                saveUser() // No pasamos el documento aquí
            } else {
                Toast.makeText(this, "Por favor, ingrese el documento", Toast.LENGTH_SHORT).show()
            }
        }

        documentDialog.show()
    }

    /*// Guardar Usuario
    private fun saveUser(){
        val url = "$URLBASE/usuario/guardar"
        val queue = Volley.newRequestQueue(this)
        queue.add(AddUser(url))
    }

    // Guardar Conductor con documento
    private fun saveDriver(document: String) {
        val url = "$URLBASE/conductor/guardar" // Ajusta la URL según la API
        val queue = Volley.newRequestQueue(this)
        queue.add(AddDriver(url, document))
    }*/

    // Funcion agregar Usuario

    private fun saveUser() {
        val url = "$URLBASE/usuario/guardar"
        val queue = Volley.newRequestQueue(this)
        val parametros = JSONObject()

        try {
            parametros.put("correoUsu", AddDataEmail.text.toString())
            parametros.put("contraseniaUsu", AddDataPassword.text.toString())
            parametros.put("nombreUsu", AddDataName.text.toString())
            parametros.put("fotoUsu", AddDataPhoto.text.toString())
            parametros.put("tipoUsuario", getRolValue(AddDataRol.selectedItem.toString()))

        } catch (e: JSONException) {
            Log.e("AddUserJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parametros, { response ->
            // La respuesta no contiene la ID del usuario, pero indica si el registro fue exitoso
            val registroExitoso = response.optBoolean("registro", false)
            if (registroExitoso) {
                // Si el registro fue exitoso, podemos obtener la ID del usuario recién creado
                val userID = response.optLong("idUsu")
                if (getRolValue(AddDataRol.selectedItem.toString()) == 2) {
                    // Si el rol es conductor, mostramos el diálogo para pedir el documento
                    showDocumentDialogForDriver(userID)

                } else {
                    // Si no es conductor, mostramos un mensaje de éxito
                    Toast.makeText(this, "Usuario guardado exitosamente", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Manejar el caso de registro no exitoso, si es necesario
            }
        }, { error ->
            Log.e("AddUser", error.toString())
        })

        queue.add(jsonObjectRequest)
    }

    private fun showDocumentDialogForDriver(userID: Long) {
        val documentDialog = Dialog(this)
        documentDialog.setContentView(R.layout.dialog_request_document)
        val btnAcceptDocument = documentDialog.findViewById<Button>(R.id.btnAcceptDocument)
        val editTextDocument = documentDialog.findViewById<EditText>(R.id.editTextDocument)

        btnAcceptDocument.setOnClickListener {
            val document = editTextDocument.text.toString().trim()
            if (document.isNotEmpty()) {
                documentDialog.dismiss()
                // Guardar el conductor con el documento y la ID del usuario
                saveDriver(userID, document) // Se pasa el valor del documento como parámetro aquí
            } else {
                Toast.makeText(this, "Por favor, ingrese el documento", Toast.LENGTH_SHORT).show()
            }
        }

        documentDialog.show()
    }

    private fun saveDriver(userID: Long, document: String) {
        val url = "$URLBASE/conductor/guardar"
        val queue = Volley.newRequestQueue(this)
        val parametros = JSONObject()

        try {
            parametros.put("idUsu", userID)
            parametros.put("identificacionCon", document)
        } catch (e: JSONException) {
            Log.e("AddDriverJson", e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parametros, { response ->
            Toast.makeText(AddDataEmail.context, response.getString("respuesta"), Toast.LENGTH_SHORT).show()
        }, { error ->
            Log.e("AddDriver", error.toString())
        })

        queue.add(jsonObjectRequest)
    }


    private fun getRolValue(rol: String): Int {
        return when (rol) {
            "Administracion" -> 0
            "Usuario" -> 1
            "Conductor" -> 2
            else -> -1 // Valor predeterminado en caso de no coincidir ninguna opción
        }
    }

}