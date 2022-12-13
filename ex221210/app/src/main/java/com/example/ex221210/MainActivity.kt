package com.example.ex221210

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(),OnMapReadyCallback  {

    // googleMap 객체 전역변수 설정
    lateinit var googleMap : GoogleMap
    var lat by Delegates.notNull<Double>()
    var lng by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // activity_main.xml의 지도가 띄워진 Fragment 불러오기
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        lat = intent.getStringExtra("latitude")!!.toDouble()
        lng = intent.getStringExtra("longitude")!!.toDouble()

        Toast.makeText(this,"위도 : $lat / 경도 : $lng",Toast.LENGTH_LONG).show()


    }

    override fun onMapReady(p0: GoogleMap) {
        // 전역변수로 설정해둔 객체에 값 넣기
        this.googleMap = p0
        // 원하는 위치의 위도경도 값 담아두기
        val latLng = LatLng( lat,lng )
        // 내가 정한 위도 경도를 중앙에 둔 지도 만들기
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        // 지도 줌 조정
        googleMap.moveCamera(CameraUpdateFactory.zoomTo((16).toFloat()))
        // 마커 더하기
        var markerOptions : MarkerOptions = MarkerOptions().position(latLng).title("현 위치")
        googleMap.addMarker(markerOptions)
        // 마이로케이션 버튼 추가
        // (버튼 클릭 시 나의 현재 위치로 지도의 중심을 옮겨주는 버튼)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }else{
            checkLocationPermissionWithRationale()
        }
    }


    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    private fun checkLocationPermissionWithRationale() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("위치정보")
                    .setMessage("이 앱을 사용하기 위해서는 위치정보에 접근이 필요합니다. 위치정보 접근을 허용하여 주세요.")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            ActivityCompat.requestPermissions(
                                this@MainActivity, arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ), MY_PERMISSIONS_REQUEST_LOCATION
                            )
                        }).create().show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: ArrayList<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        googleMap.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}
