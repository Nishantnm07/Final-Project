package com.example.event

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.event.databinding.ActivitySignBinding
import com.example.event.Model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignActivity : AppCompatActivity() {

    // Declaring variables
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var username: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    // Lazily initializing binding variable using view binding
    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Configuring Google sign-in options
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        // Initializing Firebase authentication
        auth = Firebase.auth
        // Initializing Firebase database
        database = Firebase.database.reference
        // Initializing Google sign-in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Setting click listener for create account button
        binding.createAccountButton.setOnClickListener {
            // Getting input values
            username = binding.userName.text.toString()
            email = binding.emailAddress.text.toString().trim()
            password = binding.password.text.toString().trim()

            // Validating input values
            if (email.isEmpty() || password.isBlank() || username.isBlank()) {
                Toast.makeText(this, "Please Fill all the details", Toast.LENGTH_SHORT).show()
            } else {
                // Creating account with provided email and password
                createAccount(email, password)
            }
        }

        // Setting click listener for already have account button
        binding.alreadyhavebutton.setOnClickListener {
            // Navigating to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Setting click listener for Google sign-in button
        binding.googleButton.setOnClickListener {
            // Launching Google sign-in intent
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    // Activity result launcher for Google sign-in
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) { // Check if sign-in was successful
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) { // Check if Google sign-in task was successful
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) { // Check if sign-in with credential was successful
                            // If successful, show a success message, start MainActivity, and finish the current activity
                            Toast.makeText(this, "Sign-in Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            // If sign-in with credential task failed, show a failure message
                            Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // If the result code is not OK, show a failure message
                Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }

    // Method to create user account with email and password
    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) { // Check if account creation was successful
                Toast.makeText(this, "Account Created successfully", Toast.LENGTH_SHORT).show()
                saveUserData() // Save user data to Firebase database
                startActivity(Intent(this, LoginActivity::class.java)) // Start LoginActivity
                finish()
            } else {
                // If account creation failed, show a failure message and log the error
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }

    // Method to save user data to Firebase database
    private fun saveUserData() {
        // Retrieve data from input fields
        username = binding.userName.text.toString()
        email = binding.emailAddress.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user = UserModel(username, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        // Save data to Firebase database
        database.child("user").child(userId).setValue(user)
    }
}
