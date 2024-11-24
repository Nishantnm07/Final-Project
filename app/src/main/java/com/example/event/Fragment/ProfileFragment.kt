package com.example.event.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.event.LoginActivity
import com.example.event.Model.UserModel
import com.example.event.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setUserData()
        setupEditButton()
        setupSaveButton()
        setupLogoutButton()

        return binding.root
    }

    private fun setupEditButton() {
        binding.editButton.setOnClickListener {
            toggleEditTexts()
        }
    }

    private fun setupSaveButton() {
        binding.saveInfoButton.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val address = binding.address.text.toString()
            val phone = binding.phone.text.toString()

            updateUserData(name, email, address, phone)
        }
    }

    private fun setupLogoutButton() {
        binding.logoutoButton.setOnClickListener {
            logout()
        }
    }

    private fun toggleEditTexts() {
        binding.apply {
            name.isEnabled = !name.isEnabled
            email.isEnabled = !email.isEnabled
            address.isEnabled = !address.isEnabled
            phone.isEnabled = !phone.isEnabled
        }
    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId = auth.currentUser?.uid
        userId?.let { uid ->
            val userReference = database.getReference("user").child(uid)

            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userReference.setValue(userData)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Profile updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Profile update failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        userId?.let { uid ->
            val userReference = database.getReference("user").child(uid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        userProfile?.let {
                            binding.name.setText(it.name)
                            binding.address.setText(it.address)
                            binding.email.setText(it.email)
                            binding.phone.setText(it.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event
                }
            })
        }
    }

    private fun logout() {
        auth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Optional: Finish current activity after logout
    }
}
