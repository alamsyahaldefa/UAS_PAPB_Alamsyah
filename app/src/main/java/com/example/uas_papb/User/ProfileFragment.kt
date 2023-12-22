package com.example.uas_papb.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uas_papb.PrefManager
import com.example.uas_papb.data_class.User
import com.example.uas_papb.databinding.FragmentProfileBinding
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }
    private val firestore = FirebaseFirestore.getInstance()
    private val users= firestore.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val pref = PrefManager.getInstance(requireContext())
        val loginUsername = pref.getUsername()
        users.whereEqualTo("username", loginUsername).get().addOnSuccessListener { querySnapshot->
            if (!querySnapshot.isEmpty && querySnapshot != null) {
                val docSnapshot = querySnapshot.documents.get(0)
                val userData = docSnapshot.toObject(User::class.java)
                with(binding) {
                    nameProfile.text = userData!!.username
                    emailProfile.text = userData!!.email
                    btnLogout.setOnClickListener{
                        pref.clear()
                        requireActivity().finish()
                    }
                }
            }
        }

        return binding.root
    }

}