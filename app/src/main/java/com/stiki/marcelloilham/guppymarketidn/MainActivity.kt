package com.stiki.marcelloilham.guppymarketidn

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stiki.marcelloilham.guppymarketidn.ui.AuctionFragment
import com.stiki.marcelloilham.guppymarketidn.ui.Community
import com.stiki.marcelloilham.guppymarketidn.ui.HomeFragment
import com.stiki.marcelloilham.guppymarketidn.ui.Maps
import com.stiki.marcelloilham.guppymarketidn.ui.Profile

class MainActivity : AppCompatActivity() {

    private lateinit var navview: BottomNavigationView
    private var backPressedTime: Long = 0
    private var currentFragmentTag: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        navview = findViewById(R.id.nav_view)

        // Check for extras in the intent
        val navigateTo = intent.getStringExtra("navigate_to")
        if (navigateTo != null) {
            when (navigateTo) {
                "HomeFragment" -> replaceFragment(HomeFragment(), "HomeFragment", false)
                "MapsFragment" -> replaceFragment(Maps(), "MapsFragment", false)
                "BidFragment" -> replaceFragment(AuctionFragment(), "BidFragment", false)
                "CommunityFragment" -> replaceFragment(Community(), "CommunityFragment", false)
                "ProfileFragment" -> replaceFragment(Profile(), "ProfileFragment", false)
                // Add other cases if necessary
            }
        } else {
            // Load default fragment if no intent extras
            replaceFragment(HomeFragment(), "HomeFragment", false)
        }

        navview.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment(), "HomeFragment", true)
                R.id.navigation_map -> replaceFragment(Maps(), "MapsFragment", true)
                R.id.navigation_bid -> replaceFragment(AuctionFragment(), "BidFragment", true)
                R.id.navigation_community -> replaceFragment(Community(), "CommunityFragment", true)
                R.id.navigation_profile -> replaceFragment(Profile(), "ProfileFragment", true)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String, addToBackStack: Boolean) {
        if (currentFragmentTag == tag) return

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (addToBackStack) {
            fragmentTransaction.replace(R.id.navhost, fragment, tag)
            fragmentTransaction.addToBackStack(tag)
        } else {
            fragmentTransaction.replace(R.id.navhost, fragment, tag)
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        fragmentTransaction.commit()
        currentFragmentTag = tag
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < 2000) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            backPressedTime = currentTime
        }
    }
}
