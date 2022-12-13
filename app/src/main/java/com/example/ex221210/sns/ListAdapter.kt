package com.example.ex221210.sns

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ex221210.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListAdapter(var context: Context, var data: ArrayList<ListVO>
                  , var keyData : ArrayList<String>, var bookmarkList : ArrayList<String>)
    : RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    val database = Firebase.database
    val auth: FirebaseAuth = Firebase.auth

    //BaseAdapter --> 일반 ListView
    //RecyclerView --> RecyclerVeiewAdapter 상속

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvMain: TextView
        val imgMain : ImageView
        val imgmark : ImageView
        init {
            tvMain = itemView.findViewById(R.id.tvMain)
            imgMain= itemView.findViewById(R.id.imgMain)
            imgmark = itemView.findViewById(R.id.imgMark)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflate: xml 코드를 눈에보이는 view 객체로 바꿔서 ViewHolder로 보내주는 역할
        val layoutInflater = LayoutInflater.from(context)
        // getSystemService <--  하드웨어가 가지고 있는 많은 센서 서비스들이 담겨있음

        val view = layoutInflater.inflate(R.layout.sns_template, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMain.text = data.get(position).title
//        holder.imgContent.setImageResource(data[position].imgId)
        Glide.with(context)
            .load(data[position].imgId)
            .into(holder.imgMain)

        //imgView를 클릭했을 때
        //url값을 가지고 WebViewActivity로 넘어간다!!!
        holder.imgMain.setOnClickListener {

            // adapter가 설계된 클래스에서 this는 사용할 수 없음(activity에서 사용)
            val intent = Intent(context,WebViewActivity::class.java)
            intent.putExtra("url",data[position].url)
            context.startActivity(intent)

//            val spf = context.getSharedPreferences(
//                "url",
//                Context.MODE_PRIVATE
//            )
//            var editor = spf.edit()
//            editor.putString("url",data.get(position).url)
//            editor.commit()

        }

        // 클릭했을 때 색깔을 바꾸면 기존에 있던 북마크는 색이 안칠해져있음
        // adapter가 실행되는 순간 북마크로 있던 데이터들은 바로 색칠될 수 있게
        // imgBookmark.setOnClickListener 바깥으로 북마크 색 설정하는 조건식을 달아줌
        if(bookmarkList.contains(keyData[position])){
            holder.imgmark.setImageResource(R.drawable.bookmark_color)
        }else{
            holder.imgmark.setImageResource(R.drawable.bookmark_white)
        }

        // 북마크 모양의 이미지를 클릭했을 때 해당 게시물의 uid값이 bookmarklist경로로 들어가야 함
        holder.imgmark.setOnClickListener {
            // Firebase에 있는 bookmarklist 경로로 접근
            val bookmarkRef = database.getReference("bookmarklist")
            bookmarkRef.child(auth.currentUser!!.uid).child(keyData[position]).setValue("good")

            // 이미 저장이 되어있는 게시물인지 아닌지 bookmarkList에 해당 게시물이 포함되어 있는지
            if(bookmarkList.contains(keyData[position])){
                // 북마크를 취소
                // database에서 해당 keyData를 삭제
                bookmarkRef.child(auth.currentUser!!.uid).child(keyData[position]).removeValue()
                // imgbookmark를 하얗게 만들자
                // holder.imgBookmark.setImageResource(R.drawable.bookmark_white)
            }else{
                // 북마크를 추가
                // database에 해당 keyData를 추가
                // 잘 들어갔는지 setValue를 통해 확인
                bookmarkRef.child(auth.currentUser!!.uid).child(keyData[position]).setValue("good")
                // imgbookmark를 까맣게 만들자
                // holder.imgBookmark.setImageResource(R.drawable.bookmark_color)
            }

        }

    }

    override fun getItemCount(): Int {
        return  data.size
    }

}