package com.example.ex221210.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ex221210.MainActivity
import com.example.ex221210.R


class NonSeedFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view =inflater.inflate(R.layout.fragment_non_seed, container, false)

        var tvSeed = view.findViewById<TextView>(R.id.tvSeed)

        tvSeed.setOnClickListener{

            val spf = activity?.getSharedPreferences(
                "seed",
                Context.MODE_PRIVATE
            )
            val editor = spf?.edit()
            editor?.putString("seed","go")
            editor?.commit()


            var intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)

        }

        return view
    }


}