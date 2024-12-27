package com.stiki.marcelloilham.guppymarketidn.menuProfile

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.stiki.marcelloilham.guppymarketidn.R

class MyProductsActivity : AppCompatActivity() {

    private lateinit var btnBackMyProduct: ImageButton
    private lateinit var btnAddProduct: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_product)
        initializeUI()
        enableEdgeToEdge()
        setupListeners()
    }

    private fun initializeUI() {
        btnBackMyProduct = findViewById(R.id.btnBackMyProduct)
        btnAddProduct = findViewById(R.id.btnAddProduct)
    }

    private fun setupListeners() {
        btnBackMyProduct.setOnClickListener {
            navigateToMenuProfile()
        }

        btnAddProduct.setOnClickListener {
            animateAndNavigateToAddProduct()
        }
    }

    private fun navigateToMenuProfile() {
        startActivity(Intent(this, MenuProfile::class.java))
        finish()
    }

    private fun animateAndNavigateToAddProduct() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
        btnAddProduct.startAnimation(animation)
        btnAddProduct.postDelayed({
            startActivity(Intent(this, AddMyProducts::class.java))
            finish()
        }, animation.duration)
    }
}
