package com.stiki.marcelloilham.guppymarketidn.intro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.stiki.marcelloilham.guppymarketidn.R
import java.util.concurrent.TimeUnit

class ForgotPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var editTextPhone: EditText
    private lateinit var btnGetOTP: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        val btnBackForgot = findViewById<ImageView>(R.id.btnBackForgot)
        btnBackForgot.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
//        val btnGetOTP = findViewById(R.id.btnGetOTP) as Button
//        btnGetOTP.setOnClickListener {
//            intent = Intent(this, VerifyOTP::class.java)
//            startActivity(intent)
//            finish()
//        }

        auth = FirebaseAuth.getInstance()
        editTextPhone = findViewById(R.id.editTextPhone)
        btnGetOTP = findViewById(R.id.btnGetOTP)

        btnGetOTP.setOnClickListener {
            val phoneNumber = editTextPhone.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                sendVerificationCode(phoneNumber)
            } else {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Automatically verify and sign in the user
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@ForgotPassword, e.message, Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, token)
                    this@ForgotPassword.verificationId = verificationId
                    Toast.makeText(this@ForgotPassword, "Verification code sent", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Authentication Successful", Toast.LENGTH_LONG).show()
                    // Proceed to reset password or main activity
                } else {
                    // Sign in failed, display a message and update the UI
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_LONG).show()
                }
            }
    }
}