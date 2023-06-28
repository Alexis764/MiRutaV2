package com.example.mirutav2.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mirutav2.R
import com.example.mirutav2.MainActivity.Companion.URLBASE
import com.example.mirutav2.home.stop.DialogStopActivity
import com.example.mirutav2.home.stop.StopAdapter
import com.example.mirutav2.home.stop.StopListener
import com.example.mirutav2.home.stop.StopModel
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONObject

class StopFragment : Fragment(), StopListener {

    //Variables
    private lateinit var queue: RequestQueue

    companion object {
        const val IDPAR = "idPar"
        const val NOMBREPAR = "nombrePar"
        const val DIRECCIONPAR = "direccionPar"
        const val LONGITUDPAR = "longitudPar"
        const val LATITUDPAR = "latitudPar"
        const val IMGPAR = "imgPar"
    }



    //Variables para componentes
    private lateinit var iedNameStop: TextInputEditText



    //Variables para configurar recyclerView
    private lateinit var rvStops: RecyclerView
    private lateinit var stopAdapter: StopAdapter

    private var stopList = ArrayList<StopModel>()
    private lateinit var stopListFilter: List<StopModel>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_stop, container, false)

        initComponent(rootView)
        initListeners()
        initUi()

        return rootView
    }



    //Conexion de componentes a vista
    private fun initComponent(rootView: View) {
        iedNameStop = rootView.findViewById(R.id.iedNameStop)
        rvStops = rootView.findViewById(R.id.rvStops)

        queue = Volley.newRequestQueue(this.context)
    }



    //Inicio de funciones click
    private fun initListeners() {
        iedNameStop.addTextChangedListener {
            filterStops(iedNameStop.text.toString())
        }
    }



    //Iniciar los componentes de la interfaz respecto a valores predeterminados
    private fun initUi() {
        initRvStops()
    }



    //Funciones para traer las paradas desde la dase de datos y ponerlas en el recyclerview
    //Iniciar el recyclerview
    private fun initRvStops() {
        queue.add(getStops())
    }

    //Funcion metodo get para traer las paradas
    private fun getStops(url: String = "$URLBASE/parada/listar"): StringRequest {
        val stringRequest = StringRequest(Request.Method.GET, url, {response ->
            val jsonArray = JSONArray(response)

            var i = 0
            val sizeArray = jsonArray.length()
            while (i < sizeArray) {
                stopList.add(createStop(jsonArray[i] as JSONObject))
                i++
            }

            toListRvStops(stopList)

        }, {error ->
            Log.e("Volley_getStops", error.toString())
        })

        return stringRequest
    }

    //Funcion para crear una parada
    private fun createStop(jsonStop: JSONObject): StopModel {
        val idPar = jsonStop.getLong("idPar")
        val nombrePar = jsonStop.getString("nombrePar")
        val direccionPar = jsonStop.getString("direccionPar")
        val longitudPar = jsonStop.getDouble("longitudPar")
        val latitudPar = jsonStop.getDouble("latitudPar")
        val imgPar = jsonStop.getString("imgPar")

        return StopModel(
            idPar,
            nombrePar,
            direccionPar,
            longitudPar,
            latitudPar,
            imgPar
        )
    }

    //Funcion para asignar el adaptador al RvStops
    private fun toListRvStops(stopList: ArrayList<StopModel>) {
        stopAdapter = StopAdapter(stopList, this)
        rvStops.layoutManager = LinearLayoutManager(this.context)
        rvStops.adapter = stopAdapter
        filterStops(iedNameStop.text.toString())
    }



    //Funcion para filtrar las paradas
    private fun filterStops(stopName: String) {
        if (::stopAdapter.isInitialized) {
            stopListFilter = stopList.filter {
                it.nombrePar.lowercase().contains(stopName.lowercase())
            }

            stopAdapter.updateStopList(stopListFilter)
        }
    }



    override fun onItemStopClicked(stopModel: StopModel) {
        val intent = Intent(this.context, DialogStopActivity::class.java)

        intent.putExtra(IDPAR, stopModel.idPar)
        intent.putExtra(NOMBREPAR, stopModel.nombrePar)
        intent.putExtra(DIRECCIONPAR, stopModel.direccionPar)
        intent.putExtra(LONGITUDPAR, stopModel.longitudPar)
        intent.putExtra(LATITUDPAR, stopModel.latitudPar)
        intent.putExtra(IMGPAR, stopModel.imgPar)

        startActivity(intent)
    }

}