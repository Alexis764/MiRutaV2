package com.example.mirutav2.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mirutav2.R
import com.example.mirutav2.home.admin.bus.AddBusActivity
import com.example.mirutav2.home.admin.bus.ListBusActivity
import com.example.mirutav2.home.admin.route.AddRouteActivity
import com.example.mirutav2.home.admin.route.ListRouteActivity
import com.example.mirutav2.home.admin.stop.AddStopActivity
import com.example.mirutav2.home.admin.stop.ListStopActivity
import com.example.mirutav2.home.admin.user.AddUserActivity
import com.example.mirutav2.home.admin.user.ListUserActivity
import com.example.mirutav2.map.MapActivityAdmin

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
    private lateinit var btnListRoute : Button

        //Stop
    private lateinit var btnAddStop: Button
    private lateinit var btnListStop : Button

        //User
    private lateinit var btnAddUser: Button
    private lateinit var btnListUser : Button

        //Bus
    private lateinit var btnAddBus: Button
    private lateinit var btnListBus : Button



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
        btnListRoute = rootView.findViewById(R.id.btnListRoute)

        //Botones Stop
        btnAddStop = rootView.findViewById(R.id.btnAddStop)
        btnListStop = rootView.findViewById(R.id.btnListStop)

        //Botones User
        btnAddUser = rootView.findViewById(R.id.btnAddUser)
        btnListUser = rootView.findViewById(R.id.btnListUser)

        //Botones Bus
        btnAddBus = rootView.findViewById(R.id.btnAddBus)
        btnListBus = rootView.findViewById(R.id.btnListBus)


    }


    private fun initNavigation() {

            // Botones Ruta
        btnAddRoute.setOnClickListener{
            val intent = Intent(this.context, AddRouteActivity::class.java)
            startActivity(intent)
        }

        btnListRoute.setOnClickListener {
            val intent = Intent(this.context, ListRouteActivity::class.java)
            startActivity(intent)
        }

            // Botones Paradas
        btnAddStop.setOnClickListener{
            val intent = Intent(this.context, MapActivityAdmin::class.java)
            startActivity(intent)
        }

        btnListStop.setOnClickListener {
            val intent = Intent(this.context, ListStopActivity::class.java)
            startActivity(intent)
        }

            // Botones Usuario
        btnAddUser.setOnClickListener{
            val intent = Intent(this.context, AddUserActivity::class.java)
            startActivity(intent)
        }

        btnListUser.setOnClickListener {
            val intent = Intent(this.context, ListUserActivity::class.java)
            startActivity(intent)
        }

            //Botones Bus
        btnAddBus.setOnClickListener{
            val intent = Intent(this.context, AddBusActivity::class.java)
            startActivity(intent)
        }

        btnListBus.setOnClickListener {
            val intent = Intent(this.context, ListBusActivity::class.java)
            startActivity(intent)
        }


    }



}