package com.example.ex221210.sns

import android.content.Context
import android.media.Image
import android.util.Log
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

class ListAdapter(var context: Context, var data: ArrayList<ListVO>,
                     var keyData: ArrayList<String>, var likeList : ArrayList<String>
                     , var markList : ArrayList<String>)
    : RecyclerView.Adapter<ListAdapter.ViewHolder>(){
        val database = Firebase.database
        val auth : FirebaseAuth = Firebase.auth
        var heartCt = 1
        var bookmarkCt = 1

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            val tvContent : TextView
            val tvTitle : TextView
            val imgMain : ImageView
            val imgHeart : ImageView
            val tvTime : TextView
            val imgMark : ImageView

            init {
                tvContent = itemView.findViewById(R.id.tvContent)
                tvTitle = itemView.findViewById(R.id.tvTitle)
                imgMain = itemView.findViewById(R.id.imgMain)
                imgHeart = itemView.findViewById(R.id.imgHeart)
                tvTime = itemView.findViewById(R.id.tvTime)
                imgMark = itemView.findViewById(R.id.imgMark)
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
        holder.tvTitle.text = data.get(position).title
        holder.tvTime.text = data.get(position).Time
        holder.tvContent.text = data.get(position).content
        Glide.with(context)
            .load(data[position].imgId)
            .into(holder.imgMain)

        // 좋아요 색칠
//        if(likeList.contains(keyData[position])){
//            holder.imgHeart.setImageResource(R.drawable.ic_favorite)
//        }else{
//            holder.imgHeart.setImageResource(R.drawable.ic_favorite_border)
//        }
        // 하트 모양의 이미지를 클릭했을 때 해당 게시물의 uid값이 likelist경로로 들어가야 함
        holder.imgHeart.setOnClickListener {
            heartCt++
            if(heartCt%2==0){
                holder.imgHeart.setImageResource(R.drawable.ic_favorite)
            }else{
                holder.imgHeart.setImageResource(R.drawable.ic_favorite_border)
            }
            // Firebase에 있는 likeList 경로로 접근
            val likeRef = database.getReference("likeList")
            val what = likeRef.child(auth.currentUser!!.uid).child(keyData[position])
            likeRef.child(auth.currentUser!!.uid).child(keyData[position]).setValue("like")
            Log.d("likelike","$what")
            // 이미 저장이 되어있는 게시물인지 아닌지 bookmarkList에 해당 게시물이 포함되어 있는지
            if(likeList.contains(keyData[position])){
                // 좋아요를 취소
                // database에서 해당 KeyData를 삭제
                likeRef.child(auth.currentUser!!.uid).child(keyData[position]).removeValue()
            }else{
                // 좋아요 추가
                // database에서 해당 keyData 추가
                // 잘 들어갔는지 setValue 통해 확인
                likeRef.child(auth.currentUser!!.uid).child(keyData[position]).setValue("like")
            }

        }

        holder.imgMark.setOnClickListener {
            bookmarkCt++
            if(bookmarkCt%2==0){
                holder.imgMark.setImageResource(R.drawable.bookmark_color)
            }else{
                holder.imgMark.setImageResource(R.drawable.bookmark_white)
            }
            // Firebase에 있는 likeList 경로로 접근
            val markRef = database.getReference("bookmarklist")
            val what = markRef.child(auth.currentUser!!.uid).child(keyData[position])
            markRef.child(auth.currentUser!!.uid).child(keyData[position]).setValue("like")
            // 이미 저장이 되어있는 게시물인지 아닌지 bookmarkList에 해당 게시물이 포함되어 있는지
            if(markList.contains(keyData[position])){
                // 좋아요를 취소
                // database에서 해당 KeyData를 삭제
                markRef.child(auth.currentUser!!.uid).child(keyData[position]).removeValue()
            }else{
                // 좋아요 추가
                // database에서 해당 keyData 추가
                // 잘 들어갔는지 setValue 통해 확인
                markRef.child(auth.currentUser!!.uid).child(keyData[position]).setValue("like")
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
