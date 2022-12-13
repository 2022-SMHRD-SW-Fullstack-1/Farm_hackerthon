package com.example.ex221210.sns

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ex221210.R

class SnsBookmarkAdapter(var context: Context, var data : ArrayList<SnsVO>,
                      var keyData : ArrayList<String>, var bookmarkList: ArrayList<String>)
    : RecyclerView.Adapter<SnsBookmarkAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgMark: ImageView
        val imgContent: ImageView
        val tvTitle: TextView

        init {
            imgMark = itemView.findViewById(R.id.imgMark)
            imgContent = itemView.findViewById(R.id.imgMain)
            tvTitle = itemView.findViewById(R.id.tvMain)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // list_template.xml을 눈에 보이는 View객체로 바꾼다
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.sns_template, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 북마크데이터에 포함되어있는지를 판단해서
        // view + data랑 합쳐주는 작업을 진행
        if (bookmarkList.contains(keyData[position])) {
            holder.tvTitle.text = data[position].title
            Glide.with(context)
                .load(data[position].title)
                .into(holder.imgContent)
            // Glide : 웹에 있는 이미지를 가져와서 세팅
            // imgId ---> 이미지 주소값
        }
        if (bookmarkList.contains(keyData[position])) {
            holder.imgMark.setImageResource(R.drawable.bookmark_color)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}