package com.example.event.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.event.databinding.RecentBuyItemBinding

class RecentBuyAdapter(
    private var context: Context,
    private var itemNameList: ArrayList<String>,
    private var itemImageList: ArrayList<String>,
    private var itemPriceList: ArrayList<String>,
    private var itemQuantityList: ArrayList<Int>
) : RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {

    // Create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        // Inflate the layout for each item
        val binding = RecentBuyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentViewHolder(binding)
    }

    // Return the number of items in the list
    override fun getItemCount(): Int = itemNameList.size

    // Bind data to view holder
    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(position)
    }

    // View holder for recent items
    inner class RecentViewHolder(private val binding: RecentBuyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind data to views in the ViewHolder
        fun bind(position: Int) {
            binding.apply {
                // Set item name, price, and quantity
                itemName.text = itemNameList[position]
                itemPrice.text = itemPriceList[position]
                itemQuantity.text = itemQuantityList[position].toString()

                // Load image using Glide
                val uriString = itemImageList[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(itemImage)
            }
        }
    }
}
