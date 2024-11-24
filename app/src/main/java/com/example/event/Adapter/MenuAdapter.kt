package com.example.event.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.event.DetailsActivity
import com.example.event.Model.MenuItem
import com.example.event.databinding.MenuItemBinding

class MenuAdapter(
    private val menuItems: List<MenuItem>, // List of menu items
    private val requireContext: Context // Context reference
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    // Create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        // Inflate the layout for each item
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    // Bind data to view holder
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    // Return the number of items in the list
    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Set item click listener
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsActivity(position)
                }
            }
        }

        // Open DetailsActivity when an item is clicked
        private fun openDetailsActivity(position: Int) {
            val menuItem = menuItems[position]

            // Create an intent to open DetailsActivity and pass data
            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.itemName)
                putExtra("MenuItemImage", menuItem.itemImage)
                putExtra("MenuItemDescription", menuItem.itemDescription)
                putExtra("MenuItemPrice", menuItem.itemPrice)
            }

            // Start the DetailsActivity
            requireContext.startActivity(intent)
        }

        // Bind data to the views in the ViewHolder
        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menuitemName.text = menuItem.itemName
                menuPrice.text = menuItem.itemPrice
                // Load item image using Glide
                val uri = Uri.parse(menuItem.itemImage)
                Glide.with(requireContext).load(uri).into(menuImage)
            }
        }
    }
}
