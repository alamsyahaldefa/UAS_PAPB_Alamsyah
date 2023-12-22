package com.example.uas_papb.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uas_papb.R
import com.example.uas_papb.databinding.ActivityPublicBinding

class PublicActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPublicBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.buttonNavigation.setupWithNavController(navController)

    }

}