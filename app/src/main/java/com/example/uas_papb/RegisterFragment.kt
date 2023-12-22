package com.example.uas_papb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uas_papb.data_class.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private val users= firestore.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val usernameEditText: EditText = view.findViewById(R.id.username_input)
        val emailEditText: EditText = view.findViewById(R.id.email_input)
        val passwordEditText: EditText = view.findViewById(R.id.password_input)
        val registerButton: Button = view.findViewById(R.id.register)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            registerPublic(username, email, password)
        }

        return view
    }

    fun registerPublic(username:String, email: String, password: String) {
        // Implementasi registrasi public
        val newUser= User(username = username,email = email, password = password, role = "public")
        users.add(newUser).addOnSuccessListener { doc ->
            Toast.makeText(requireContext(), "Register berhasil", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
