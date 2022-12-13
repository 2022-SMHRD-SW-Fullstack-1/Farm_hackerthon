package com.example.ex221210.sns

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.ex221210.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        // 받아온 Url값을 내서
        // 해당 웹페이지가 WebView에 뜨게 만들자
        // Fragment 1202

        val wv = findViewById<WebView>(R.id.wv)
        val spf = getSharedPreferences(
            "url",
            Context.MODE_PRIVATE
        )
        val url = spf.getString("url","http://www.google.com")
        val ws = wv.settings
        ws.javaScriptEnabled = true
        wv.webViewClient = WebViewClient()
        wv.loadUrl(url!!)
    }
}