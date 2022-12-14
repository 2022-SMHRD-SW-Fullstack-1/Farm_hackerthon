package com.example.ex221210.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ex221210.R
import com.example.ex221210.fragment.Fragment3
import com.example.ex221210.utils.FBAuth
import com.example.ex221210.utils.FBdatabase
import com.google.firebase.database.core.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class BoardEditActivity : AppCompatActivity() {

    lateinit var etEditTitle : EditText
    lateinit var etEditContent : EditText
    lateinit var imgEditLoad : ImageView
    lateinit var imgEditWrite : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)
        etEditTitle = findViewById(R.id.etEditTitle)
        etEditContent = findViewById(R.id.etEditContent)
        imgEditLoad = findViewById(R.id.imgEditLoad)
        imgEditWrite = findViewById(R.id.imgEditWrite)

        val content = intent.getStringExtra("content")
        val title = intent.getStringExtra("title")
        val key = intent.getStringExtra("key")
        val uid = intent.getStringExtra("uid")


        imgEditLoad.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }
        imgEditWrite.setOnClickListener {
            editBoardData(key.toString())
            imgUpload(key!!)
        }


    }


    private fun editBoardData(key: String){
        FBdatabase.getBoardRef().child(key).setValue(BoardVO(
            etEditTitle.text.toString(),
            etEditContent.text.toString(),
            FBAuth.getUid(),
            FBAuth.getTime()
        ))
        // 수정 확인 메시지
        Toast.makeText(this, "게시글이 수정되었습니다", Toast.LENGTH_SHORT).show()
        // 액티비티 종료
        finish()
    }
    fun imgUpload(key : String){

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        imgEditLoad.isDrawingCacheEnabled = true
        imgEditLoad.buildDrawingCache()
        val bitmap = (imgEditLoad.drawable as BitmapDrawable).bitmap
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
            imgEditLoad.setImageURI(it.data?.data)
        }


    }




}