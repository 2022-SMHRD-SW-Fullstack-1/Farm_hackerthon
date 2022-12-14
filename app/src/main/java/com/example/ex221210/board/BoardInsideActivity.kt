package com.example.ex221210.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ex221210.R
import com.example.ex221210.utils.FBAuth
import com.example.ex221210.utils.FBdatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {

    lateinit var imgIn : ImageView

    override fun onResume() {
        super.onResume()
        setContentView(R.layout.activity_board_inside)

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        // id값
        val tvInTitle = findViewById<TextView>(R.id.tvInTitle)
        val tvInTime = findViewById<TextView>(R.id.tvInTime)
        val tvInContent = findViewById<TextView>(R.id.tvInContent)
        imgIn = findViewById(R.id.imgIn)

        val content = intent.getStringExtra("content")
        val time = intent.getStringExtra("time")
        val title = intent.getStringExtra("title")
        // 이미지를 Firebase에서 꺼내올 때 사용
        val key = intent.getStringExtra("key")
        val uid = intent.getStringExtra("uid")

        tvInContent.text = content.toString()
        tvInTime.text = time.toString()
        tvInTitle.text = title.toString()

        // 게시물의 uid값으로 이름을 지정
        getImageData(key.toString())

        val writerUid = FBAuth.getUid()

        // 게시물의 uid와 현재 로그인한 사용자의 uid를 비교해 수정, 삭제버튼 활성화
        btnEdit.isEnabled = writerUid.equals(uid)
        btnDelete.isEnabled = writerUid.equals(uid)

        // 게시물 수정
        btnEdit.setOnClickListener {
            val intent = Intent(this@BoardInsideActivity, BoardEditActivity::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("key", key)
            intent.putExtra("title", title)
            intent.putExtra("content", content)
            startActivity(intent)
        }

        // 게시물 삭제
        btnDelete.setOnClickListener {
            deleteBoardData(key!!)
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    // 게시글 삭제하는 함수
    private fun deleteBoardData(key: String){
        // 게시글 삭제
        FBdatabase.getBoardRef().child(key).removeValue()
        // 삭제 확인 메시지
        Toast.makeText(this, "게시글이 삭제되었습니다", Toast.LENGTH_SHORT).show()

        // 게시글 수정 액티비티 종료
        finish()
    }




    // Image를 가져오는 함수 만들기
    fun getImageData(key : String){
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imgIn) // 지역변수
            }
        }
    }


}