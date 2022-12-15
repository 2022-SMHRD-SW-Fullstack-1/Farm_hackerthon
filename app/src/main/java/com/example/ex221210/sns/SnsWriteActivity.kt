package com.example.ex221210.sns

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

class SnsWriteActivity : AppCompatActivity() {

    lateinit var imgSnsLoad : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sns_write)

        // id 값 찾아오기
        imgSnsLoad = findViewById(R.id.imgSnsLoad)
        val etSnsTitle = findViewById<EditText>(R.id.etSnsTitle)
        val etSnsContent = findViewById<EditText>(R.id.etSnsContent)
        val imgSnsWrite = findViewById<ImageView>(R.id.imgSnsWrite)

        // 갤러리로 이동해서 이미지를 받아오는
        imgSnsLoad.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }

        // 모든 값(title content time 등)을 firebase에 저장시켜줘야함
        imgSnsWrite.setOnClickListener {
            val title = etSnsTitle.text.toString()
            val content = etSnsContent.text.toString()

            // auth
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            // 실제 데이터 넣어주기
            // setValue가 되기 전에 미리 BoardVO가 저장될 key값(uid_)을 만들기
            var key = FBdatabase.getBoardRef().push().key.toString()

            FBdatabase.getSnsRef().child(key).setValue(SnsVO(title, content, uid, time))
            imgUpload(key)

//            val intent = Intent(this, SnsInsideActivity::class.java)
//            intent.putExtra("uid", uid)
//            intent.putExtra("title", title)
//            intent.putExtra("time", time)
//            intent.putExtra("content", content)
//            intent.putExtra("key", key)
//            startActivity(intent)


            finish() // 이전 페이지로 돌아가기

        }
    }

    fun imgUpload(key : String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        imgSnsLoad.isDrawingCacheEnabled = true
        imgSnsLoad.buildDrawingCache()
        val bitmap = (imgSnsLoad.drawable as BitmapDrawable).bitmap
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
            imgSnsLoad.setImageURI(it.data?.data)
        }


    }
}