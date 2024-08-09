package com.example.betapp.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.betapp.R

class PaymentWebviewActivity : AppCompatActivity() {
    private lateinit var webview:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_webview)
        webview=findViewById(R.id.webview)
        webview.getSettings().setJavaScriptEnabled(true);
        val mobileUserAgent =
            "Mozilla/5.0 (Linux; Android 9; Pixel 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Mobile Safari/537.36"
        webview.settings.userAgentString = mobileUserAgent
        val url=intent.getStringExtra("url")
        Log.d("url",url!!)
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    // Handle the URL change here
                    Log.d("Redirected URL:", " $it")
                    if (it.contains("https://marutibets.com/api/pay_callback.php")) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            finish()
                        }, 5000)
                    } else if (it.startsWith("ppe:") or it.startsWith("gpay:") or it.startsWith("upi:")or it.startsWith("paytmmp:")   ) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        view?.context?.startActivity(intent)
                        return true
                    } else {
                        return false
                    }
                    // Load the URL in the WebView
                }
                return false
            }
        }

        webview.loadUrl(url!!)

    }}
