package com.example.mirutav2.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.mirutav2.R
import com.example.mirutav2.home.admin.bus.AddBusActivity
import com.example.mirutav2.home.admin.bus.DeleteBusActivity
import com.example.mirutav2.home.admin.bus.UpdateBusActivity
import com.example.mirutav2.home.admin.route.AddRouteActivity
import com.example.mirutav2.home.admin.route.DeleteRouteActivity
import com.example.mirutav2.home.admin.route.UpdateRouteActivity
import com.example.mirutav2.home.admin.stop.AddStopActivity
import com.example.mirutav2.home.admin.stop.DeleteStopActivity
import com.example.mirutav2.home.admin.stop.UpdateStopActivity
import com.example.mirutav2.home.admin.user.AddUserActivity
import com.example.mirutav2.home.admin.user.DeleteUserActivity
import com.example.mirutav2.home.admin.user.UpdateUserActivity
import com.google.android.material.textfield.TextInputEditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AdminFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    //Variables Botones Navegacion
        //Route
    private lateinit var btnAddRoute: Button
    private lateinit var btnUpdateRoute: Button
    private lateinit var btnDeleteRoute: Button
        //Stop
    private lateinit var btnAddStop: Button
    private lateinit var btnUpdateStop: Button
    private lateinit var btnDeleteStop: Button
        //User
    private lateinit var btnAddUser: Button
    private lateinit var btnUpdateUser: Button
    private lateinit var btnDeleteUser: Button
        //Bus
    private lateinit var btnAddBus: Button
    private lateinit var btnUpdateBus: Button
    private lateinit var btnDeleteBus: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView : View = inflater.inflate(R.layout.fragment_admin, container, false)

        initVariables(rootView)
        initNavigation()



        return rootView
    }



    private fun initVariables(rootView: View) {

        //Botones Route
        btnAddRoute = rootView.findViewById(R.id.btnAddRoute)
        btnUpdateRoute = rootView.findViewById(R.id.btnUpdateRoute)
        btnDeleteRoute = rootView.findViewById(R.id.btnDeleteRoute)
        //Botones Stop
        btnAddStop = rootView.findViewById(R.id.btnAddStop)
        btnUpdateStop = rootView.findViewById(R.id.btnUpdateStop)
        btnDeleteStop = rootView.findViewById(R.id.btnDeleteStop)
        //Botones User
        btnAddUser = rootView.findViewById(R.id.btnAddUser)
        btnUpdateUser = rootView.findViewById(R.id.btnUpdateUser)
        btnDeleteUser = rootView.findViewById(R.id.btnDeleteUser)
        //Botones Bus
        btnAddBus = rootView.findViewById(R.id.btnAddBus)
        btnUpdateBus = rootView.findViewById(R.id.btnUpdateBus)
        btnDeleteBus = rootView.findViewById(R.id.btnDeleteBus)

    }


    private fun initNavigation() {

            // Botones Ruta
        btnAddRoute.setOnClickListener{
            val intent = Intent(this.context, AddRouteActivity::class.java)
            startActivity(intent)
        }
        btnUpdateRoute.setOnClickListener{
            val intent = Intent(this.context, UpdateRouteActivity::class.java)
            startActivity(intent)
        }
        btnDeleteRoute.setOnClickListener{
            val intent = Intent(this.context, DeleteRouteActivity::class.java)
            startActivity(intent)
        }
            // Botones Paradas
        btnAddStop.setOnClickListener{
            val intent = Intent(this.context, AddStopActivity::class.java)
            startActivity(intent)
        }
        btnUpdateStop.setOnClickListener{
            val intent = Intent(this.context, UpdateStopActivity::class.java)
            startActivity(intent)
        }
        btnDeleteStop.setOnClickListener{
            val intent = Intent(this.context, DeleteStopActivity::class.java)
            startActivity(intent)
        }
            // Botones Usuario
        btnAddUser.setOnClickListener{
            val intent = Intent(this.context, AddUserActivity::class.java)
            startActivity(intent)
        }
        btnUpdateUser.setOnClickListener{
            val intent = Intent(this.context, UpdateUserActivity::class.java)
            startActivity(intent)
        }
        btnDeleteUser.setOnClickListener{
            val intent = Intent(this.context, DeleteUserActivity::class.java)
            startActivity(intent)
        }
            //Botones Bus
        btnAddBus.setOnClickListener{
            val intent = Intent(this.context, AddBusActivity::class.java)
            startActivity(intent)
        }
        btnUpdateBus.setOnClickListener{
            val intent = Intent(this.context, UpdateBusActivity::class.java)
            startActivity(intent)
        }
        btnDeleteBus.setOnClickListener{
            val intent = Intent(this.context, DeleteBusActivity::class.java)
            startActivity(intent)
        }


    }



}