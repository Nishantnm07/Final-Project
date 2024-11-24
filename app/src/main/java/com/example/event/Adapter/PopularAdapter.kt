package com.example.event.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.event.DetailsActivity
import com.example.event.databinding.PopulerItemBinding

class PopularAdapter(
    private val items: List<String>,
    private val price: List<String>,
    private val image: List<Int>,
    private val requireContext: Context
) : RecyclerView.Adapter<PopularAdapter.PouplerViewHolder>() {

    // Create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PouplerViewHolder {
        return PouplerViewHolder(
            PopulerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    // Bind data to view holder
    override fun onBindViewHolder(holder: PouplerViewHolder, position: Int) {
        val item = items[position]
        val images = image[position]
        val price = price[position]
        holder.bind(item, price, images)

        // Handle item click to open details activity
        holder.itemView.setOnClickListener {
            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", item)
                putExtra("MenuItemImage", images)
            }
            requireContext.startActivity(intent)
        }
    }

    // Return the number of items in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // View holder for popular items
    class PouplerViewHolder(private val binding: PopulerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.itemImage

        // Bind data to views in the ViewHolder
        fun bind(item: String, price: String, images: Int) {
            binding.apply {
                // Set item name, price, and image
                itemNamePopuler.text = item
                PricePopular.text = price
                imageView.setImageResource(images)
            }
        }
    }
}
