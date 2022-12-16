package com.example.ex221210.fragment



import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.ex221210.MainActivity
import com.example.ex221210.R
import com.example.ex221210.sns.ListActivity
import kotlinx.android.synthetic.main.fragment_1.view.*
import kotlinx.android.synthetic.main.sns_template.view.*
import java.text.SimpleDateFormat
import java.util.*


class Fragment1 : Fragment() {

    var time : SimpleDateFormat = SimpleDateFormat("yyyy/ MM/ dd")
    private fun getTime(): String? {
        var mNow = System.currentTimeMillis()
        var mDate = Date(mNow)
        return time.format(mDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        var currentTime = getTime()



        var view = inflater.inflate(R.layout.fragment_1, container, false)

        var btnNow = view.findViewById<Button>(R.id.btnNow)

        val loginSpf = requireContext().getSharedPreferences(
            "loginInfo",
            Context.MODE_PRIVATE
        )
        val loginId = loginSpf?.getString("loginId", "null") as String

        btnNow.setOnClickListener {
            var msgNow = loginId+"님이 토마토 지금 받기를 신청하셨습니다"
            val nowSpf = requireContext().getSharedPreferences(
                "now",
                Context.MODE_PRIVATE
            )
            val editor = nowSpf.edit()
            editor.putString("now",msgNow)
            editor.commit()

            var intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
            activity?.finish()

        }

//        var animationView2 = view.findViewById<LottieAnimationView>(R.id.animationView2)
        var tvPercent = view.findViewById<TextView>(R.id.tvPercent)
        var zeroToHun = 0

        var tvToday = view.findViewById<TextView>(R.id.tvToday)
        // 심은 날짜
//        tvToday.text = "2022/11/15"
        var tvHarvest = view.findViewById<TextView>(R.id.tvHarvest)
        // 오늘 날짜
        tvToday.text = currentTime
        tvHarvest.text = "2023/04/02"

//        val handler = Handler(Looper.getMainLooper())
//        val runnable = Runnable{
////            animationView2.cancelAnimation()
//        }
//        handler.postDelayed(runnable,2000)

        val countDownTimer1 = object : CountDownTimer(4000,30){
            override fun onTick(p0: Long) {
                if(zeroToHun<=1){
                tvPercent.text = zeroToHun.toString()+"%"
                zeroToHun++
                }
            }
            override fun onFinish() {
            }
        }
        countDownTimer1.start()


        val avMainTitle = view.findViewById<LottieAnimationView>(R.id.avMainTitle)

        val tvMainTitle = view.findViewById<TextView>(com.example.ex221210.R.id.tvMainTitle)
        avMainTitle.setOnClickListener{

            val spf = requireContext().getSharedPreferences(
                "diary",
                Context.MODE_PRIVATE
            )
            val editor = spf.edit()
            editor.putString("diary","diary")
            editor.commit()

            var intent = Intent(requireContext(),ListActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }




        return view
    }


}