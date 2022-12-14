package com.example.ex221210.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ex221210.R
import com.example.ex221210.utils.FBAuth
import com.example.ex221210.utils.FBdatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    lateinit var imgLoad : ImageView
    lateinit var bitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)

        // id 값 찾아오기
        imgLoad = findViewById(R.id.imgEditLoad)
        val etTitle = findViewById<EditText>(R.id.etEditTitle)
        val etContent = findViewById<EditText>(R.id.etEditContent)
        val imgWrite = findViewById<ImageView>(R.id.imgEditWrite)

        // 갤러리로 이동해서 이미지를 받아오는
        imgLoad.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }

        // 모든 값(title content time 등)을 firebase에 저장시켜줘야함
        imgWrite.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()

            // board
            // - key (게시물의 고유한 uid : push())
            // - boardVO(title, content, 사용자 uid, time)
            // FBdatabase.getBoardRef().push().setValue(BoardVO("1","1","1","1"))

            // auth
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            // 실제 데이터 넣어주기
            // setValue가 되기 전에 미리 BoardVO가 저장될 key값(uid_)을 만들기
            var key = FBdatabase.getBoardRef().push().key.toString()

            FBdatabase.getBoardRef().child(key).setValue(BoardVO(title, content, uid, time))
            imgUpload(key)

            val intent = Intent(this, BoardInsideActivity::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("title", title)
            intent.putExtra("time", time)
            intent.putExtra("content", content)
            intent.putExtra("key", key)
            startActivity(intent)


            finish() // 이전 페이지로 돌아가기

        }
    }






//    // 사진찍기 결과 가져오기
//    private val activityResult : ActivityResultLauncher<Intent> = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()){
//        if(it.resultCode == RESULT_OK && it.data != null){
//            // 값 담기
//            val extras = it.data!!.extras
//            // bitmap으로 타입 변경
//            val bitmap = it?.data?.extras?.get("data") as Bitmap
//
//            //화면에 보여주기
//            imgPicture.setImageBitmap(bitmap)
//        }
//    }




    fun imgUpload(key : String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        imgLoad.isDrawingCacheEnabled = true
        imgLoad.buildDrawingCache()
        val bitmap = (imgLoad.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
        }.addOnSuccessListener { taskSnapshot ->

        }
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            imgLoad.setImageURI(it.data?.data)
        }


    }
}