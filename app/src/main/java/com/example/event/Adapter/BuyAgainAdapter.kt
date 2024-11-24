package com.example.event.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.event.databinding.BuyAgainItemBinding

// Adapter class for the "Buy Again" items RecyclerView
class BuyAgainAdapter(
    private val buyAgainitemName: MutableList<String>, // List of item names
    private val buyAgainitemPrice: MutableList<String>, // List of item prices
    private val buyAgainitemImage: MutableList<String>, // List of item image URIs
    private var requireContext: Context // Context reference
) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    // Function to bind data to the view holder
    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(
            buyAgainitemName[position], // Bind item name
            buyAgainitemPrice[position], // Bind item price
            buyAgainitemImage[position] // Bind item image URI
        )
    }

    // Function to create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        // Inflate the layout for each item
        val binding = BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    // Function to get item count
    override fun getItemCount(): Int = buyAgainitemName.size

    // View holder class
    inner class BuyAgainViewHolder(private val binding: BuyAgainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // Function to bind data to the views
        fun bind(itemName: String, itemPrice: String, itemImage: String) {
            // Set item name
            binding.buyAgainitemName.text = itemName
            // Set item price
            binding.buyAgainitemPrice.text = itemPrice
            // Load item image using Glide library
            val uri = Uri.parse(itemImage)
            Glide.with(requireContext).load(uri).into(binding.buyAgainitemImage)
        }
    }
}
