package com.example.ex221210

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SJMainActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sjmain)


            auth = Firebase.auth

            // 로그아웃 기능
//            imgLogout.setOnClickListener{
//
//                auth.signOut()
//
//                // 로그아웃하고나면 IntroActivity로 이동
//                val intent = Intent(this,IntroActivity::class.java)
//                // 이동하기 전에 이전에 쌓여있는 Activity를 모두 날려주기
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//                finish()
//            }

            supportFragmentManager.beginTransaction().replace(
                R.id.fl,
                SJChatFragment()
            ).commit()


    }
}