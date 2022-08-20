package com.ayata.esewaremotefirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ayata.esewaremotefirebase.databinding.ActivityMainBinding
import com.ayata.esewaremotefirebase.presentation.DashboardFragment
import com.ayata.esewaremotefirebase.presentation.DetailFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, DashboardFragment())
            .commit()
        initButton()
    }
    private fun initButton() {
        binding.btnAddNote.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, DetailFragment())
                .commit()
        }
    }
}