package com.stiki.marcelloilham.guppymarketidn.menuProfile

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.stiki.marcelloilham.guppymarketidn.R

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient() // Handle loading within the WebView
        webView.settings.javaScriptEnabled = true // Enable JavaScript if needed
        webView.settings.loadWithOverviewMode = true // Load the page in overview mode
        webView.settings.useWideViewPort = true // Enable wide view port

        // Get the URL from the intent and load it
        val url = intent.getStringExtra("url")
        webView.loadUrl(url ?: "https://www.google.com") // Default URL
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack() // Go back in WebView history
        } else {
            super.onBackPressed()
        }
    }
}
