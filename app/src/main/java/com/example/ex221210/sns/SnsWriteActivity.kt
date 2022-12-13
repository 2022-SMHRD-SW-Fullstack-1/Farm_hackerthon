package com.example.ex221210.sns

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ex221210.R
import com.example.ex221210.utils.FBAuth
import com.example.ex221210.utils.FBdatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class SnsWriteActivity : AppCompatActivity() {

    lateinit var imgLoad : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sns_write)

        //id 값 찾아오기
        imgLoad = findViewById(R.id.imgload)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val imgWrite = findViewById<ImageView>(R.id.imgWrite)

        //갤러리로 이동해서 이미지를 받아오는
        imgLoad.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }
        // 모든 값(title content time ...)을 firebase에 저장시켜줘야함
        imgWrite.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()

            // board
            // -key(게시물의 고유한 uid : push())
            //  - boardVO( title, content, 사용자 uid, time)
            //FBdatabase.getBoardRef().push().setValue(BoardVO("1","1","1","1"))

            //auth
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            // 실제 데이터를 넣어주자
            //setValue가 되기전에 미리 BoardVO가 저장될 key값(uid_)을 만들자
            var key = FBdatabase.getBoardRef().push().key.toString()
            FBdatabase.getBoardRef().child(key).setValue(SnsVO(title, content, uid, time))
            imgUpload(key)
            finish()//이전 페이지로 돌아가기
        }

    }

    fun imgUpload(key: String){

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
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            imgLoad.setImageURI(it.data?.data)
        }


    }
}