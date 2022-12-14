package com.example.ex221210.comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.ex221210.R
import com.example.ex221210.utils.FBAuth
import com.example.ex221210.utils.FBdatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.comment_lv_item.*

class CommentEditActivity : AppCompatActivity() {

    lateinit var key : String
    lateinit var commentKey : String
    lateinit var commentMainArea : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_edit)

        val commentEdit = findViewById<TextView>(R.id.commentEdit) //수정하기
        val backBtn = findViewById<ImageView>(R.id.backBtn) //뒤로 가기
        commentMainArea = findViewById(R.id.commentMainArea) // 댓글 수정
        val commentEditBtn = findViewById<Button>(R.id.commentEditBtn) // 수정하기 버튼
        val commentDeleteBtn = findViewById<Button>(R.id.commentDeleteBtn) // 삭제하기 버튼

        // 게시글의 키값을 가져옴
        key = intent.getStringExtra("key").toString()

        // BoardInsideActivity에서 댓글의 키값을 받아옴
        commentKey = intent.getStringExtra("commentKey").toString()

        // 댓글 키 값을 바탕으로 댓글 하나의 정보를 가져옴
        getCommentData(key, commentKey)

        // 뒤로 가기 버튼 -> CommentEditActivity 종료
        backBtn.setOnClickListener {
            finish()
        }

        // 댓글 수정
        commentEditBtn.setOnClickListener {
            editCommentData(key, commentKey)
        }
        // 댓글 삭제
        commentDeleteBtn.setOnClickListener {
            deleteCommentData(key, commentKey)
        }

    } // onCreate 바깥

    // 댓글삭제기능
    fun deleteCommentData(key: String, commentKey: String){
        // 댓글 삭제
        FBdatabase.commentRef().child(key).child(commentKey).removeValue()
        // 삭제 확인 메시지
        Toast.makeText(this, "댓글이 삭제되었습니다", Toast.LENGTH_SHORT).show()
        // CommentEditActivity 종료
        finish()
    }

    fun editCommentData(key: String, commentKey: String){
        // 수정한 값으로 업데이트
        FBdatabase.commentRef().child(key).child(commentKey).setValue(CommentVO(
            commentMainArea.text.toString(),
            FBAuth.getUid(),
            FBAuth.getTime()
        ))
        // 수정 확인 메시지
        Toast.makeText(this, "댓글이 수정되었습니다", Toast.LENGTH_SHORT).show()
        // 댓글 수정 액티비티 종료
        finish()
    }

    // 댓글 하나의 정보를 가져옴
    fun getCommentData(key: String, commentKey: String){

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener{

            // 데이터 스냅샷
            override fun onDataChange(snapshot: DataSnapshot) {
                // getCommentListData()와 달리 반복문 아님 -> 단일 아이템
                // 예외 처리
                try {
                    // 데이터 스냅샷 내 데이터모델 형식으로 저장된 아이템(=게시글)
                    val item = snapshot.getValue(CommentVO::class.java)

                    // 본문 해당 영역에 넣음(작성자 및 시간은 직접 수정 X)
                    commentMainArea.setText(item?.content)
                }catch (e: java.lang.Exception){
                    Log.d("댓글 가져오기", "오류 발생")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
    }


}