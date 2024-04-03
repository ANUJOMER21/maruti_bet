package com.example.betapp.GameActivity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.betapp.GameActivity.Gridfragment.GridFragment
import com.example.betapp.GameActivity.Gridfragment.ViewmodelGrid1
import com.example.betapp.R
import com.example.betapp.model.BetItem
import com.google.android.material.tabs.TabLayout

class SinglePatti2 : AppCompatActivity() {
    private lateinit var viewModel: ViewmodelGrid1
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_patti2)
        viewModel = ViewModelProvider(this).get(ViewmodelGrid1::class.java)
        val toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)

        val fragment = GridFragment() // Replace with your child fragment
        replaceFragment(fragment)

    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framelayout, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}