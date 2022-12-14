package com.example.ex221210.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.ex221210.MainMapActivity
import com.example.ex221210.MapSplashActivity
import com.example.ex221210.R

class Fragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_2, container, false)

        val btnFruit = view.findViewById<Button>(R.id.btnFruit)
        val btnVegi = view.findViewById<Button>(R.id.btnVegi)
        val btnFlower = view.findViewById<Button>(R.id.btnFlower)

        btnVegi.setOnClickListener {

            val intent = Intent(requireContext(),MapSplashActivity::class.java)
            startActivity(intent)
        }




        return view
    }

}