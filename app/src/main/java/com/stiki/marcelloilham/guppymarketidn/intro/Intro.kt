package com.stiki.marcelloilham.guppymarketidn.intro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.stiki.marcelloilham.guppymarketidn.R

class Intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val loginButtonIntro = findViewById(R.id.loginButtonIntro) as Button
        loginButtonIntro.setOnClickListener{
            intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }

        val registerButtonIntro = findViewById(R.id.registerButtonIntro) as Button
        registerButtonIntro.setOnClickListener{
            intent = Intent(this,Register::class.java)
            startActivity(intent)
            finish()
        }
    }
}