package com.example.ex221210.board

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ex221210.R
import com.example.ex221210.utils.FBAuth
import com.example.ex221210.utils.FBdatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_farm.view.*
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var imgLoad : ImageView
    lateinit var bitmap : Bitmap
    lateinit var imgCamera: ImageView
    var imgCameraUpload = false
    var imgGalleryUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)
        checkPermission()
        // id 값 찾아오기
        imgLoad = findViewById(R.id.imgEditLoad)
        val etTitle = findViewById<EditText>(R.id.etEditTitle)
        val etContent = findViewById<EditText>(R.id.etEditContent)
        val imgWrite = findViewById<ImageView>(R.id.imgEditWrite)
        imgCamera = findViewById(R.id.imgCamera)
        // 갤러리로 이동해서 이미지를 받아오는
        imgLoad.setOnClickListener {
            imgGalleryUpload = true
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }

        // 카메라로 이동
        imgCamera.setOnClickListener {
            imgCameraUpload = true
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
                launcher.launch(intent)
            }


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
            if(imgGalleryUpload){imgUpload(key)
            }else if(imgCameraUpload){CameraUpload(key)}


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


    // 카메라 권한 받아오기
    private fun checkPermission() {
        var permission = mutableMapOf<String, String>()
        permission["camera"] = Manifest.permission.CAMERA
//        permission["storageRead"] = Manifest.permission.READ_EXTERNAL_STORAGE
//        permission["storageWrite"] =  Manifest.permission.WRITE_EXTERNAL_STORAGE

        var denied = permission.count { ContextCompat.checkSelfPermission(this, it.value)  == PackageManager.PERMISSION_DENIED }

        if(denied > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(permission.values.toTypedArray(), REQUEST_IMAGE_CAPTURE)
        }

    }

    // 카메라 권한 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_IMAGE_CAPTURE) {
            var count = grantResults.count { it == PackageManager.PERMISSION_DENIED }

            if(count != 0) {
                Toast.makeText(applicationContext, "권한을 동의해주세요.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // 동의 받은 경우 실행
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            imgCamera.setImageBitmap(imageBitmap)
        }
    }


    // 게시글에 이미지 첨부
    fun imgUpload(key : String){
        // Cloud Storage에 파일 업로드
        val storage = Firebase.storage
        // 우선 파일 이름을 포함하여 파일의 전체 경로를 가리키는 참조를 만듦
        val storageRef = storage.reference
        // 임의의 게시글 하나와 그 게시글 내 이미지 하나를 쉽게 매칭하기 위해 DB 내 게시글 키값과 첨부한 이미지 이름이 같으면 됨
        val mountainsRef = storageRef.child("$key.png")

        // 적절한 참조를 만들었을 시
        // 갤러리 업로드
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

    fun CameraUpload(key:String){
        // Cloud Storage에 파일 업로드
        val storage = Firebase.storage
        // 우선 파일 이름을 포함하여 파일의 전체 경로를 가리키는 참조를 만듦
        val storageRef = storage.reference
        // 임의의 게시글 하나와 그 게시글 내 이미지 하나를 쉽게 매칭하기 위해 DB 내 게시글 키값과 첨부한 이미지 이름이 같으면 됨
        val mountainsRef = storageRef.child("$key.png")

        // 카메라 업로드
        imgCamera.isDrawingCacheEnabled = true
        imgCamera.buildDrawingCache()

        val bitmap = (imgCamera.drawable as BitmapDrawable).bitmap

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
        }.addOnSuccessListener { taskSnapshot ->

        }
    }


    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && imgGalleryUpload) {
            imgLoad.setImageURI(it.data?.data)

        }
//        if(it.resultCode == RESULT_OK && imgCameraUpload){
//            imgCamera.setImageURI(it.data?.data)
//        }
    }





}