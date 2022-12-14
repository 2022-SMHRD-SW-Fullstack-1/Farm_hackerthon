package com.example.ex221210

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class FarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm)

        var farm_name = intent.getStringExtra("farm")

        var tv = findViewById<TextView>(R.id.tv)

        tv.text = farm_name

    }
}