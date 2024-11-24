package com.example.event.Model

// Define a data class named MenuItem
data class MenuItem(
    val itemName: String? = null, // Variable to hold the name of the menu item, default value is null
    val itemPrice: String? = null, // Variable to hold the price of the menu item, default value is null
    val itemDescription: String? = null, // Variable to hold the description of the menu item, default value is null
    val itemImage: String? = null // Variable to hold the image URL of the menu item, default value is null
)

