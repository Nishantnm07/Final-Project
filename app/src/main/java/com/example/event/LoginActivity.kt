package com.example.event

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.event.databinding.ActivityLoginBinding
import com.example.event.Model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var userName: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        // Initialization of Firebase Auth
        auth = Firebase.auth
        // Initialization of Firebase Database
        database = Firebase.database.reference
        // Initialization of Google
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // login with email and password
        binding.loginButton.setOnClickListener {

            // get data from text field
            email = binding.emailAddress.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter all the details ", Toast.LENGTH_SHORT).show()
            } else {
                createUser()
                Toast.makeText(this, "Login successFull ", Toast.LENGTH_SHORT).show()
            }
        }
        binding.donthavebutton.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }

        // google sign-in
        binding.googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }
    //
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // This block is executed when the activity launched with the launcher completes
        if (result.resultCode == Activity.RESULT_OK) { // Check if the result is OK
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data) // Get the GoogleSignInAccount from the result
            if (task.isSuccessful) { // Check if the sign-in task was successful
                val account: GoogleSignInAccount? = task.result // Get the GoogleSignInAccount object
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null) // Create a credential using the GoogleSignInAccount's ID token
                auth.signInWithCredential(credential).addOnCompleteListener { task -> // Sign in with the credential
                    if (task.isSuccessful) { // Check if the sign-in with credential task was successful
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

    private fun createUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserdata()
                        val user = auth.currentUser
                        updateUi(user)
                    } else {
                        Toast.makeText(this, "Sign-in field ", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveUserdata() {
        // get data from text field
        email = binding.emailAddress.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user = UserModel(userName,email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        // save data in to database
        database.child("user").child(userId).setValue(user)

    }

    override fun onStart() {
        super.onStart()
        val currentUser= auth.currentUser
        if (currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun updateUi(user: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}