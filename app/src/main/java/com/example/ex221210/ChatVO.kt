package com.example.ex221210

data class ChatVO(val name : String, val msg : String) {

    // 반드시!! FireBase에 RealTime DataBase 사용하기 위해서
    // 무조건!! 기본생성자 만들어 주기!!
    constructor() : this("","")

}