package com.example.ex221210

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // 채팅만들기
        // AdapterView6단계
        // 1. Container 결정

        val rvChat = view.findViewById<RecyclerView>(R.id.rvChat)
        val etChat = view.findViewById<EditText>(R.id.etChat)
        val btnChat = view.findViewById<Button>(R.id.btnChat)

        // 2. Template 결정
        // chat_list.xml

        // 3. Item 결정
        // ChatVO
        val chatList = ArrayList<ChatVO>()


        // 4. Adapter 결정
        val spf = activity?.getSharedPreferences(
            "loginInfo",
            Context.MODE_PRIVATE
        )

        val loginId = spf?.getString("loginId", "null") as String

        val adapter = ChatAdapter(requireContext(), chatList, loginId)

        // 5. Container에 Adapter 부착
        rvChat.adapter = adapter
        rvChat.layoutManager = LinearLayoutManager(requireContext())

        // 6. Event 처리

        val db = Firebase.database
        val chatRef = db.getReference("chat")

        btnChat.setOnClickListener {
            val msg = etChat.text.toString()
            // FireBase RealTime DataBase의
            // chat 경로에 ChatVO class 를 setValue
            val chat = ChatVO(loginId, msg)
            // FireBase에 추가추가추가하기
            chatRef.push().setValue(chat)
            // 채팅 창 비워주기
            etChat.text = null
        }

        val seedSpf = activity?.getSharedPreferences(
            "seedInfo",
            Context.MODE_PRIVATE
        )
        val seedInfo = seedSpf?.getString("mySeed","") as String
        if(seedInfo!=""){
            val chat = ChatVO(loginId, seedInfo)
            chatRef.push().setValue(chat)
        }

        chatRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val chatItem = snapshot.getValue<ChatVO>() as ChatVO
                chatList.add(chatItem)
                adapter.notifyDataSetChanged()
                val editor = seedSpf.edit()
                editor.putString("mySeed","")
                editor.commit()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        return view
    }
}