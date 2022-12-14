package com.example.ex221210.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ex221210.LoginActivity
import com.example.ex221210.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()
        btnToLogin()
    }

    private fun btnToLogin() {
        cl.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}