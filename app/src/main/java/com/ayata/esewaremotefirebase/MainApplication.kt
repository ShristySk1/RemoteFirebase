package com.ayata.esewaremotefirebase

import android.app.Application
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication: Application() {
    @Inject
     lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    override fun onCreate() {
        super.onCreate()
        firebaseRemoteConfig.activate()
        Log.d("testfetched", "onCreate: "+firebaseRemoteConfig.getString("app_subtitle"));
    }
}