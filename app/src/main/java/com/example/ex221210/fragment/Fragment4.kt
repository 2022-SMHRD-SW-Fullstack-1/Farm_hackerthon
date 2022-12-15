package com.example.ex221210.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ex221210.R
import com.example.ex221210.sns.SnsInsideActivity
import com.example.ex221210.sns.SnsAdapter
import com.example.ex221210.sns.SnsVO
import com.example.ex221210.sns.SnsWriteActivity
import com.example.ex221210.utils.FBdatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Fragment4 : Fragment() {

    // getBoardData를 통해 받아온 item(BoardVO)을 관리하는 배열
    var boardList = ArrayList<SnsVO>()
    lateinit var adapter: SnsAdapter
    var keyData = ArrayList<String>()
    var bookmarkList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?
        ,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // btnWrite를 클릭하면 BoardWriteActivity로 이동

        var view = inflater.inflate(R.layout.fragment_4, container, false)
        val boardRv = view.findViewById<RecyclerView>(R.id.boardRv)
        val btnWrite = view.findViewById<Button>(R.id.btnWrite)

        btnWrite.setOnClickListener {
            val intent = Intent(requireActivity(), SnsWriteActivity::class.java)
            startActivity(intent)
        }

        //1. 한칸에 들어갈 디자인 만들기(board_list.xml)
        //2. adapter에 보낼 데이터 가져오기
        //firebase에 있는 board 경로에 있는 데이터를 가저오기
        getBookmarkData()
        getBoardData()
        //3. Adatper 만들기(data)
        adapter = SnsAdapter(requireContext(), boardList,keyData, bookmarkList)

        //클릭 이벤트를 호출
        adapter.setOnItemClickListener(object : SnsAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(requireContext(),
                    SnsInsideActivity::class.java)
                intent.putExtra("title", boardList[position].title)
                intent.putExtra("time", boardList[position].time)
                intent.putExtra("content", boardList[position].content)
                intent.putExtra("key",keyData[position])
                startActivity(intent)
            }
        })

        //4. boardRv 에 adapter 적용
        boardRv.adapter = adapter
        boardRv.layoutManager = LinearLayoutManager(requireContext())


        return view
    }

    //board에 있는 데이터 다~~ 가져오는 함수
    fun getBoardData(){
        val postListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //Firebase에서 snapshot으로 데이터를 받아온 경우
                // 게시물의 uid
                // - BoardVO
                for (model in snapshot.children){
                    val item = model.getValue(SnsVO::class.java)
                    if (item != null) {
                        boardList.add(item)
                    }
                    keyData.add(model.key.toString())
                    //img가 key값으로 이름이 설정되어 있음
                }
                //adapter를 새로고침
                boardList.reverse()
//                keyData.reverse()
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //오류가 발생했을 경우 실행되는 함수
            }

        }

        //board에 있는 모든~~ 데이터가 들어간다~
        FBdatabase.getSnsRef().addValueEventListener(postListener)
    }

    fun getBookmarkData(){
        val bookmarkListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (model in snapshot.children){
                    val item = model.getValue(SnsVO::class.java)
                    if(item!=null){
                        boardList.add(item)
                    }
                    keyData.add(model.key.toString())
                }
                //adapter를 새로고침
                boardList.reverse()
                keyData.reverse()
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //오류가 발생했을 경우 실행되는 함수
            }

        }
        FBdatabase.getBookmarkRef().addValueEventListener(bookmarkListener)

    }

}