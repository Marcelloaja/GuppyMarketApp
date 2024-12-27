package com.stiki.marcelloilham.guppymarketidn.menuProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.stiki.marcelloilham.guppymarketidn.R

class Information : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_information)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val whatsappButton: ImageButton = findViewById(R.id.whatsapp_button)
        val followButton: TextView = findViewById(R.id.follow_button)
        val facebookButton: ImageButton = findViewById(R.id.facebook_button)
        val downloadButton: Button = findViewById(R.id.download_guide_button)

        whatsappButton.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
            whatsappButton.startAnimation(animation)
            whatsappButton.postDelayed({
                val uri = Uri.parse("https://api.whatsapp.com/send?phone=62881026433688")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }, animation.duration)
        }

        followButton.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
            followButton.startAnimation(animation)
            followButton.postDelayed({
                val uri = Uri.parse("https://www.instagram.com/cello.tag")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }, animation.duration)
        }

        facebookButton.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
            facebookButton.startAnimation(animation)
            facebookButton.postDelayed({
                val uri = Uri.parse("https://www.facebook.com/MarcelloParkHae")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }, animation.duration)
        }

        // Set up the Download Guide button
        downloadButton.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
            downloadButton.startAnimation(animation)
            downloadButton.postDelayed({
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", "https://drive.google.com/file/d/1AwopxNnAjbdJPYIPj43WU6jLmaz2ExpL/view?usp=drive_link")
                startActivity(intent)
            }, animation.duration)
        }
    }
}
