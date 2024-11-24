package com.example.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.event.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    // Lazily initialize the view binding
    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // Set the content view using view binding

        // Set click listener for the next button
        binding.nextButton.setOnClickListener {
            // Create an intent to start the LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent) // Start the LoginActivity
        }
    }
}
