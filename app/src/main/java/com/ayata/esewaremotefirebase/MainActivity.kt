package com.ayata.esewaremotefirebase

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ayata.esewaremotefirebase.data.RemoteConfigManager
import com.ayata.esewaremotefirebase.databinding.ActivityMainBinding
import com.ayata.esewaremotefirebase.presentation.DashboardFragment
import com.ayata.esewaremotefirebase.presentation.DetailFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var remoteConfigManager: RemoteConfigManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, DashboardFragment())
            .commit()
        initButton()
        initRemoteConfig()

    }

    private fun initRemoteConfig() {
        val mainBackground=remoteConfigManager.getThemeData().color
        Log.d("testcolor", "initRemoteConfig: "+mainBackground);
        val plusColor=remoteConfigManager.getThemeData().content_color
        binding.btnAddNote.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(mainBackground)));
        binding.btnAddNote.setColorFilter(Color.parseColor(plusColor))
    }

    private fun initButton() {
        binding.btnAddNote.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, DetailFragment()).addToBackStack("null")
                .commit()
        }
    }

}