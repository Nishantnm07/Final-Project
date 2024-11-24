package com.example.event

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.event.databinding.ActivityDetailsBinding
import com.example.event.Model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var itemNames: String? = null
    private var itemImages: String? = null
    private var itemDescriptions: String? = null
    private var itemPrices: String? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Retrieve item details from intent extras
        itemNames = intent.getStringExtra("MenuItemName")
        itemDescriptions = intent.getStringExtra("MenuItemDescription")
        itemPrices = intent.getStringExtra("MenuItemPrice")
        itemImages = intent.getStringExtra("MenuItemImage")

        // Set item details to views using view binding
        with(binding) {
            detailitemName.text = itemNames
            detailDescription.text = itemDescriptions
            Glide.with(this@DetailsActivity).load(Uri.parse(itemImages)).into(detailitemImage)
        }

        // Set click listener for the back button
        binding.imageButton.setOnClickListener {
            finish() // Finish the activity and go back to the previous activity
        }

        // Set click listener for the add item button
        binding.addItemButton.setOnClickListener {
            addItemToCart() // Call addItemToCart function to add the item to the cart
        }
    }

    // Function to add the item to the cart
    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: "" // Get the current user's ID

        // Create a CartItems object with the item details
        val cartItem = CartItems(
            itemNames.toString(),
            itemPrices.toString(),
            itemDescriptions.toString(),
            itemImages.toString(),
            1 // Quantity set to 1 by default
        )

        // Save the cart item data to Firebase database
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem)
            .addOnSuccessListener {
                // If the operation is successful, show a success message
                Toast.makeText(this, "Item added to cart successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // If the operation fails, show a failure message
                Toast.makeText(this, "Failed to add item to cart", Toast.LENGTH_SHORT).show()
            }
    }
}
