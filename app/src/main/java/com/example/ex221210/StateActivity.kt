package com.example.ex221210

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ex221210.LoginActivity
import com.example.ex221210.R
import com.example.ex221210.databinding.ActivityStateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StateActivity : AppCompatActivity() {
    lateinit var binding: ActivityStateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fun logout() {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        binding.tvSettingLogout.setOnClickListener {
            logout()
        }
        binding.tvSettingDelete.setOnClickListener {
            FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "탈퇴가 완료되었습니다", Toast.LENGTH_LONG).show()
                    val editor = getSharedPreferences("autoLogin", Context.MODE_PRIVATE).edit()
                    editor.putString("loginId", "")
                    editor.putString("loginPw", "")
                    editor.commit()
                    logout()
                }else{
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.tvSettingEdit.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }
}