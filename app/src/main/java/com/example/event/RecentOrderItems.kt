package com.example.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.event.Adapter.RecentBuyAdapter
import com.example.event.Model.OrderDetails
import com.example.event.databinding.ActivityRecentOrderItemsBinding

class RecentOrderItems : AppCompatActivity() {
    // Lazily initialize the view binding
    private val binding: ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }

    // Declare variables to hold item details
    private lateinit var allitemNames: ArrayList<String>
    private lateinit var allitemImages: ArrayList<String>
    private lateinit var allitemPrices: ArrayList<String>
    private lateinit var allitemQuantities: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // Set the content view using view binding

        // Set click listener for the back button to finish the activity
        binding.backButton.setOnClickListener {
            finish()
        }

        // Retrieve recent order items from intent extras
        val recentOrderItems = intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                val recentOrderItem = orderDetails[0]

                // Extract item details from the recent order item
                allitemNames = recentOrderItem.itemNames as ArrayList<String>
                allitemImages = recentOrderItem.itemImages as ArrayList<String>
                allitemPrices = recentOrderItem.itemPrices as ArrayList<String>
                allitemQuantities = recentOrderItem.itemQuantities as ArrayList<Int>
            }
        }

        // Set up the recycler view adapter
        setAdapter()
    }

    private fun setAdapter() {
        val rv = binding.recyclerViewRecentBuy
        rv.layoutManager = LinearLayoutManager(this) // Set layout manager
        val adapter = RecentBuyAdapter(this, allitemNames, allitemImages, allitemPrices, allitemQuantities) // Create adapter
        rv.adapter = adapter // Set adapter to recycler view
    }
}
