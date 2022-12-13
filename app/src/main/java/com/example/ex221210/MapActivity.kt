package com.example.ex221210

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        supportActionBar?.hide()
        btnToLogin()
    }

    private fun btnToLogin() {
        btn_signIn.setOnClickListener {
            startActivity(Intent(this@MapActivity, LoginActivity::class.java))
        }
    }
}