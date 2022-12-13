package com.example.ex221210.sns

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ex221210.R

//Fragment3에 있는 boardRv에 적용될 Adpater
class SnsAdapter(var context: Context, var boardList: ArrayList<SnsVO>): RecyclerView.Adapter<SnsAdapter.ViewHolder>() {

    // 리스너 커스텀
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    // 객체 저장 변수 선언
    lateinit var mOnItemClickListener: OnItemClickListener
    //객체 전달 메서드
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val tvMain : TextView
        val imgMain : ImageView
        val imgHeart : ImageView
        val imgMark : ImageView
        val imgTalk : ImageView
        val tvTime : TextView


        init {
            tvMain = itemView.findViewById(R.id.tvMain)
            imgMain = itemView.findViewById(R.id.imgMain)
            imgHeart = itemView.findViewById(R.id.imgHeart)
            imgMark = itemView.findViewById(R.id.imgMark)
            imgTalk = itemView.findViewById(R.id.imgTalk)
            tvTime = itemView.findViewById(R.id.tvTime)

            itemView.setOnClickListener {
                val position = adapterPosition
                if(position !=RecyclerView.NO_POSITION){
                    mOnItemClickListener.onItemClick(itemView, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.sns_template,null)

        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMain.text = boardList[position].title
        holder.imgMain.setImageResource(R.drawable.garden)
        holder.imgHeart.setImageResource(R.drawable.ic_favorite_border)
        holder.imgMark.setImageResource(R.drawable.bookmark_white)
        holder.imgTalk.setImageResource(R.drawable.ic_chat_black)
        holder.tvTime.text = boardList[position].time


    }

    override fun getItemCount(): Int {
        return boardList.size// 항목의 갯수
    }

}