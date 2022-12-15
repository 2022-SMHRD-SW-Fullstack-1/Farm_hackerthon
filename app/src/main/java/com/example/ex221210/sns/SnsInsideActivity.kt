package com.example.ex221210.sns

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ex221210.R
import com.example.ex221210.comment.CommentEditActivity
import com.example.ex221210.comment.CommentLVAdapter
import com.example.ex221210.comment.CommentVO
import com.example.ex221210.utils.FBAuth
import com.example.ex221210.utils.FBdatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_board_inside.*

class SnsInsideActivity : AppCompatActivity() {

    lateinit var imgIn : ImageView
    // 댓글 목록
    private val commentList = mutableListOf<CommentVO>()
    // 댓글의 키 목록
    private val commentKeyList = mutableListOf<String>()
    lateinit var commentLVAdapter: CommentLVAdapter
    lateinit var tvCount : TextView
    lateinit var btnDelete : Button
    lateinit var imgCom : ImageView
    lateinit var etWriteCom : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_inside)

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)
        // id값
        val tvInTitle = findViewById<TextView>(R.id.tvInTitle)
        val tvInTime = findViewById<TextView>(R.id.tvInTime)
        val tvInContent = findViewById<TextView>(R.id.tvInContent)
        imgIn = findViewById(R.id.imgIn)
        val lv = findViewById<ListView>(R.id.lvComment)
        tvCount = findViewById(R.id.tvCount)
        imgCom = findViewById(R.id.imgCom)
        etWriteCom = findViewById(R.id.etWriteCom)

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
        btnEdit.isEnabled = writerUid.equals(FBAuth.getUid())
        btnDelete.isEnabled = writerUid.equals(FBAuth.getUid())

        // 리스트뷰 어댑터 연결 (댓글 목록)
        commentLVAdapter = CommentLVAdapter(commentList)
        lv.adapter = commentLVAdapter

        // 댓글 목록 (리스트 뷰)
        lv.setOnTouchListener(object : View.OnTouchListener{
            // 리스트뷰를 터치했을 때
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                lvComment.requestDisallowInterceptTouchEvent(true)
                return false
            }
        })

        // 파이어베이스의 댓글 키를 기반으로 댓글 데이터(=본문+uid+시간) 받아옴
        lv.setOnItemClickListener { parent, view, position, id ->

            // 댓글 작성자의 uid와 현재 사용자의 uid가 동일하면
            if(commentList[position].uid == FBAuth.getUid()) {

                // 명시적 인텐트 -> 다른 액티비티 호출
                val intent = Intent(baseContext, CommentEditActivity::class.java)

                // 댓글수정 액티비티로 키 값 전달
                intent.putExtra("key", key)

                // 댓글수정 액티비티로 댓글의 키 값 전달
                intent.putExtra("commentKey", commentKeyList[position])

                // 댓글수정 액티비티 시작
                startActivity(intent)
            }

        }

        // 게시판 fragment에서 게시글의 키 값을 받아옴 (key)
        getCommentListData(key!!)

        // 게시물 수정
//        btnEdit.setOnClickListener {
//            val intent = Intent(this@SnsInsideActivity, SnsEditActivity::class.java)
//            intent.putExtra("uid", uid)
//            intent.putExtra("key", key)
//            intent.putExtra("title", title)
//            intent.putExtra("content", content)
//
//            startActivity(intent)
//            finish()
//
//        }

        // 게시물 삭제
        btnDelete.setOnClickListener {
            deleteBoardData(key!!)
        }

        imgCom.setOnClickListener{
            setComment(key)
        }

    } // Oncreate 바깥

    private fun getCommentListData(key: String){
        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener{

            //데이터 스냅샷
            override fun onDataChange(snapshot: DataSnapshot) {
                // 댓글 목록 비움 : 저장/삭제 마다 데이터 누적돼 게시글 중복으로 저장되는 것 방지
                commentList.clear()

                // 데이터 스냅샷 내 데이터모델 형식으로 저장된
                for(Model in snapshot.children){
                    val item = Model.getValue(CommentVO::class.java)
                    commentList.add(item!!)
                    commentKeyList.add(Model.key.toString())
                }

                // 댓글 키 목록을 출력
                commentKeyList

                // 댓글 목록도 출력
                commentList

                // 댓글 개수 출력
                tvCount.text = commentList.count().toString()
                // 동기화(새로고침) -> 리스트 크기 및 아이템 변화를 어댑터에 알림
                commentLVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("댓글 리스트", "출력 오류")
            }
        }
        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FBdatabase.commentRef().child(key).addValueEventListener(postListener)
    }

    // 작성한 댓글을 등록
    private fun setComment(key: String){
        val content = etWriteCom.text.toString()
        val uid = FBAuth.getUid()
        val time = FBAuth.getTime()

        FBdatabase.commentRef()
            .child(key)
            .push()
            .setValue(CommentVO(content, uid, time))

        // 등록 확인 메시지 띄움
        Toast.makeText(this, "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show()

        // 댓글 입력란 비움
        etWriteCom.text = null

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