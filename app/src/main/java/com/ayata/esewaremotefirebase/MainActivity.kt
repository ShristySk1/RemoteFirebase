package com.ayata.esewaremotefirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ayata.esewaremotefirebase.databinding.ActivityMainBinding
import com.ayata.esewaremotefirebase.presentation.DashboardFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment,DashboardFragment()).commit()
    }
}