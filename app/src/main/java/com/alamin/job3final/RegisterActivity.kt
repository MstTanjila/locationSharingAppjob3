package com.alamin.job3final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthenticationViewModel
    private lateinit var firestoreViewModel: FirestoreViewModel
    private lateinit var buttonRegister: Button
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextDisplayName: EditText
    private lateinit var textViewLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegister = findViewById(R.id.registerBtn)
        editTextEmail = findViewById(R.id.emailEt)
        editTextPassword = findViewById(R.id.passwordEt)
        editTextDisplayName = findViewById(R.id.displayNameEt)
        textViewLogin = findViewById(R.id.loginTxt)

        authViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)

        buttonRegister.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            authViewModel.register(email, password, {
                // Registration successful, save user data to Firestore
                val displayName = editTextDisplayName.text.toString()
                val location = "Location not available" // Default value
                firestoreViewModel.saveUser(authViewModel.getCurrentUserId(), displayName, email, location)

                // Navigate to main activity or any other screen
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, { errorMessage ->
                // Display error message to user
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            })
        }
        textViewLogin.setOnClickListener {
            // Navigate to registration screen
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser!=null){
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()
        }
    }
}