package com.example.ex221210

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.zip.Inflater

class ChatAdapter(val context:Context, val chatList:ArrayList<ChatVO>, val loginId:String) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    inner class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){

        val imgChat : ImageView
        val tvChatOtherName : TextView
        val tvChatOtherMsg : TextView
        val tvChatMyMsg : TextView

        init {
            imgChat = itemView.findViewById(R.id.imgChat)
            tvChatOtherName = itemView.findViewById(R.id.tvChatOtherName)
            tvChatOtherMsg = itemView.findViewById(R.id.tvChatOtherMsg)
            tvChatMyMsg = itemView.findViewById(R.id.tvChatMyMsg)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        var view = layoutInflater.inflate(R.layout.sj_chat_list,null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val myName = chatList.get(position).name

        if(loginId == myName){
            // 내가 보낸 메세지
            // 노란 메세지만 보이도록 설정
            holder.imgChat.visibility = View.GONE
            holder.tvChatOtherName.visibility = View.GONE
            holder.tvChatOtherMsg.visibility = View.GONE

            holder.tvChatMyMsg.visibility = View.VISIBLE
            // VISIBLE인 값은 설정해주자
            holder.tvChatMyMsg.setText(chatList.get(position).msg)
        }else{
            // 다른 사람이 보낸 메세지
            // 이미지 , 이름 , 회색 메세지가 보이도록 설정
            holder.imgChat.visibility = View.VISIBLE
            holder.tvChatOtherName.visibility = View.VISIBLE
            holder.tvChatOtherMsg.visibility = View.VISIBLE
            // VISIBLE인 값은 설정해주자
            holder.imgChat.setImageResource(R.drawable.img_contact1)
            holder.tvChatOtherName.setText(chatList.get(position).name)
            holder.tvChatOtherMsg.setText(chatList.get(position).msg)

            holder.tvChatMyMsg.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return chatList.size
    }

}