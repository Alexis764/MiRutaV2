package com.example.mirutav2.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.mirutav2.R

class HomeFragment : Fragment() {

    private lateinit var switch: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_home, container, false)
        initComponent(rootView)
        initSwitch()
        return rootView
    }

    private fun initComponent(rootView: View) {
        switch = rootView.findViewById(R.id.switch1)
    }

    private fun initSwitch() {
        val activity = requireActivity()
        val sp: SharedPreferences = activity.getSharedPreferences("SP", Context.MODE_PRIVATE)
        val editor = sp.edit()

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editor.putInt("Theme", 0)
            } else {
                editor.putInt("Theme", 1)
            }
            editor.apply()
            setDayNight(activity as AppCompatActivity)
        }
    }

    private fun setDayNight(activity: AppCompatActivity) {
        val sp: SharedPreferences = activity.getSharedPreferences("SP", Context.MODE_PRIVATE)
        val theme = sp.getInt("Theme", 1)
        if (theme == 0) {
            activity.delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            activity.delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
    }
}
