package com.kaboomsanto.chatapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.kaboomsanto.chatapplication.R
import com.kaboomsanto.chatapplication.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var loginBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(loginBinding.root)
        supportActionBar?.title = "Tech_Santo ChatApp Login "
        auth = FirebaseAuth.getInstance()

        loginBtn = findViewById(R.id.buttonLogin)

        loginBinding.textViewSignUp.setOnClickListener {
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }

        loginBinding.apply {
            loginBtn.setOnClickListener {
                val email = editTextEmail.text.toString().trim()
                val password = editTextPassword.text.toString().trim()
                signIn(email, password)
            }
        }
    }

//    private fun showLoader(show: Boolean) {
//        val buttonLogin: Button = findViewById(R.id.buttonLogin)
//        val progressBar: ProgressBar = findViewById(R.id.progressBar)
//        if (show) {
//            progressBar.visibility = View.VISIBLE
//            buttonLogin.isEnabled = false
//            buttonLogin.text = ""
//        } else {
//            progressBar.visibility = View.GONE
//            buttonLogin.isEnabled = true
//            buttonLogin.text = getString(R.string.login)
//        }
//    }

    private fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "You must fill in the fields", Toast.LENGTH_SHORT).show()

            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Login, MainActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
