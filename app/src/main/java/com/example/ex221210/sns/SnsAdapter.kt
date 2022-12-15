package com.example.ex221210.sns

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ex221210.R
import com.example.ex221210.utils.FBAuth.Companion.getUid
import com.example.ex221210.utils.FBdatabase.Companion.getBookmarkRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

//Fragment3에 있는 boardRv에 적용될 Adpater
class SnsAdapter(var context: Context, var boardList: ArrayList<SnsVO>, var keyData : ArrayList<String>,var bookmarkList: ArrayList<String>): RecyclerView.Adapter<SnsAdapter.ViewHolder>() {

    val database = Firebase.database
    val auth: FirebaseAuth = Firebase.auth
    var bookmarkCk = false
    var likeCk = false
    var likeCount = 1
    var MarkCount = 1
    // 리스너 커스텀
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    // 객체 저장 변수 선언
    lateinit var mOnItemClickListener: OnItemClickListener

    //객체 전달 메서드
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvMain: TextView
        val imgMain: ImageView
        val imgHeart: ImageView
        val imgMark: ImageView
        val imgTalk: ImageView
        val tvTime: TextView


        init {
            tvMain = itemView.findViewById(R.id.tvMain)
            imgMain = itemView.findViewById(R.id.imgMain)
            imgHeart = itemView.findViewById(R.id.imgHeart)
            imgMark = itemView.findViewById(R.id.imgMark)
            imgTalk = itemView.findViewById(R.id.imgTalk)
            tvTime = itemView.findViewById(R.id.tvTime)

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(itemView, position)
                }
            }

            imgHeart.setOnClickListener {
                likeCount++
                if(likeCount%2==0){
                    imgHeart.setImageResource(R.drawable.ic_favorite)
                }else{
                    imgHeart.setImageResource(R.drawable.ic_favorite_border)
                }
            }


        }


        // adapter가 설계된 클래스에서 this는 사용할 수 없음(activity에서 사용)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.sns_template, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // 클릭했을 때 색깔을 바꾸면 기존에 있던 북마크는 색이 안칠해져있음
        // adapter가 실행되는 순간 북마크로 있던 데이터들은 바로 색칠될 수 있게
        // imgBookmark.setOnClickListener 바깥으로 북마크 색 설정하는 조건식을 달아줌
//            if (bookmarkList.contains(keyData[position])) {
//                holder.imgMark.setImageResource(R.drawable.bookmark_color)
//            } else {
//                holder.imgMark.setImageResource(R.drawable.bookmark_white)
//            }


        Log.d("키데이터",keyData[position].toString())



        fun getBookmarkData(key: String?) {
            val bookmarkListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var value = snapshot.getValue<String>()

                    if(value == null){
                        Log.d("getBookmarkData","if실행")
                        bookmarkCk = false
                        holder.imgMark.setImageResource(R.drawable.bookmark_white)
                    }else{
                        bookmarkCk = true
                        holder.imgMark.setImageResource(R.drawable.bookmark_color)
                        Log.d("getBookmarkData","else실행")
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            }

            getBookmarkRef().child(getUid()).child(key!!).addValueEventListener(bookmarkListener)

        }

        getBookmarkData(keyData[position])


        // 북마크 모양의 이미지를 클릭했을 때 해당 게시물의 uid값이 bookmarklist경로로 들어가야 함
        holder.imgMark.setOnClickListener {
            // Firebase에 있는 bookmarklist 경로로 접근

            val bookmarkRef = database.getReference("bookmarklist")

            //bookmarkRef.child(auth.currentUser!!.uid).child(keyData[position]).setValue("good")

            Log.d("키데이터",keyData[position].toString())


            Log.d("키데이터 그 잡채",keyData[position])
//                // 이미 저장이 되어있는 게시물인지 아닌지 bookmarkList에 해당 게시물이 포함되어 있는지
//                if(bookmarkRef.child(auth.currentUser!!.uid).child(keyData[position]) != null){
//
//                        // 북마크를 취소
//                        // database에서 해당 keyData를 삭제
//                        bookmarkRef.child(auth.currentUser!!.uid).child(keyData[position]).removeValue()
//                        // imgbookmark를 하얗게 만들자
//                        holder.imgMark.setImageResource(R.drawable.bookmark_white)
//                        Log.d("북마크","북마크 취소 작동")
//                    }else{
//
//                    // 북마크를 추가
//                    // database에 해당 keyData를 추가
//                    // 잘 들어갔는지 setValue를 통해 확인
//                    bookmarkRef.child(auth.currentUser!!.uid).child(keyData[position]).setValue("good")
//                    // imgbookmark를 까맣게 만들자
//                    holder.imgMark.setImageResource(R.drawable.bookmark_color)
//                    Log.d("북마크","북마트 추가 작동")
//                }
//
//                holder.imgMark.setOnClickListener {


            if(bookmarkCk){
                holder.imgMark.setImageResource(R.drawable.bookmark_white)
                bookmarkRef.child(auth.currentUser!!.uid).child(keyData[position]).removeValue()
            } else{
                holder.imgMark.setImageResource(R.drawable.bookmark_color)
                bookmarkRef.child(auth.currentUser!!.uid).child(keyData[position]).setValue("bookmark")
            }


//                }

        }

    }




    override fun getItemCount(): Int {
        return boardList.size// 항목의 갯수
    }

}