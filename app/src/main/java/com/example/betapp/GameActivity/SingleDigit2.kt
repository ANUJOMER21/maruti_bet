package com.example.betapp.GameActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.betapp.GameActivity.Gridfragment.GridFragment
import com.example.betapp.GameActivity.Gridfragment.SigleDigitV2
import com.example.betapp.R

class SingleDigit2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_digit2)
        val fragment = SigleDigitV2() // Replace with your child fragment
        replaceFragment(fragment)
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framelayout, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}