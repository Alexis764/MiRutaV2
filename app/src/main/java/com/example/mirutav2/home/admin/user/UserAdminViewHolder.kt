package com.example.mirutav2.home.admin.user

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.UserModel

class UserAdminViewHolder(view : View) : RecyclerView.ViewHolder(view) {


    // Variables componentes vista
    private val itemidentification = view.findViewById<TextView>(R.id.item_admin_identificationUser)
    private val itemname = view.findViewById<TextView>(R.id.item_admin_nameUsu)
    private val itememail = view.findViewById<TextView>(R.id.item_admin_emailUsu)
    private val itemrol = view.findViewById<TextView>(R.id.item_admin_rol)

    // Variable Botones

    val btnitemUpdate = view.findViewById<Button>(R.id.btn_item_admin_update_user)
    val btnitemDelete = view.findViewById<Button>(R.id.btn_item_admin_delete_user)

    // Funcion renderizado

    fun render(userModel: UserModel) {

        itemidentification.text = userModel.idUsu.toString()
        itemname.text = userModel.nombreUsu
        itememail.text = userModel.correoUsu
        itemrol.text = userModel.tipoUsuario.toString()

    }


}