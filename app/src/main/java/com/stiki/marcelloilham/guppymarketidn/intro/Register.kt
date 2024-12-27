package com.stiki.marcelloilham.guppymarketidn.intro

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import com.stiki.marcelloilham.guppymarketidn.MainActivity
import com.stiki.marcelloilham.guppymarketidn.R

class Register : AppCompatActivity() {

    lateinit var editNameRegister: EditText
    lateinit var editEmailRegister: EditText
    lateinit var editPasswordRegister: EditText
    lateinit var editPhoneRegister: EditText
    lateinit var editDescriptionRegister: EditText
    lateinit var btnRegister: Button
    lateinit var btnSigninRegister: TextView
    lateinit var btnBackRegister: ImageView
    lateinit var progressDialog: ProgressDialog

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        editNameRegister = findViewById(R.id.etNameRegister)
        editEmailRegister = findViewById(R.id.etEmailRegister)
        editPasswordRegister = findViewById(R.id.etPasswordRegister)
        editPhoneRegister = findViewById(R.id.etPhone)
        editDescriptionRegister = findViewById(R.id.etDescription)
        btnRegister = findViewById(R.id.btnRegister)
        btnSigninRegister = findViewById(R.id.btnSigninRegister)
        btnBackRegister = findViewById(R.id.btnBackRegister)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Registering")
        progressDialog.setMessage("Please wait...")

        btnBackRegister.setOnClickListener {
            startActivity(Intent(this, Intro::class.java))
            finish()
        }
        btnSigninRegister.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        btnRegister.setOnClickListener {
            if (editNameRegister.text.isNotEmpty() && editEmailRegister.text.isNotEmpty() && editPasswordRegister.text.isNotEmpty() && editPhoneRegister.text.isNotEmpty() && editDescriptionRegister.text.isNotEmpty()) {
                processRegister()
            } else {
                Toast.makeText(this, "Complete all data fields!", LENGTH_SHORT).show()
            }
        }

        editEmailRegister.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val emailInput = s.toString()
                if (!emailInput.contains("@")) {
                    editEmailRegister.setError("Please include an '@' in the email address.")
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    editEmailRegister.setError("Please enter an part following '@' example 'gmail.com'")
                } else {
                    editEmailRegister.error = null
                }
            }
        })
    }

    private fun processRegister() {
        val name = editNameRegister.text.toString()
        val email = editEmailRegister.text.toString()
        val password = editPasswordRegister.text.toString()
        val phone = editPhoneRegister.text.toString()
        val description = editDescriptionRegister.text.toString()

        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    user?.let {
                        val userUpdateProfile = userProfileChangeRequest {
                            displayName = name
                        }
                        it.updateProfile(userUpdateProfile)
                            .addOnCompleteListener { profileUpdateTask ->
                                if (profileUpdateTask.isSuccessful) {
                                    saveUserToDatabase(it, name, email, phone, description)
                                } else {
                                    progressDialog.dismiss()
                                    Toast.makeText(this, profileUpdateTask.exception?.localizedMessage, LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { error ->
                                progressDialog.dismiss()
                                Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
                            }
                    } ?: run {
                        progressDialog.dismiss()
                        Toast.makeText(this, "User creation failed.", LENGTH_SHORT).show()
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, task.exception?.localizedMessage, LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { error ->
                progressDialog.dismiss()
                Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
    }

    private fun saveUserToDatabase(user: FirebaseUser, name: String, email: String, phone: String, description: String) {
        val sPhone = editPhoneRegister.text.toString()
        val sName = editNameRegister.text.toString()
        val sDescription = editDescriptionRegister.text.toString()
        val sEmail = editEmailRegister.text.toString()
        val sPassword = editPasswordRegister.text.toString()

        val useMap = hashMapOf(
            "phone" to sPhone,
            "name" to sName,
            "description" to sDescription,
            "email" to sEmail,
            "password" to sPassword
        )

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("user").document(userId).set(useMap)
            .addOnCompleteListener {
                Toast.makeText(this, "Successfully Added!", Toast.LENGTH_SHORT).show()
                editPhoneRegister.text.clear()
                editNameRegister.text.clear()
                editDescriptionRegister.text.clear()
                editEmailRegister.text.clear()
                editPasswordRegister.text.clear()

                // Start Login activity
                startActivity(Intent(this, Login::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
            }
    }
}
