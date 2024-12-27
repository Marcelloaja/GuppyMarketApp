package com.stiki.marcelloilham.guppymarketidn.intro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.stiki.marcelloilham.guppymarketidn.R

class VerifyOTP : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verify_otp)

        val btnBackVerify = findViewById<ImageView>(R.id.btnBackVerify)
        btnBackVerify.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
            finish()
        }

        val btnVerify = findViewById(R.id.btnVerify) as Button
        btnVerify.setOnClickListener{
            intent = Intent(this,ResetPassword::class.java)
            startActivity(intent)
            finish()
        }
    }
}