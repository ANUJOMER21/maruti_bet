package com.example.betapp.GameActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.betapp.GameActivity.Gridfragment.GridFragment2
import com.example.betapp.R

class DoublePatti2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_double_patti2)
        val fragment = GridFragment2() // Replace with your child fragment
        replaceFragment(fragment)

    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framelayout, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}