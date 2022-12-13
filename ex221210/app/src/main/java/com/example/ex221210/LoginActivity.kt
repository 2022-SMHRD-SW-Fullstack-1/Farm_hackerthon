package com.example.ex221210

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        auth = Firebase.auth

        toRegister()
    }
    private fun toRegister(){
        tv_register.setOnClickListener {
            startActivity(Intent(this, JoinActivity::class.java))
        }

        val url = "https://handfarm-f4e9c-default-rtdb.firebaseio.com/"
        val db = Firebase.database(url)

        val sharedPreferences = getSharedPreferences(
            "autoLogin",
            Context.MODE_PRIVATE
        )
        val loginId = sharedPreferences.getString("loginId","")
        val loginPw = sharedPreferences.getString("loginPw","")

        val spf = getSharedPreferences(
            "loginInfo",
            Context.MODE_PRIVATE
        )
        val loginName = spf.getString("loginId","null")


        // FirebaseAuth 초기화
        auth = Firebase.auth

        val etLoginEmail: EditText = findViewById(R.id.et_email)
        val etLoginPw: EditText = findViewById(R.id.et_password)
        val btnLoginLogin : Button = findViewById(R.id.btn_login)


        etLoginEmail.setText(loginId)
        etLoginPw.setText(loginPw)


        btnLoginLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val pw = etLoginPw.text.toString()

            auth.signInWithEmailAndPassword(email,pw)
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful == true){
                        // 로그인에 성공
                        Toast.makeText(this,"성공", Toast.LENGTH_SHORT).show()

                        // 자동로그인 기능
                        val editor = sharedPreferences.edit()
                        editor.putString("loginId",email)
                        editor.putString("loginPw",pw)
                        editor.commit()
                        //

                        // 채팅용 SharedPreferences (내 아이디만 가지고 있는 가방)
                        val editorSpf = spf.edit()
                        editorSpf.putString("loginId",email)
                        editorSpf.commit()


                        val intent = Intent(this,SJMainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        // 로그인에 실패
                        Toast.makeText(this,"실패", Toast.LENGTH_SHORT).show()
                    }
                }


            Toast.makeText(this,"이메일 : ${email} / 비밀번호 : ${pw}",
                Toast.LENGTH_SHORT).show()

        }

        //let do login now.


    }

}