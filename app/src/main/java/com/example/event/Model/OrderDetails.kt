package com.example.event.Model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

// Define the OrderDetails class
class OrderDetails() : Serializable {
    // Define properties for order details
    var userUid: String? = null // User UID
    var userName: String? = null // User name
    var itemNames: MutableList<String>? = null // Names of ordered items
    var itemImages: MutableList<String>? = null // Images of ordered items
    var itemPrices: MutableList<String>? = null // Prices of ordered items
    var itemQuantities: MutableList<Int>? = null // Quantities of ordered items
    var address: String? = null // Delivery address
    var totalPrice: String? = null // Total price of the order
    var phoneNumber: String? = null // User phone number
    var orderAccepted: Boolean = false // Indicates whether the order is accepted
    var paymentReceived: Boolean = false // Indicates whether the payment is received
    var itemPushKey: String? = null // Unique key for the order item
    var currentTime: Long = 0 // Timestamp of the order

    // Secondary constructor for parcelable implementation
    constructor(parcel: Parcel) : this() {
        // Read parcel data and assign to properties
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    // Secondary constructor for creating OrderDetails instances
    constructor(
        userId: String,
        name: String,
        eventItemName: ArrayList<String>,
        eventItemPrice: ArrayList<String>,
        eventItemImage: ArrayList<String>,
        eventItemQuantities: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phone: String,
        time: Long,
        itemPushKey: String?,
        b: Boolean,
        b1: Boolean
    ) : this() {
        // Initialize properties with provided values
        this.userUid = userId
        this.userName = name
        this.itemNames = eventItemName
        this.itemPrices = eventItemPrice
        this.itemImages = eventItemImage
        this.itemQuantities = eventItemQuantities
        this.address = address
        this.totalPrice = totalAmount
        this.phoneNumber = phone
        this.currentTime = time
        this.itemPushKey = itemPushKey
        this.orderAccepted = orderAccepted
        this.paymentReceived = paymentReceived
    }

    // Write data to parcel
    fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    // Describe parcel contents
    fun describeContents(): Int {
        return 0
    }

    // Companion object for parcelable implementation
    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        // Create OrderDetails object from parcel
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        // Create an array of OrderDetails objects
        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}
