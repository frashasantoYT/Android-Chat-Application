package com.kaboomsanto.chatapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kaboomsanto.chatapplication.databinding.ActivityRegisterBinding
import com.kaboomsanto.chatapplication.models.User

class Register : AppCompatActivity() {
    private lateinit var registerBinding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(registerBinding.root)
        supportActionBar?.title = "Tech_Santo ChatApp Register "
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        registerBinding.textViewLogin.setOnClickListener {
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }


        registerBinding.apply {
            buttonSignUp.setOnClickListener {
                val email = editTextEmail.text.toString().trim()
                val password = editTextPassword.text.toString().trim()
                val confirmPass = editTextConfirmPassword.text.toString()
                val fullName = editTextFullName.text.toString()


                if(password != confirmPass){
                    Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                register(email,password,fullName)

            }
        }

    }


    fun register(email: String, password: String, fullName:String){
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(applicationContext, "All fields should be filled", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            task ->
            if (task.isSuccessful){
              val userId = auth.currentUser?.uid
                val user = User(userId, fullName, email)
                userId?.let {
                    database.reference.child("users").child(it).setValue(user).addOnCompleteListener {
                        dbTask ->
                        if(dbTask.isSuccessful){
                            Toast.makeText(applicationContext, "You have successfully registered", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@Register, Login::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(applicationContext, "Failed to save user data", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            }
            else{
               Toast.makeText(applicationContext, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }


    }
}