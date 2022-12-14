package com.example.ex221210

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ex221210.fragment.Fragment3
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.properties.Delegates


class MainMapActivity : AppCompatActivity(),OnMapReadyCallback  {

    // googleMap 객체 전역변수 설정
    lateinit var googleMap : GoogleMap
    var lat by Delegates.notNull<Double>()
    var lng by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_map)

        // activity_main.xml의 지도가 띄워진 Fragment 불러오기
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        lat = intent.getStringExtra("latitude")!!.toDouble()
        lng = intent.getStringExtra("longitude")!!.toDouble()


    }

    override fun onMapReady(p0: GoogleMap) {
        // 전역변수로 설정해둔 객체에 값 넣기
        this.googleMap = p0
        // 원하는 위치의 위도경도 값 담아두기

        // 지도 줌이 이상해서 일단 만들어 놓음
        val latLngMinus = LatLng(lat-0.055,lng)

        val latLng = LatLng( lat,lng )
        val latLng1 = LatLng( lat+0.001,lng+0.001 )
        val latLng2 = LatLng( lat-0.01,lng-0.0011 )
        val latLng3 = LatLng( lat-0.002,lng-0.003 )
        val latLng4 = LatLng( lat-0.0025,lng+0.0011 )
        // 내가 정한 위도 경도를 중앙에 둔 지도 만들기
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        // 지도 줌 조정
        googleMap.moveCamera(CameraUpdateFactory.zoomTo((15).toFloat()))
//         마커 더하기
//        var markerOptions : MarkerOptions = MarkerOptions().position(latLng).title("현 위치")
        var markerOptions1 : MarkerOptions = MarkerOptions().position(latLng1).title("성준 농장 ★4.5").snippet("대표 작물 : 토마토")
        var markerOptions2 : MarkerOptions = MarkerOptions().position(latLng2).title("정우 농장 ★4.5").snippet("대표 작물 : 양배추")
        var markerOptions3 : MarkerOptions = MarkerOptions().position(latLng3).title("혜원 농장 ★4.5").snippet("대표 작물 : 상추")
        var markerOptions4 : MarkerOptions = MarkerOptions().position(latLng4).title("연주 농장 ★4.5").snippet("대표 작물 : 당근")


//        googleMap.addMarker(markerOptions)
        googleMap.addMarker(markerOptions1)
        googleMap.addMarker(markerOptions2)
        googleMap.addMarker(markerOptions3)
        googleMap.addMarker(markerOptions4)


//  마커클릭 이벤트
//        googleMap.setOnMarkerClickListener(OnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
//            // which is clicked and displaying it in a toast message.
//            val markerName: String = marker.title.toString()
//            Toast.makeText(this, "Clicked location is $markerName", Toast.LENGTH_SHORT)
//                .show()
//            false
//        })
        
//        마커 타이틀 클릭 이벤트
        googleMap.setOnInfoWindowClickListener { marker ->

            val markerName: String = (marker.title.toString()).substring(0,5)

            var intent = Intent(this,FarmActivity::class.java)
            intent.putExtra("farm",markerName)
            startActivity(intent)

            false
        }


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
                                this@MainMapActivity, arrayOf(
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
        grantResults: IntArray,
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
