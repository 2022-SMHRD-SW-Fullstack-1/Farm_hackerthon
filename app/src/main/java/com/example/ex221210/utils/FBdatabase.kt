package com.example.ex221210.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBdatabase {

    // realtime database 사용은 이 클래스를 통해 진행
    companion object{
        val database = Firebase.database

        // FBdatabase.getBoardRef()
        fun getBoardRef() : DatabaseReference{
            return database.getReference("board")
        }
        fun getSnsRef() : DatabaseReference{
            return database.getReference("sns")
        }
        fun getContentRef() : DatabaseReference{
            return database.getReference("content")
        }
        fun getBookmarkRef() : DatabaseReference{
            return database.getReference("bookmarklist")
        }
        // database 인스턴스를 클래스마다 생성할 필요가 없음..??
    }
}