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

class SJLogin : AppCompatActivity() {


    // FirebaseAuth 선언
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sjlogin)

        // Write a message to the database
        val url = "https://handfarm-f4e9c-default-rtdb.firebaseio.com/"
        val db = Firebase.database(url)
        val myRef = db.getReference("message")
        myRef.setValue("오잉")

        // 무언가 값이 추가된 이벤트를 감지하는 Listener
        myRef.addValueEventListener(object:ValueEventListener{
            // 데이터가 변하면 동작하는 로직
            override fun onDataChange(snapshot: DataSnapshot) {
                val message = snapshot.getValue<String>()

//                tvMsg.setText(message)

            }
            // 데이터가 삭제되면 동작하는 로직
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        ///////////////////////////////////////////////////

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

        val etLoginEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etLoginPw = findViewById<EditText>(R.id.etLoginPw)
        val btnLoginLogin = findViewById<Button>(R.id.btnLoginLogin)

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

    }
}