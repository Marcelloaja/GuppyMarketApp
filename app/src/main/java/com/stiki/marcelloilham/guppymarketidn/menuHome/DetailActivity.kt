package com.stiki.marcelloilham.guppymarketidn.menuHome

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.stiki.marcelloilham.guppymarketidn.R

class DetailActivity : AppCompatActivity() {

    private lateinit var btnBackDetail: ImageView
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var btnMin: ImageButton
    private lateinit var btnPlus: ImageButton
    private lateinit var editTextNumber: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_limited)

        // Inisialisasi semua view
        btnBackDetail = findViewById(R.id.btnBackDetails)
        imageView = findViewById(R.id.detail_image)
        textView = findViewById(R.id.detail_text)
        btnMin = findViewById(R.id.btnMin) // Menghubungkan dengan ID yang sesuai
        btnPlus = findViewById(R.id.btnPlus) // Menghubungkan dengan ID yang sesuai
        editTextNumber = findViewById(R.id.editTextNumber) // Menghubungkan dengan ID yang sesuai

        // Set klik listener untuk tombol kembali
        btnBackDetail.setOnClickListener {
            finish() // Mengakhiri aktivitas saat ini dan kembali ke aktivitas sebelumnya
        }

        // Mendapatkan data dari intent
        val itemName = intent.getStringExtra("ITEM_NAME")
        val itemImage = intent.getIntExtra("ITEM_IMAGE", 0)

        // Menampilkan data di view
        imageView.setImageResource(itemImage)
        textView.text = itemName

        // Mengatur fungsi untuk tombol min dan plus
        btnMin.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
            btnMin.startAnimation(animation)
            btnMin.postDelayed({
                var number = editTextNumber.text.toString().toIntOrNull() ?: 0
                if (number > 0) {
                    number--
                    editTextNumber.setText(number.toString())
                }
            }, animation.duration)
        }

        btnPlus.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
            btnPlus.startAnimation(animation)
            btnPlus.postDelayed({
                var number = editTextNumber.text.toString().toIntOrNull() ?: 0
                number++
                editTextNumber.setText(number.toString())
            }, animation.duration)
        }
    }
}