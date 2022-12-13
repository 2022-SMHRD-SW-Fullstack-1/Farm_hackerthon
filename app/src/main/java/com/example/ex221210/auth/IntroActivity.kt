package com.example.ex221210.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.ex221210.JoinActivity
import com.example.ex221210.LoginActivity
import com.example.ex221210.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        auth = Firebase.auth

        val btnIntroLogin = findViewById<Button>(R.id.btnIntroLogin)
        val btnIntroJoin = findViewById<Button>(R.id.btnIntroJoin)

        // login -> LoginActivity로 이동
        btnIntroLogin.setOnClickListener {
            val intent = Intent(this@IntroActivity,LoginActivity::class.java)
            startActivity(intent)
        }
        // join -> JoinActivity로 이동
        btnIntroJoin.setOnClickListener {
            val intent = Intent(this@IntroActivity, JoinActivity::class.java)
            startActivity(intent)
        }
    }
}