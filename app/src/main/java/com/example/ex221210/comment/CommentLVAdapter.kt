package com.example.ex221210.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.ex221210.R
import com.example.ex221210.utils.FBAuth
import kotlinx.coroutines.NonDisposableHandle.parent

class CommentLVAdapter(val commentList: MutableList<CommentVO>) : BaseAdapter() {

//    inner class ViewHolder(item)

    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(p0: Int): Any {
       return commentList[p0]
    }

    override fun getItemId(p0: Int): Long {
        val Long = p0.toLong()
        return Long
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        // 레이아웃 인플레이터 -> 리사이클러뷰에서 뷰홀더 만들 때 반복적으로 사용
        view = LayoutInflater.from(parent?.context).inflate(R.layout.comment_lv_item, parent, false)

        // 각 아이템뷰의 본문, 시간 영역에
        val commentMain = view?.findViewById<TextView>(R.id.commentMainArea)
        val commentTime = view?.findViewById<TextView>(R.id.commentTimeArea)

        // 본문, 시간 넣음
        commentMain!!.text = commentList[position].content
        commentTime!!.text = commentList[position].time

        // 댓글 작성자의 uid와 현재 사용자의 uid가 일치하면 댓글 배지가 보이도록 처리
        val myCommentBadge = view?.findViewById<ImageView>(R.id.myCommentBadge)
        myCommentBadge?.isVisible = commentList[position].uid.equals(FBAuth.getUid())

        // 뷰 변환
        return view
    }




}