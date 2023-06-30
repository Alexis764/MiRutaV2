package com.example.mirutav2.home.admin.user

import com.example.mirutav2.home.UserModel

interface UserAdminListener {

    fun onBtnUpdateClicked( userModel : UserModel )
    fun onBtnDeleteClicked( userModel : UserModel )
}