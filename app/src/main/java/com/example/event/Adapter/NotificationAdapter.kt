package com.example.event.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.event.databinding.NotificationItemBinding
import java.util.ArrayList

class NotificationAdapter(private var notification: ArrayList<String>, private var notificationImage: ArrayList<Int>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    // Create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        // Inflate the layout for each item
        val binding =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    // Bind data to view holder
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }

    // Return the number of items in the list
    override fun getItemCount(): Int = notification.size

    inner class NotificationViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Bind data to views in the ViewHolder
        fun bind(position: Int) {
            binding.apply {
                // Set notification text and image
                notificationTextView.text = notification[position]
                notificationImageView.setImageResource(notificationImage[position])
            }
        }
    }
}
