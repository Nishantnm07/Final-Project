package com.example.event.Model

// Define a data class named CartItems
data class CartItems(
    var itemName: String? = null, // Variable to hold the name of the item, default value is null
    var itemPrice: String? = null, // Variable to hold the price of the item, default value is null
    var itemDescription: String? = null, // Variable to hold the description of the item, default value is null
    var itemImage: String? = null, // Variable to hold the image URL of the item, default value is null
    var itemQuantity: Int? = null, // Variable to hold the quantity of the item, default value is null
)

