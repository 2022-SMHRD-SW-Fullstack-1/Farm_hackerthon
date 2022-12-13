package com.example.ex221210.board

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log.d
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.ex221210.R
import com.example.ex221210.utils.FBAuth
import com.example.ex221210.utils.FBdatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.Permissions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.Logger

class BoardWriteActivity : AppCompatActivity() {

    lateinit var imgLoad : ImageView
    lateinit var imgPicture : ImageView
    lateinit var bitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)

        // id 값 찾아오기
        imgLoad = findViewById(R.id.imgLoad)
        imgPicture = findViewById(R.id.imgPicture)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val imgWrite = findViewById<ImageView>(R.id.imgWrite)

        // 갤러리로 이동해서 이미지를 받아오는
        imgLoad.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }

        // 사진 찍어서 업로드
        imgPicture.setOnClickListener {
            // 사진 촬영
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResult.launch(intent)
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
            finish() // 이전 페이지로 돌아가기
        }
    }

    // 결과 가져오기
    private val activityResult : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){



        if(it.resultCode == RESULT_OK && it.data != null){
           // 값 담기
           val extras = it.data!!.extras

           // bitmap으로 타입 변경
           bitmap = extras?.get("data") as Bitmap

           // 화면에 보여주기
           imgPicture.setImageBitmap(bitmap)
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