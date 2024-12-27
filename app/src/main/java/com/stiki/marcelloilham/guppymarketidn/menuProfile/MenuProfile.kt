package com.stiki.marcelloilham.guppymarketidn.menuProfile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.stiki.marcelloilham.guppymarketidn.R
import com.stiki.marcelloilham.guppymarketidn.intro.Login
import java.io.File
import java.text.DecimalFormat

class MenuProfile : AppCompatActivity() {

    private lateinit var btnBackProfile: ImageView
    private lateinit var btnLogout: TextView
    private lateinit var btnMyProducts: ImageView
    private lateinit var imgInformation: ImageButton
    private lateinit var menuChatCS: TextView
    private lateinit var clearCacheLayout: LinearLayout
    private lateinit var cacheSizeText: TextView
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 1001
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_profile)

        btnBackProfile = findViewById(R.id.btnBackProfile)
        btnLogout = findViewById(R.id.btnLogout)
        btnMyProducts = findViewById(R.id.btnMyProducts)
        menuChatCS = findViewById(R.id.menu_chat_cs)
        clearCacheLayout = findViewById(R.id.clear_cache_layout)
        cacheSizeText = findViewById(R.id.cache_size_text)
        imgInformation = findViewById(R.id.imgInformation)

        // Display the cache size on startup
        displayCacheSize()

        btnBackProfile.setOnClickListener {
            finish()  // Close the current activity and return to the previous one
        }

        btnMyProducts.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
            btnMyProducts.startAnimation(animation)
            btnMyProducts.postDelayed({
                startActivity(Intent(this, MyProductsActivity::class.java))
                finish()
            }, animation.duration)
        }

        imgInformation.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
            imgInformation.startAnimation(animation)
            imgInformation.postDelayed({
                startActivity(Intent(this, Information::class.java))
            }, animation.duration)
        }

        btnLogout.setOnClickListener {
            // Create an AlertDialog to confirm logout
            val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
            btnLogout.startAnimation(animation)
            btnLogout.postDelayed({
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout")
                builder.setMessage("Are you sure you want to logout?")

                builder.setPositiveButton("Yes") { dialog, _ ->
                    googleSignInClient.signOut()
                    FirebaseAuth.getInstance().signOut()
                    clearUserDataLocally() // Clear locally saved data when logging out
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }

                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss() // Dismiss the dialog if the user cancels
                }

                // Show the AlertDialog
                val dialog = builder.create()
                dialog.show()
            }, animation.duration)
        }

        menuChatCS.setOnClickListener {
            val userName = getUserNameFromDatabase()
            val message =
                "*Chat from Guppy Market!*\n\nFrom: _${userName}_\nHello, thank you for contacting CS.\nQuestion: "
            val phoneNumberWithCountryCode = "+6289625976080"
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        String.format(
                            "https://api.whatsapp.com/send?phone=%s&text=%s",
                            phoneNumberWithCountryCode, message
                        )
                    )
                )
            )
        }

        clearCacheLayout.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.button_click)
            clearCacheLayout.startAnimation(animation)
            clearCacheLayout.postDelayed({
                clearCache()
            }, animation.duration)
        }
    }

    private fun displayCacheSize() {
        val cacheSize = getCacheSize(this)
        cacheSizeText.text = formatSize(cacheSize)
    }

    private fun getCacheSize(context: Context): Long {
        var size: Long = 0
        try {
            val cacheDir = context.cacheDir
            size += getDirSize(cacheDir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    private fun getDirSize(dir: File): Long {
        var size: Long = 0
        for (file in dir.listFiles()!!) {
            size += if (file.isDirectory) {
                getDirSize(file)
            } else {
                file.length()
            }
        }
        return size
    }

    private fun formatSize(size: Long): String {
        val hrSize: String
        val m = size / 1024.0
        val dec = DecimalFormat("0.00")
        hrSize = if (m > 1) {
            val g = m / 1024.0
            if (g > 1) {
                val t = g / 1024.0
                if (t > 1) {
                    dec.format(t) + " TB"
                } else {
                    dec.format(g) + " GB"
                }
            } else {
                dec.format(m) + " MB"
            }
        } else {
            dec.format(size.toDouble()) + " KB"
        }
        return hrSize
    }

    private fun clearCache() {
        try {
            val dir = cacheDir
            deleteDir(dir)
            displayCacheSize()
            Toast.makeText(this, "Cache cleared", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to clear cache", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

    private fun getUserNameFromDatabase(): String {
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getString("name", "Friend") ?: "Friend"
    }

    private fun clearUserDataLocally() {
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}