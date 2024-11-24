package com.example.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.event.Fragment.CongratsBottomSheet
import com.example.event.databinding.ActivityPayOutBinding
import com.example.event.Model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var eventItemName: ArrayList<String>
    private lateinit var eventItemPrice: ArrayList<String>
    private lateinit var eventItemImage: ArrayList<String>
    private lateinit var eventItemDescription: ArrayList<String>
    private lateinit var eventItemQuantities: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth and FirebaseDatabase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        // Retrieve user data
        setUserData()

        // Retrieve event item details from intent extras
        val intent = intent
        eventItemName = intent.getStringArrayListExtra("EventItemName") ?: ArrayList()
        eventItemPrice = intent.getStringArrayListExtra("EventItemPrice") ?: ArrayList()
        eventItemImage = intent.getStringArrayListExtra("EventItemImage") ?: ArrayList()
        eventItemDescription = intent.getStringArrayListExtra("EventItemDescription") ?: ArrayList()
        eventItemQuantities = intent.getIntegerArrayListExtra("EventItemQuantities") ?: ArrayList()

        // Calculate total amount and set it to the view
        totalAmount =  "Rs" + calculateTotalAmount().toString()
        binding.totalAmount.setText(totalAmount)

        // Set click listener for the back button
        binding.backeButton.setOnClickListener {
            finish() // Finish the activity and go back to the previous activity
        }

        // Set click listener for the "Place My Order" button
        binding.PlaceMyOrder.setOnClickListener {
            // Retrieve user input
            name = binding.name.text.toString().trim()
            address = binding.address.text.toString().trim()
            phone = binding.phone.text.toString().trim()

            // Validate user input
            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            } else {
                // If user input is valid, place the order
                placeOrder()
            }
        }
    }

    // Function to place the order
    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(
            userId, name, eventItemName, eventItemPrice, eventItemImage, eventItemQuantities,
            address, totalAmount, phone, time, itemPushKey, false, false
        )

        // Save order details to Firebase database
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            // If order placement is successful, show a confirmation message
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
            // Remove items from the user's cart
            removeItemFromCart()
            // Add the order to the user's order history
            addOrderToHistory(orderDetails)
        }.addOnFailureListener {
            // If order placement fails, show an error message
            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to add the order to the user's order history
    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory").child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {
                // Successfully added order to history
            }.addOnFailureListener {
                // Failed to add order to history
            }
    }

    // Function to remove items from the user's cart
    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }

    // Function to calculate the total amount of the order
    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until eventItemPrice.size) {
            var price = eventItemPrice[i]
            val lastChar = price.takeLast(2)
            val priceIntValue = if (lastChar == "Rs") {
                price.dropLast(2).toIntOrNull() ?: 0
            } else {
                price.toIntOrNull() ?: 0
            }
            var quantity = eventItemQuantities[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }


    // Function to set user data from Firebase to the UI
    private fun setUserData() {
        val user = auth.currentUser
        user?.let { currentUser ->
            val userId = currentUser.uid
            val userReference = databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names = snapshot.child("name").getValue(String::class.java) ?: ""
                        val addresses = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("phone").getValue(String::class.java) ?: ""

                        // Set user data to the UI
                        binding.apply {
                            name.setText(names)
                            address.setText(addresses)
                            phone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled
                }
            })
        }
    }
}

