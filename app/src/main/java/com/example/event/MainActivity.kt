package com.example.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.event.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.event.Fragment.NotificationBottomFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the NavController associated with the fragment container view
        val navController = findNavController(R.id.fragmentContainerView)

        // Find the bottom navigation view
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Setup bottom navigation with the NavController
        bottomNav.setupWithNavController(navController)

        // Set click listener for the notification button
        binding.notificationButton.setOnClickListener {
            // Create an instance of NotificationBottomFragment
            val bottomSheetDialog = NotificationBottomFragment()

            // Show the bottom sheet fragment using the supportFragmentManager
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }
    }
}
