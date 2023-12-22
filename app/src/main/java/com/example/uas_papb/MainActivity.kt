package com.example.uas_papb

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.uas_papb.Admin.AdminActivity
import com.example.uas_papb.User.PublicActivity
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager: ViewPager = findViewById(R.id.content)
        val tabLayout: TabLayout = findViewById(R.id.dashboard)

        val adapter = TabAdapter(supportFragmentManager)
        adapter.addFragment(LoginFragment(), "Login")
        adapter.addFragment(RegisterFragment(), "Register")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        val pref = PrefManager.getInstance(this)
        if (pref.isLoggedIn()) {
            if (pref.getRole() == "public") {
                val intent = Intent(this, PublicActivity::class.java)
                startActivity(intent)
            }
            else if (pref.getRole() == "admin"){
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
