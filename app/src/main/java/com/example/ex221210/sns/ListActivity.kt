package com.example.ex221210.sns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ex221210.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListActivity : AppCompatActivity() {

    lateinit var adapter: ListAdapter
    var keyData = ArrayList<String>()
    lateinit var likeRef :DatabaseReference
    lateinit var markRef : DatabaseReference
    var likeList = ArrayList<String>()
    var markList = ArrayList<String>()

    var auth : FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // realTime Database에 필요한 객체 선언
        val database = Firebase.database
        // database에 어떤 것을 참조할건지
        val allContent = database.getReference("content") // Fragment2에서 전체보기를 눌렀을 때 가져올 데이터가 담기는 곳
        likeRef = database.getReference("likeList")
        markRef = database.getReference("marklist")
        // content : 전체 게시물들이 저장될 경로
        // uid(push() 타임스탬프 : 게시물을 구분할 수 있는 고유한 값)
        //          imgId, content, title, time
        // uid2
        //          imgId, content, title, time

        val rvSns = findViewById<RecyclerView>(R.id.rvSns)

        // TextView
        // RecyclerView ---> Adapter, data(VO), template(xml)

        // Adapter ---> ListAdapter
        // data(VO) ---> ListVO
        // template ---> layout폴더에 sns_template.xml

        // 이미지의 id (Int), title(String) ---> VO로 묶어야할 데이터
        // 3 ~ 4개 정도 임의로 만들어 놓기
        val data = ArrayList<ListVO>()
        adapter = ListAdapter(this, data, keyData, likeList, markList)
        // FireBase에서 데이터를 받아오는 Listener
        val postListener = object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(model in snapshot.children){
                    val item = model.getValue(ListVO::class.java)
                    // model에는 여러가지 게시물이 담겨있고
                    // 1개에 대한 게시물에 접근하기 위해 value를 ListVO
                    if(item != null){
                        data.add(item)
                    }
                    keyData.add(model.key.toString())
                }
                // 데이터를 받아오는 속도가 adapter가 실행되는 속도보다 느림
                // 그래서 데이터를 받아온 직후에 새로고침 필요함
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        allContent.addValueEventListener(postListener)
        getLikeData()

        data.add(ListVO("2022/10/23","https://previews.123rf.com/images/abeya/abeya1506/abeya150600001/40859357-%EC%94%A8%EC%95%97%EC%9D%84-%EB%BF%8C%EB%A6%B4-%EC%A4%80%EB%B9%84%EA%B0%80-%EB%90%9C-%EC%95%84%EC%8A%A4%ED%8C%8C%EB%9D%BC%EA%B1%B0%EC%8A%A4-%EB%B0%AD-%EC%A4%80%EB%B9%84.jpg","토마토 1주차","1일차 씨앗을 심었습니다"))



        for(i in 0 until data.size){
            allContent.push().setValue(
                data[i]
            )
        }

        // Adapter : recyclerView를 상속받게 만들어주기
        // ViewHolder, OncreateView, OnbindingView, getItemCount


        // ListActivity에서 내가 만든 ListAdapter를 rv에 적용하기!
        // 단 GridLayoutManager를 사용해서 두 줄로 쌓이게 만들기
        rvSns.adapter = adapter
        rvSns.layoutManager = GridLayoutManager(this,1)
    }
    // likeList에 저장된 데이터 가져오는 함수
    fun getLikeData(){
        val postListener2 = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                likeList.clear()
                for(model in snapshot.children){
                    likeList.add(model.key.toString())
                }
                adapter.notifyDataSetChanged()

                markList.clear()
                for(model in snapshot.children){
                    markList.add(model.key.toString())
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        likeRef.child(auth.currentUser!!.uid).addValueEventListener(postListener2)
    }



}