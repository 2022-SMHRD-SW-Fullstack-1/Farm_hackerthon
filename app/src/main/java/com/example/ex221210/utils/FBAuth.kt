package com.example.ex221210.utils


import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {

    companion object{
        lateinit var auth : FirebaseAuth

        fun getUid() :String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser!!.uid
        }

        // 현재 시간을 가져오는 함수
        fun getTime() : String{
            // Calendar 객체는 getInstance()메서드로 객체를 생성
            val currentTime = Calendar.getInstance().time
            // 시간을 나타낼 형식, 어느 위치의 시간을 가져올건지 설정
            // "yyyy.mm.dd HH:mm:ss"
            val time = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentTime)
            return time
        }
    }
}