package com.example.wordle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView 사용 6단계
        // 1. Container 결정
        val rvGame = findViewById<RecyclerView>(R.id.rvGame)

        val answer = "apple"

        // 2. Template 결정
        // game_list.xml

        // 3. Item 설정
        val gameList = ArrayList<GameVO>()
        gameList.add(GameVO("","","","",""))
        gameList.add(GameVO("","","","",""))
        gameList.add(GameVO("","","","",""))
        gameList.add(GameVO("","","","",""))
        gameList.add(GameVO("","","","",""))
        gameList.add(GameVO("","","","",""))


        // 4. Adapter 결정
        var adapter = GameAdapter(this,gameList, answer)

        // 5. Container 에 Adapter 부착
        rvGame.adapter = adapter
        rvGame.layoutManager = LinearLayoutManager(this)


    }
}