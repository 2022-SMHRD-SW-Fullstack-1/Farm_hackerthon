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
import com.example.ex221210.board.BoardAdapter
import com.example.ex221210.board.BoardInsideActivity
import com.example.ex221210.board.BoardVO
import com.example.ex221210.board.BoardWriteActivity
import com.example.ex221210.utils.FBdatabase
import com.example.ex221210.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Fragment3 : Fragment() {

    // getBoardData를 통해 받아온 item(BoardVO)를 관리하는 배열
    // 게시글 (=제목+본문+uid+시간) 목록
    var boardList = ArrayList<BoardVO>()
    // 리스트뷰 어댑터 선언
    lateinit var adapter: BoardAdapter

    var keyData = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment3 변수 가져오기
        var view = inflater.inflate(R.layout.fragment_3, container, false)
        val boardRV = view.findViewById<RecyclerView>(R.id.boardRv)
        val btnWrite = view.findViewById<Button>(R.id.btnWrite)

        // btnWrite 버튼을 누르면 BoardWriteActivity로 넘어감
        btnWrite.setOnClickListener {
            val intent = Intent(requireActivity(),BoardWriteActivity::class.java)
            startActivity(intent)
        }

        // 1. board_list.xml 디자인
        // 2. adapter에 보낼 데이터 가져오기
        // firebase에 있는 board 경로에 있는 데이터를 가져오기
        getBoardData() // 함수 밑에 있음
        // 3. Adapter 만들기(data)
        adapter = BoardAdapter(requireContext(), boardList)

        // 클릭 이벤트를 호출
        adapter.setOnItemClickListner(object : BoardAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                // BoardInsideAtivity로 넘어가자
                val intent = Intent(requireContext(), BoardInsideActivity::class.java)
                intent.putExtra("uid", boardList[position].uid)
                intent.putExtra("title", boardList[position].title)
                intent.putExtra("time", boardList[position].time)
                intent.putExtra("content", boardList[position].content)
                intent.putExtra("key", keyData[position])
                startActivity(intent)
            }
        })

        // 4. boardRv에 adapter 적용
        boardRV.adapter = adapter
        boardRV.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    // board에 있는 데이터 다 가져오는 함수
    fun getBoardData(){
        // 데이터베이스에서 컨텐츠의 세부 정보를 검색
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Firebase에서 snapshot으로 데이터를 받아온 경우
                // 게시물의 uid
                // - BoardVO
                // 게시글 목록 비움 : 저장/삭제 마다 데이터 누적돼 게시글 중복으로 저장되는 것 방지
                boardList.clear()

                // 데이터 스냅샷 내 데이터모델 형식으로 저장된
                for(model in snapshot.children){
                    // 아이템 (=게시글)
                    val item = model.getValue(BoardVO::class.java)
                    // 게시글이 있으면
                    if(item != null){
                        // 게시글 목록에 아이템 넣기
                        boardList.add(item)
                    }
                    // 게시글 키 목록에 문자열 형식으로 변환한 키 넣음
                    keyData.add(model.key.toString())
                    // img가 key값으로 이름이 설정되어 있음
                }
                // 전체 게시글 최신순 변경
                boardList.reverse()
                keyData.reverse()
                // 동기화(새로고침) -> 리스트 크기 및 아이템 변화를 어댑터에 알림
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 오류 발생 시 실행되는 함수
            }

        }
        // board에 있는 모든 데이터가 들어감
        FBdatabase.getBoardRef().addValueEventListener(postListener)
    }


}