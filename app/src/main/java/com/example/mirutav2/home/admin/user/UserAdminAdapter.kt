package com.example.mirutav2.home.admin.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirutav2.R
import com.example.mirutav2.home.UserModel

class UserAdminAdapter (
    var userList : List<UserModel>,
    private val useradminlistener : UserAdminListener) :
    RecyclerView.Adapter<UserAdminViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_listar_user, parent, false)
        return UserAdminViewHolder(view)
    }

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: UserAdminViewHolder, position: Int) {
        holder.render(userList[position])

        holder.btnitemUpdate.setOnClickListener {
            useradminlistener.onBtnUpdateClicked(userList[position])
        }

        holder.btnitemDelete.setOnClickListener {
            useradminlistener.onBtnDeleteClicked(userList[position])
            notifyDataSetChanged()
        }
    }


}