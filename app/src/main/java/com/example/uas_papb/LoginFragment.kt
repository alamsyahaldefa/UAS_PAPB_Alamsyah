package com.example.uas_papb

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uas_papb.Admin.AdminActivity
import com.example.uas_papb.User.PublicActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private val users= firestore.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)


        val usernameEditText: EditText = view.findViewById(R.id.username_input)
        val passwordEditText: EditText = view.findViewById(R.id.password_input)
        val loginButton: Button = view.findViewById(R.id.login)

        val pref = PrefManager.getInstance(requireContext())

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            users.whereEqualTo("username", username).get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Toast.makeText(requireContext(),"Login Gagal", Toast.LENGTH_SHORT).show()
                }
                else {
                    val docSnapshot = querySnapshot.documents.get(0)
                    val storedPassword = docSnapshot.getString("password")
                    if (password.equals(storedPassword)){
                        val email = docSnapshot.getString("email")
                        val usn = docSnapshot.getString("username")
                        val role = docSnapshot.getString("role")

                        pref.setLoggedIn(true)
                        pref.saveUsername(usn.toString())
                        pref.saveRole(role.toString())

                        if(role=="public"){
                            val intent = Intent(requireContext(), PublicActivity::class.java)
                            startActivity(intent)
                        }
                        else if(role=="admin"){
                            val intent = Intent(requireContext(), AdminActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }

        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
