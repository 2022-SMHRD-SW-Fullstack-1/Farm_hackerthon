package com.example.ex221210

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class FarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm)

        var farm_name = intent.getStringExtra("farm")

        var tv = findViewById<TextView>(R.id.tv)
        val btnSeed = findViewById<Button>(R.id.btnSeed)
        tv.text = farm_name





        val tvTitle1 = findViewById<TextView>(R.id.tvTitle1)
        val tvTitle2 = findViewById<TextView>(R.id.tvTitle2)
        val tvTitle3 = findViewById<TextView>(R.id.tvTitle3)
        val tvTitle4 = findViewById<TextView>(R.id.tvTitle4)
        val tvTitle5 = findViewById<TextView>(R.id.tvTitle5)
        val tvTitle6 = findViewById<TextView>(R.id.tvTitle6)
        tvTitle1.text = "양배추"
        tvTitle2.text = "토마토"
        tvTitle3.text = "적양파"
        tvTitle4.text = "당근"
        tvTitle5.text = "옥수수"
        tvTitle6.text = "가지"

        var tvNum1 = findViewById<TextView>(R.id.tvNum1)
        var tvNum2 = findViewById<TextView>(R.id.tvNum2)
        var tvNum3 = findViewById<TextView>(R.id.tvNum3)
        var tvNum4 = findViewById<TextView>(R.id.tvNum4)
        var tvNum5 = findViewById<TextView>(R.id.tvNum5)
        var tvNum6 = findViewById<TextView>(R.id.tvNum6)
        var num1 = 0
        var num2 = 0
        var num3 = 0
        var num4 = 0
        var num5 = 0
        var num6 = 0

        val btnPlus1 = findViewById<Button>(R.id.btnPlus1)
        val btnPlus2 = findViewById<Button>(R.id.btnPlus2)
        val btnPlus3 = findViewById<Button>(R.id.btnPlus3)
        val btnPlus4 = findViewById<Button>(R.id.btnPlus4)
        val btnPlus5 = findViewById<Button>(R.id.btnPlus5)
        val btnPlus6 = findViewById<Button>(R.id.btnPlus6)

        val btnMinus1 = findViewById<Button>(R.id.btnMinus1)
        val btnMinus2 = findViewById<Button>(R.id.btnMinus2)
        val btnMinus3 = findViewById<Button>(R.id.btnMinus3)
        val btnMinus4 = findViewById<Button>(R.id.btnMinus4)
        val btnMinus5 = findViewById<Button>(R.id.btnMinus5)
        val btnMinus6 = findViewById<Button>(R.id.btnMinus6)


        btnPlus1.setOnClickListener {
            tvNum1.text = (++num1).toString()
        }
        btnMinus1.setOnClickListener {
            if(num1>0){
                tvNum1.text = (--num1).toString()
            }
        }

        btnPlus2.setOnClickListener {
            tvNum2.text = (++num2).toString()
        }
        btnMinus2.setOnClickListener {
            if(num2>0){
                tvNum2.text = (--num2).toString()
            }
        }

        btnPlus3.setOnClickListener {
            tvNum3.text = (++num3).toString()
        }
        btnMinus3.setOnClickListener {
            if(num3>0){
                tvNum3.text = (--num3).toString()
            }
        }

        btnPlus4.setOnClickListener {
            tvNum4.text = (++num4).toString()
        }
        btnMinus4.setOnClickListener {
            if(num4>0){
                tvNum4.text = (--num4).toString()
            }
        }

        btnPlus5.setOnClickListener {
            tvNum5.text = (++num5).toString()
        }
        btnMinus5.setOnClickListener {
            if(num5>0){
                tvNum5.text = (--num5).toString()
            }
        }

        btnPlus6.setOnClickListener {
            tvNum6.text = (++num6).toString()
        }
        btnMinus6.setOnClickListener {
            if(num6>0){
                tvNum6.text = (--num6).toString()
            }
        }


        val spf = this.getSharedPreferences(
            "loginInfo",
            Context.MODE_PRIVATE
        )
        val loginId = spf?.getString("loginId", "null") as String


        btnSeed.setOnClickListener {
            var orderString =""
            if(num1>0){

                orderString = "${tvTitle1.text} ${num1}개"
            }
            if(num2>0){
                orderString = "${orderString} ${tvTitle2.text} ${num2}개"
            }
            if(num3>0){
                orderString = "${orderString} ${tvTitle3.text} ${num3}개"
            }
            if(num4>0){
                orderString = "${orderString} ${tvTitle4.text} ${num4}개"
            }
            if(num5>0){
                orderString = "${orderString} ${tvTitle5.text} ${num5}개"
            }
            if(num6>0){
                orderString = "${orderString} ${tvTitle6.text} ${num6}개"
            }

            orderString =  "${loginId}님이 ${orderString}를 주문하셨습니다"


            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("seedInfo",orderString)
            intent.putExtra("firstSeed","ok")
            startActivity(intent)

            finish()

//            supportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fl,ChatFragment)
//                .commit()
        }










    }
}