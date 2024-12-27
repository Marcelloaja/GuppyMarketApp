package com.stiki.marcelloilham.guppymarketidn.menuProfile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.stiki.marcelloilham.guppymarketidn.R

class AddMyProducts : AppCompatActivity() {

    private lateinit var btnBackAddMyProduct: ImageButton
    private lateinit var spinnerLeft: Spinner
    private lateinit var spinnerRight: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_my_products)
        initializeUI()
        setupListeners()

        spinnerLeft = findViewById(R.id.spinnerLeft)
        spinnerRight = findViewById(R.id.spinnerRight)

        // Data untuk Spinner kiri
        val itemsLeft = listOf(
            "All", "Solid", "Albino", "Tuxedo", "Grass",
            "Lace", "Cobra", "Mozaik", "MetalHead", "SwordTail",
            "ShortTail", "Halfmoon", "CrownTail", "Koi", "BigEar",
            "DumboEar", "Female"
        )
        val adapterLeft = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsLeft)
        adapterLeft.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLeft.adapter = adapterLeft

        // Data untuk Spinner kanan
        val itemsRight = listOf("Available", "Limited")
        val adapterRight = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsRight)
        adapterRight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRight.adapter = adapterRight

        // Menangani pemilihan item pada Spinner kiri
        spinnerLeft.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                Toast.makeText(
                    this@AddMyProducts,
                    "Selected fish species: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }
        }

        // Menangani pemilihan item pada Spinner kanan
        spinnerRight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                Toast.makeText(
                    this@AddMyProducts,
                    "Selected stock: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }
        }

    }

    private fun initializeUI() {
        btnBackAddMyProduct = findViewById(R.id.btnBackAddMyProduct)
    }

    private fun setupListeners() {
        btnBackAddMyProduct.setOnClickListener {
            navigateToMyProductsActivity()
        }
    }

    private fun navigateToMyProductsActivity() {
        startActivity(Intent(this, MyProductsActivity::class.java))
        finish()
    }
}
