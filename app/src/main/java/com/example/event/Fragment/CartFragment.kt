package com.example.event.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.event.Adapter.CartAdapter
import com.example.event.Model.CartItems
import com.example.event.PayOutActivity
import com.example.event.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var itemName: MutableList<String>
    private lateinit var itemPrice: MutableList<String>
    private lateinit var itemDescription: MutableList<String>
    private lateinit var itemImagesUri: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance() // Initialize FirebaseDatabase

        retrieveCartItems()

        binding.proceedButton.setOnClickListener {
            // get order items details before proceeding to check out
            getOrderItemsDetail()
        }
        return binding.root
    }

    private fun getOrderItemsDetail() {
        val orderIdReference: DatabaseReference = database.reference.child("user").child(userId).child("CartItems")

        val itemName = mutableListOf<String>()
        val itemPrice = mutableListOf<String>()
        val itemImage = mutableListOf<String>()
        val itemDescription = mutableListOf<String>()
        // get items Quantities
        val itemQuantities = cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    // get the cartItems to respective List
                    val orderItems = itemSnapshot.getValue(CartItems::class.java)
                    // add items details in to list
                    orderItems?.itemName?.let { itemName.add(it) }
                    orderItems?.itemPrice?.let { itemPrice.add(it) }
                    orderItems?.itemDescription?.let { itemDescription.add(it) }
                    orderItems?.itemImage?.let { itemImage.add(it) }
                }
                orderNow(itemName, itemPrice, itemDescription, itemImage, itemQuantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(), "Order making Failed. Please Try Again.", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun orderNow(
        itemName: MutableList<String>,
        itemPrice: MutableList<String>,
        itemDescription: MutableList<String>,
        itemImage: MutableList<String>,
        itemQuantities: MutableList<Int>
    ) {
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            intent.putExtra("EventItemName", ArrayList(itemName))
            intent.putExtra("EventItemPrice", ArrayList(itemPrice))
            intent.putExtra("EventItemImage", ArrayList(itemImage))
            intent.putExtra("EventItemDescription", ArrayList(itemDescription))
            intent.putExtra("EventItemQuantities", ArrayList(itemQuantities))
            startActivity(intent)
        }
    }

    private fun retrieveCartItems() {
        userId = auth.currentUser?.uid ?: ""
        val itemReference: DatabaseReference = database.reference.child("user").child(userId).child("CartItems")
        // list to store cart items
        itemName = mutableListOf()
        itemPrice = mutableListOf()
        itemDescription = mutableListOf()
        itemImagesUri = mutableListOf()
        quantity = mutableListOf()

        // fetch data from the database
        itemReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    // get the cartItems object from the child node
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    // add cart items details to the list
                    cartItems?.itemName?.let { itemName.add(it) }
                    cartItems?.itemPrice?.let { itemPrice.add(it) }
                    cartItems?.itemDescription?.let { itemDescription.add(it) }
                    cartItems?.itemImage?.let { itemImagesUri.add(it) }
                    cartItems?.itemQuantity?.let { quantity.add(it) }
                }

                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = CartAdapter(
                    requireContext(),
                    itemName,
                    itemPrice,
                    itemDescription,
                    itemImagesUri,
                    quantity
                )

                binding.cartRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cartRecyclerView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not fetched", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
