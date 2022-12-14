package com.example.ex221210.fragment


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.ex221210.R
import kotlin.random.Random


class Fragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_1, container, false)
//        var animationView2 = view.findViewById<LottieAnimationView>(R.id.animationView2)
//        var tvPercent = view.findViewById<TextView>(R.id.tvPercent)
//        var zeroToHun = 0
//
//        val handler = Handler(Looper.getMainLooper())
//        val runnable = Runnable{
//            animationView2.cancelAnimation()
//        }
//        handler.postDelayed(runnable,2000)
////
//        val countDownTimer1 = object : CountDownTimer(2000,10){
//            override fun onTick(p0: Long) {
//                tvPercent.text = zeroToHun.toString()+"%"
//                zeroToHun++
//
//            }
//            override fun onFinish() {
//            }
//        }
//        countDownTimer1.start()
//        애니메이션 이미지











        return view
    }

}