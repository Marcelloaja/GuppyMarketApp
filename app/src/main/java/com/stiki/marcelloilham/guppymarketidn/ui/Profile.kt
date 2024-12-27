package com.stiki.marcelloilham.guppymarketidn.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.stiki.marcelloilham.guppymarketidn.R
import com.stiki.marcelloilham.guppymarketidn.menuProfile.MenuProfile

class Profile : Fragment() {

    private lateinit var textName: TextView
    private lateinit var textEmail: TextView
    private lateinit var textPhone: TextView
    private lateinit var textDescription: TextView
    private lateinit var btnMenuProfile: ImageButton

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        textName = view.findViewById(R.id.namaProfile)
        textEmail = view.findViewById(R.id.emailProfile)
        textPhone = view.findViewById(R.id.phoneProfile)
        textDescription = view.findViewById(R.id.descriptionProfile)
        btnMenuProfile = view.findViewById(R.id.btnMenuProfile)

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val userId = firebaseUser.uid
            val userDocRef = db.collection("user").document(userId)

            userDocRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name").orEmpty()
                    val email = document.getString("email").orEmpty()
                    val phone = document.getString("phone").orEmpty()
                    val description = document.getString("description").orEmpty()

                    textName.text = name
                    textEmail.text = email
                    textPhone.text = phone
                    textDescription.text = description

                    saveUserDataLocally(name, email, phone, description)
                    Log.d("Profile", "Data retrieved: Name=$name, Email=$email, Phone=$phone, Description=$description")
                } else {
                    Log.e("Profile", "No document exists for user.")
                    loadUserDataLocally() // Load locally saved data if Firestore data is not available
                }
            }.addOnFailureListener { error ->
                Log.e("Profile", "Error retrieving data: ", error)
                loadUserDataLocally()
            }
        }

        btnMenuProfile.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(context, R.anim.button_click)
            btnMenuProfile.startAnimation(animation)
            btnMenuProfile.postDelayed({
                startActivity(Intent(activity, MenuProfile::class.java))
            }, animation.duration)
        }

        return view
    }

    private fun saveUserDataLocally(name: String, email: String, phone: String, description: String) {
        val prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("name", name)
            putString("email", email)
            putString("phone", phone)
            putString("description", description)
            apply()
        }
    }

    private fun loadUserDataLocally() {
        val prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        textName.text = prefs.getString("name", "")
        textEmail.text = prefs.getString("email", "")
        textPhone.text = prefs.getString("phone", "")
        textDescription.text = prefs.getString("description", "")
    }
}