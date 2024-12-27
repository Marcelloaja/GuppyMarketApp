package com.stiki.marcelloilham.guppymarketidn.intro

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.JsonToken
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.stiki.marcelloilham.guppymarketidn.MainActivity
import com.stiki.marcelloilham.guppymarketidn.R

class Login : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var btnBackLogin: ImageView
    private lateinit var signupLogin: TextView
    private lateinit var btnForgotPassword: TextView
    private lateinit var editEmailLogin: EditText
    private lateinit var editPasswordLogin: EditText
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var progressDialog: ProgressDialog
    // BTN GOOGLE
    private lateinit var btnGoogle: ImageView
    private lateinit var googleSignInClient: GoogleSignInClient
    companion object {
        private const val RC_SIGN_IN = 1001
    }
    // DATABASE
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val prefs by lazy { getSharedPreferences("login_prefs", Context.MODE_PRIVATE) }
    private val userprefs by lazy { getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }

    override fun onStart() {
        super.onStart()
        // jika sudah login diarahkan ke MainActivity
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigate_to", "HomeFragment")
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin = findViewById(R.id.btnLogin)
        btnBackLogin = findViewById(R.id.btnBackLogin)
        signupLogin = findViewById(R.id.signup_login)
        btnForgotPassword = findViewById(R.id.btnForgotPassword)
        editEmailLogin = findViewById(R.id.etEmailLogin)
        editPasswordLogin = findViewById(R.id.etPasswordLogin)
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox)

        btnGoogle = findViewById(R.id.btnLoginGoogle)

        progressDialog = ProgressDialog(this).apply {
            setTitle("Logging")
            setMessage("Please wait...")
        }

        loadLoginData()

        //LOGIN WITH GOOGLE
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnLogin.setOnClickListener {
            if (editEmailLogin.text.isNotEmpty() && editPasswordLogin.text.isNotEmpty()) {
                prosesLogin()
            } else {
                Toast.makeText(this, "Complete all data fields!", LENGTH_SHORT).show()
            }
        }

        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        btnBackLogin.setOnClickListener {
            startActivity(Intent(this, Intro::class.java))
            finish()
        }
        signupLogin.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
            finish()
        }
        btnForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
            finish()
        }

        // Email validation
        editEmailLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed while text is changing
            }

            override fun afterTextChanged(s: Editable?) {
                val emailInput = s.toString()
                if (!emailInput.contains("@")) {
                    editEmailLogin.error = "Please include an '@' in the email address."
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    editEmailLogin.error = "Please enter a part following '@', example 'gmail.com'"
                } else {
                    editEmailLogin.error = null  // Clear the error
                }
            }
        })
    }

    private fun prosesLogin() {
        val email = editEmailLogin.text.toString()
        val password = editPasswordLogin.text.toString()

        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (rememberMeCheckbox.isChecked) {
                    saveLoginData(email, password)
                    println("Login_data : Remember Me enabled, data saved.")
                } else {
                    clearLoginData()
                    println("Login_data : Remember Me disabled, data not saved.")
                }

                // Add an extra to the intent
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("navigate_to", "HomeFragment")
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
            }
    }

    private fun saveLoginData(email: String, password: String) {
        prefs.edit().apply {
            putString("email", email)
            putString("password", password)
            putBoolean("remember_me", true)
            apply()
        }
    }
    private fun loadLoginData() {
        if (prefs.getBoolean("remember_me", false)) {
            editEmailLogin.setText(prefs.getString("email", ""))
            editPasswordLogin.setText(prefs.getString("password", ""))
            rememberMeCheckbox.isChecked = true
        }
    }
    private fun clearLoginData() {
        prefs.edit().clear().apply()
    }

    // LOGIN WITH GOOGLE
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            //Menangani Proses Login Google
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Jika berhasil
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, e.localizedMessage, LENGTH_SHORT).show()
            }
        }
    }
    fun firebaseAuthWithGoogle(idToken: String) {
        progressDialog.show()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
//                val user = firebaseAuth.currentUser
                val account = GoogleSignIn.getLastSignedInAccount(this)
                val displayName = account?.displayName
                val email = account?.email

                userprefs.edit().apply {
                    putString("name", displayName)
                    putString("email", email)
                    apply()
                }

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("navigate_to", "HomeFragment")
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
            }
    }
}
